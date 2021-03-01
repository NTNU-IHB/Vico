package no.ntnu.ihb.vico

import com.google.gson.GsonBuilder
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
import no.ntnu.ihb.vico.render.Geometry
import org.joml.Matrix4fc
import org.joml.Quaterniond
import org.joml.Vector3d
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.name

class KtorServer(
    private val port: Int
) : ObserverSystem(Family.all(Transform::class.java, Geometry::class.java).build()) {

    private var app: NettyApplicationEngine? = null
    private val subscribers = Collections.synchronizedList(mutableListOf<SendChannel<Frame>>())

    private val gson = GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .create()

    @ExperimentalPathApi
    override fun postInit() {
        app = embeddedServer(Netty, port = port, watchPaths = listOf("classes", "resources")) {

            install(WebSockets)

            routing {

                val cl = KtorServer::class.java.classLoader
                val res = cl.getResource("js")!!
                val path = Paths.get(res.toURI())
                Files.walk(path, 1).forEach {
                    val name = it.name
                    if (name.endsWith(".js")) {
                        get("js/${name}") {
                            call.respondText(ContentType.Application.OctetStream) {
                                cl.getResourceAsStream("js/${name}").bufferedReader().readText()
                            }
                        }
                    }
                }

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

                                    outgoing.send(Frame.Text(makeJson(true)))

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
                        // do nothing
                    } catch (ex: Throwable) {
                        ex.printStackTrace()
                    } finally {
                        subscribers.remove(outgoing)
                    }

                }
            }

        }.start(wait = false)
    }

    private fun makeJson(includeGeometry: Boolean): String {

        val data = entities.associate { e ->
            val t = e.get<Transform>()
            val g = e.get<Geometry>()

            val m = t.getWorldMatrix()
            e.name to JsonTransform(
                m.getTranslation(Vector3d()),
                m.getNormalizedRotation(Quaterniond()),
                if (includeGeometry) toJson(g) else null
            )

        }

        val type = if (includeGeometry) "setup" else "update"
        return gson.toJson(JsonPayload(type, data))

    }

    override fun observe(currentTime: Double) {

        synchronized(subscribers) {
            runBlocking {
                subscribers.forEach { sub ->
                    sub.send(Frame.Text(makeJson(false)))
                }
            }
        }

    }

    override fun close() {
        app?.stop(500, 500)
    }
}

fun toJson(g: Geometry): JsonGeometry {
    return JsonGeometry(
        g.offset,
        g.color,
        g.opacity,
        g.visible,
        g.wireframe
    )
}

class JsonPayload(
    val type: String,
    val data: Any
)

class JsonGeometry(
    val offsetTransform: Matrix4fc? = null,
    val color: Int,
    val opacity: Float,
    val visible: Boolean,
    val wireframe: Boolean
)

class JsonTransform(
    val position: Vector3d,
    val rotation: Quaterniond,
    val geometry: JsonGeometry? = null
)

