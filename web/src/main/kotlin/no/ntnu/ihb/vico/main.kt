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

fun main() {
    embeddedServer(
        Netty,
        port = 8000,
        watchPaths = listOf("classes", "resources")
    ) {

        install(WebSockets)

        routing {
            get("/") {
                call.respondText(ContentType.Text.Html) {
                    KtorServer::class.java.classLoader.getResourceAsStream("index.html").reader().readText()
                }
            }
            get("/visual") {
                call.respondText(ContentType.Text.Html) {
                    KtorServer::class.java.classLoader.getResourceAsStream("visual.html").reader().readText()
                }
            }


            webSocket("/visual") {

                try {
                    while (true) {

                        when ((incoming.receive() as Frame.Text).readText()) {
                            "subscribe" -> {

                            }
                            "close" -> {
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
