package no.ntnu.ihb.vico

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.runBlocking
import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Family
import no.ntnu.ihb.vico.core.ObserverSystem
import java.util.*

class KtorServer(
    private val port: Int
) : ObserverSystem(Family.all(Transform::class.java).build()) {

    private var app: NettyApplicationEngine? = null
    private val subscribers = Collections.synchronizedList(mutableListOf<SendChannel<Frame>>())

    override fun postInit() {
        app = embeddedServer(Netty, port = port, watchPaths = listOf("classes", "resources")) {

            install(WebSockets)

            routing {


                get("/") {
                    call.respondText(ContentType.Text.Html) {
                        KtorServer::class.java.classLoader.getResourceAsStream("index.html")!!.reader().readText()
                    }
                }
                get("/visual") {
                    call.respondText(ContentType.Text.Html) {
                        KtorServer::class.java.classLoader.getResourceAsStream("visual.html")!!.reader().readText()
                    }
                }

                webSocket("/visual") {

                    try {
                        while (true) {

                            when ((incoming.receive() as Frame.Text).readText()) {
                                "subscribe" -> {
                                    synchronized(subscribers) {
                                        subscribers.add(outgoing)
                                    }
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
                    }

                }
            }

        }.start(wait = false)
    }

    override fun observe(currentTime: Double) {

        synchronized(subscribers) {
            runBlocking {
                subscribers.forEach { sub ->
                    sub.send(Frame.Text("hello"))
                }
            }
        }

    }

    override fun close() {
        app?.stop(500, 500)
    }
}
