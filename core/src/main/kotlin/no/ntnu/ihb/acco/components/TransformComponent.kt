package no.ntnu.ihb.acco.components

import no.ntnu.ihb.acco.core.Component
import no.ntnu.ihb.acco.core.RealLambdaProperty
import no.ntnu.ihb.acco.math.Frame
import no.ntnu.ihb.acco.math.fromArray
import no.ntnu.ihb.acco.math.toArray
import org.joml.Vector3d

class TransformComponent : Component() {

    val frame = Frame()

    init {
        val tmp = Vector3d()
        registerProperties(
            RealLambdaProperty("localPosition", 3,
                getter = { ref -> frame.getLocalTranslation(tmp).toArray(ref) },
                setter = { values -> frame.setLocalTranslation(tmp.fromArray(values)) }
            ),
            RealLambdaProperty(
                "worldPosition", 3,
                getter = { ref -> frame.getTranslation(tmp).toArray(ref) },
                setter = { values -> frame.setTranslation(tmp.fromArray(values)) }
            )
        )
    }

    fun setParent(parent: TransformComponent) {
        frame.setParent(parent.frame)
    }

}
