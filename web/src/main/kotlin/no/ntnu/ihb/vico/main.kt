package no.ntnu.ihb.vico

import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val res = KtorServer::class.java.classLoader.getResource("js")
    val path = Paths.get(res.toURI())
    Files.walk(path, 1).forEach { println(it) }
//    for (r in res) {
//        println(r)
//    }

//    embeddedServer(
//        Netty,
//        port = 8000,
//        watchPaths = listOf("classes", "resources")
//    ) {
//
//        install(WebSockets)
//
//        routing {
//
//            get("/") {
//                call.respondHtml {
//                    makeIndex()
//                }
//            }
//            get("/visual") {
//                call.respondHtml {
//                    makeVisual()
//                }
//            }
//
//
//            webSocket("/visual") {
//
//                try {
//                    while (true) {
//
//                        when ((incoming.receive() as Frame.js.Text).readText()) {
//                            "subscribe" -> {
//
//                            }
//                            "close" -> {
//                                close(CloseReason(CloseReason.Codes.NORMAL, "Closing connection.."))
//                            }
//                        }
//                    }
//                } catch (ex: ClosedReceiveChannelException) {
//
//                } catch (ex: Throwable) {
//                    ex.printStackTrace()
//                }
//
//            }
//        }
//    }.start(wait = false)
}
