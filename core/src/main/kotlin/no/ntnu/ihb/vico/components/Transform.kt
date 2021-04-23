package no.ntnu.ihb.vico.components

import no.ntnu.ihb.vico.core.Component
import no.ntnu.ihb.vico.core.Mappable
import no.ntnu.ihb.vico.core.Properties
import no.ntnu.ihb.vico.core.RealLambdaProperty
import no.ntnu.ihb.vico.math.Frame
import no.ntnu.ihb.vico.math.fromArray
import no.ntnu.ihb.vico.math.toArray
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3d
import org.joml.Vector3f

class Transform : Frame(), Component, Mappable {

    override val properties = Properties()

    init {
        val tmp = Vector3d()
        properties.registerProperties(
            RealLambdaProperty(this, "localPosition", 3,
                getter = { ref -> getLocalTranslation(tmp).toArray(ref) },
                setter = { values -> setLocalTranslation(tmp.fromArray(values)) }
            ),
            RealLambdaProperty(
                this, "worldPosition", 3,
                getter = { ref -> getTranslation(tmp).toArray(ref) },
                setter = { values -> setTranslation(tmp.fromArray(values)) }
            )
        )
    }

    override fun getData(setup: Boolean): Map<String, Any> {
        val w = getWorldMatrixf(Matrix4f())
        return mapOf(
            "position" to w.getTranslation(Vector3f()),
            "quaternion" to w.getNormalizedRotation(Quaternionf())
        )
    }
}
