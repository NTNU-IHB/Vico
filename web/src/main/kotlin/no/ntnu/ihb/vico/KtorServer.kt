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
import no.ntnu.ihb.vico.render.Geometry
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URL
import java.util.*
import kotlin.concurrent.thread
import kotlin.io.path.ExperimentalPathApi


class KtorServer @JvmOverloads constructor(
    private val port: Int,
    private val openBrowser: Boolean = true
) : ObserverSystem(Family.all().build()) {

    private var t0: Long = 0L
    private var app: NettyApplicationEngine? = null
    private val subscribers = Collections.synchronizedList(mutableListOf<SendChannel<Frame>>())

    private lateinit var updateFrame: String

    private fun sendSubs(frame: JsonFrame) {
        val json = frame.toJson()
        synchronized(subscribers) {
            runBlocking {
                subscribers.forEach { sub ->
                    sub.send(Frame.Text(json))
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
                            }.collect { frame ->

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
                    } catch (ex: Throwable) {
                        ex.printStackTrace()
                    } finally {
                        synchronized(subscribers) {
                            subscribers.remove(outgoing)
                        }
                    }

                }

                webSocket("chart") {

                    try {

                        incoming.consumeAsFlow()
                            .mapNotNull {
                                val read = (it as Frame.Text).readText()
                                JsonFrame.fromJson(read)
                            }.collect { frame ->

                                when (frame.action) {

                                }

                            }

                    } catch (ex: ClosedReceiveChannelException) {
                    } catch (ex: Throwable) {
                        ex.printStackTrace()
                    }

                }

            }

            println(
                """
                    
                Serving on http://$hostName:$port (public)
                Serving on http://127.0.0.1:$port (local)
                
            """.trimIndent()
            )

            if (openBrowser) {
                try {
                    java.awt.Desktop.getDesktop().browse(URI("http://127.0.0.1:$port/index.html"))
                } catch (ex: UnsupportedOperationException) {
                    // Do nothing
                }
            }

        }.start(wait = false)

        updateFrame = JsonFrame(
            action = "update",
            data = engine.toMap(false)
        ).toJson()

    }

    override fun observe(currentTime: Double) {

        val timeSinceUpdate = (System.currentTimeMillis() - t0).toDouble() / 1000
        if (timeSinceUpdate > MAX_UPDATE_RATE) {

            updateFrame = JsonFrame(
                action = "update",
                data = engine.toMap(false)
            ).toJson()

            t0 = System.currentTimeMillis()
        }
    }

    override fun entityAdded(entity: Entity) {

        if (initialized) {
            thread(true) {
                Thread.sleep(100)
                sendSubs(
                    JsonFrame(
                        action = "add",
                        data = entity.toMap(true)
                    )
                )
            }
        }

        entity.getOrNull<Geometry>()?.also { g ->
            g.addEventListener("onColorChanged") { evt ->

                sendSubs(
                    JsonFrame(
                        action = "colorChanged",
                        data = mapOf(
                            "name" to entity.name,
                            "color" to evt.value
                        )
                    )
                )

            }
            g.addEventListener("onVisibilityChanged") { evt ->
                sendSubs(
                    JsonFrame(
                        action = "visibilityChanged",
                        data = mapOf(
                            "name" to entity.name,
                            "visible" to evt.value
                        )
                    )
                )
            }
            g.addEventListener("onWireframeChanged") { evt ->
                sendSubs(
                    JsonFrame(
                        action = "wireframeChanged",
                        data = mapOf(
                            "name" to entity.name,
                            "wireframe" to evt.value
                        )
                    )
                )
            }
        }

    }

    override fun entityRemoved(entity: Entity) {

        sendSubs(
            JsonFrame(
                action = "remove",
                data = entity.name
            )
        )

    }

    override fun close() {
        try {
            app?.stop(500, 500)
        } catch (ex: Exception) {
            // do nothing
        }
    }

    private companion object {
        private const val MAX_UPDATE_RATE = 1.0 / 60

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
