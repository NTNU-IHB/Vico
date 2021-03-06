package no.ntnu.ihb.vico

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.runBlocking
import no.ntnu.ihb.vico.core.Entity
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.core.ObserverSystem
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URL
import java.util.*
import kotlin.concurrent.thread
import kotlin.io.path.ExperimentalPathApi


class KtorServer(
    private val port: Int
) : ObserverSystem(Family.all().build()) {

    private var t0: Long = 0L
    private var app: NettyApplicationEngine? = null
    private val subscribers = Collections.synchronizedList(mutableListOf<SendChannel<Frame>>())

    private lateinit var updateFrame: String

    override fun entityAdded(entity: Entity) {

        thread(true) {
            Thread.sleep(100)
            synchronized(subscribers) {
                runBlocking {

                    subscribers.forEach { sub ->
                        sub.send(
                            Frame.Text(
                                JsonFrame(
                                    action = "add",
                                    data = entity.toMap(true)
                                ).toJson()
                            )
                        )
                    }

                }
            }

        }

    }

    override fun entityRemoved(entity: Entity) {
        synchronized(subscribers) {
            runBlocking {

                subscribers.forEach { sub ->
                    sub.send(
                        Frame.Text(
                            JsonFrame(
                                action = "remove",
                                data = entity.name
                            ).toJson()
                        )
                    )
                }

            }
        }
    }

    @ExperimentalPathApi
    override fun postInit() {
        app = embeddedServer(Netty, port = port) {

            install(WebSockets)

            routing {

                resources("/")

                val cl = KtorServer::class.java.classLoader
                get("/") {
                    call.respondText(ContentType.Text.Html) {
                        cl.getResourceAsStream("index.html")!!.bufferedReader().readText()
                    }
                }

                get("/assets") {
                    val file = File(call.request.queryString())
                    if (file.exists()) {
                        call.respondFile(file)
                    }
                }

                webSocket("/visual") {

                    try {

                        incoming.consumeAsFlow()
                            .mapNotNull {
                                val read = (it as Frame.Text).readText()
                                JsonFrame.fromJson(read)
                            }
                            .collect { frame ->

                                when (frame.action) {
                                    "subscribe" -> {
                                        outgoing.send(
                                            Frame.Text(
                                                JsonFrame(
                                                    action = "setup",
                                                    data = engine.toMap(true)
                                                ).toJson()
                                            )
                                        )
                                        synchronized(subscribers) {
                                            subscribers.add(outgoing)
                                        }
                                    }
                                    "update" -> {
                                        outgoing.send(Frame.Text(updateFrame))
                                    }
                                    "keyPressed" -> {
                                        val key = frame.data as String
                                        engine.registerKeyPress(key)
                                    }
                                    "close" -> {
                                        synchronized(subscribers) {
                                            subscribers.remove(outgoing)
                                        }
                                        close(CloseReason(CloseReason.Codes.NORMAL, "Closing connection.."))
                                    }
                                }

                            }

                    } catch (ex: ClosedReceiveChannelException) {
                        ex.printStackTrace()
                    } catch (ex: Throwable) {
                        ex.printStackTrace()
                    } finally {
                        synchronized(subscribers) {
                            subscribers.remove(outgoing)
                        }
                    }

                }
            }

            println(
                """
                Serving on http://$hostName:$port (public)
                Serving on http://127.0.0.1:$port (local)
            """.trimIndent()
            )

            java.awt.Desktop.getDesktop().browse(URI("http://127.0.0.1:$port/index.html"))

        }.start(wait = false)

        updateFrame = JsonFrame(
            action = "update",
            data = engine.toMap(false)
        ).toJson()

    }

    override fun observe(currentTime: Double) {

        val timeSinceUpdate = (System.currentTimeMillis() - t0).toDouble() / 1000
        if (timeSinceUpdate > MAX_SUBSCRIPTION_RATE) {

            updateFrame = JsonFrame(
                action = "update",
                data = engine.toMap(false)
            ).toJson()

            t0 = System.currentTimeMillis()
        }
    }

    override fun close() {
        try {
            app?.stop(500, 500)
        } catch (ex: Exception) {
            // do nothing
        }
    }

    private companion object {
        private const val MAX_SUBSCRIPTION_RATE = 1.0 / 60

        private val hostName by lazy {
            var hostAddress: String? = "127.0.0.1"
            try {
                val url = URL("http://checkip.amazonaws.com/")
                val `in` = url.openStream().bufferedReader()
                val read = `in`.readLine().trim { it <= ' ' }
                if (read.isNotEmpty()) {
                    hostAddress = read
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            hostAddress
        }

    }

}
