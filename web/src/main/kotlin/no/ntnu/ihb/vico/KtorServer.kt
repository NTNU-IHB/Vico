package no.ntnu.ihb.vico

import com.google.gson.GsonBuilder
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.http.content.*
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
import no.ntnu.ihb.vico.render.mesh.*
import org.joml.Quaterniond
import org.joml.Vector3d
import java.util.*
import kotlin.io.path.ExperimentalPathApi

class KtorServer(
    private val port: Int
) : ObserverSystem(Family.all(Transform::class.java, Geometry::class.java).build()) {

    private var app: NettyApplicationEngine? = null
    private val subscribers = Collections.synchronizedList(mutableListOf<SendChannel<Frame>>())

    private var t0: Long = 0L

    private val gson = GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .create()

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

                webSocket("/visual") {

                    try {
                        while (true) {

                            val recv = (incoming.receive() as Frame.Text).readText()
                            val frame = gson.fromJson(recv, JsonFrame::class.java)

                            when (frame.action) {
                                "subscribe" -> {
                                    outgoing.send(Frame.Text(makeJson(true)))
                                    synchronized(subscribers) {
                                        subscribers.add(outgoing)
                                    }
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

    override fun observe(currentTime: Double) {

        val timeSinceUpdate = (System.currentTimeMillis() - t0).toDouble() / 1000
        if (timeSinceUpdate > MAX_SUBSCRIPTION_RATE) {
            synchronized(subscribers) {
                runBlocking {
                    subscribers.forEach { sub ->
                        sub.send(Frame.Text(makeJson(false)))
                    }
                }
            }
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

    private companion object {
        private val MAX_SUBSCRIPTION_RATE = 1.0 / 60
    }

}

private class JsonFrame(
    val action: String,
    val data: Any? = null
)

private fun toJson(g: Geometry): JsonGeometry {
    val shape = when (val shape = g.shape) {
        is PlaneShape -> shape.toJsonShape()
        is BoxShape -> shape.toJsonShape()
        is SphereShape -> shape.toJsonShape()
        is CylinderShape -> shape.toJsonShape()
        is CapsuleShape -> shape.toJsonShape()
        else -> throw UnsupportedOperationException()
    }
    val offset = g.offset?.get(FloatArray(16))
    return JsonGeometry(
        offset,
        g.color,
        g.opacity,
        g.visible,
        g.wireframe,
        shape
    )
}

private class JsonPayload(
    val type: String,
    val data: Any
)

private class JsonShape(
    val type: String,
    val shape: Map<String, Any>
)

private fun PlaneShape.toJsonShape(): JsonShape {
    return JsonShape(
        "plane",
        mapOf(
            "width" to width,
            "height" to height
        )
    )
}

private fun BoxShape.toJsonShape(): JsonShape {
    return JsonShape(
        "box",
        mapOf(
            "width" to width,
            "height" to height,
            "depth" to depth
        )
    )
}

private fun SphereShape.toJsonShape(): JsonShape {
    return JsonShape(
        "sphere",
        mapOf(
            "radius" to radius
        )
    )
}

private fun CylinderShape.toJsonShape(): JsonShape {
    return JsonShape(
        "cylinder",
        mapOf(
            "radius" to radius,
            "height" to height
        )
    )
}

private fun CapsuleShape.toJsonShape(): JsonShape {
    return JsonShape(
        "capsule",
        mapOf(
            "radius" to radius,
            "height" to height
        )
    )
}

private class JsonGeometry(
    val offset: FloatArray? = null,
    val color: Int,
    val opacity: Float,
    val visible: Boolean,
    val wireframe: Boolean,
    val shape: JsonShape
)

private class JsonTransform(
    val position: Vector3d,
    val rotation: Quaterniond,
    val geometry: JsonGeometry? = null
)

