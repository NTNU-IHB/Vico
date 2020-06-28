package no.ntnu.ihb.acco.components

import no.ntnu.ihb.acco.core.Component
import no.ntnu.ihb.acco.core.RealLambdaProperty
import no.ntnu.ihb.acco.math.Frame
import no.ntnu.ihb.acco.math.IFrame
import no.ntnu.ihb.acco.math.fromArray
import no.ntnu.ihb.acco.math.toArray
import org.joml.Vector3d

class TransformComponent : IFrame by Frame(), Component() {

    init {
        val tmp = Vector3d()
        registerProperties(listOf(
            RealLambdaProperty("localPosition", 3,
                getter = { ref -> getLocalTranslation(tmp).toArray(ref) },
                setter = { values -> setLocalTranslation(tmp.fromArray(values)) }
            ),
            RealLambdaProperty(
                "worldPosition", 3,
                getter = { ref -> getTranslation(tmp).toArray(ref) },
                setter = { values -> setTranslation(tmp.fromArray(values)) }
            ))
        )
    }

}
