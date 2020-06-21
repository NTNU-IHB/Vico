package no.ntnu.ihb.acco.components

import no.ntnu.ihb.acco.core.Component
import no.ntnu.ihb.acco.core.RealLambdaVar
import no.ntnu.ihb.acco.math.Frame
import no.ntnu.ihb.acco.math.IFrame
import no.ntnu.ihb.acco.math.fromArray
import no.ntnu.ihb.acco.math.toArray
import org.joml.Vector3d

class TransformComponent internal constructor() : IFrame by Frame(), Component() {

    init {
        val tmp = Vector3d()
        registerVariables(listOf(
            RealLambdaVar("localPosition", 3,
                getter = { ref -> getLocalTranslation(tmp).toArray(ref) },
                setter = { values -> setLocalTranslation(tmp.fromArray(values)) }
            ),
            RealLambdaVar("worldPosition", 3,
                getter = { ref -> getTranslation(tmp).toArray(ref) },
                setter = { values -> setTranslation(tmp.fromArray(values)) }
            ))
        )
    }

}
