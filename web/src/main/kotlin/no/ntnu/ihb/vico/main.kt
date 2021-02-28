package no.ntnu.ihb.vico

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.cio.websocket.*
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
                call.respondHtml {
                    makeIndex()
                }
            }
            get("/visual") {
                call.respondHtml {
                    makeVisual()
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
