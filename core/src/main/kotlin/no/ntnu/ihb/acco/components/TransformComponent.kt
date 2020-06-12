package no.ntnu.ihb.acco.components

import no.ntnu.ihb.acco.core.CosimulationComponent
import no.ntnu.ihb.acco.core.RealLambdaVar
import no.ntnu.ihb.acco.core.Var
import no.ntnu.ihb.acco.math.Frame
import no.ntnu.ihb.acco.math.IFrame
import no.ntnu.ihb.acco.math.fromArray
import no.ntnu.ihb.acco.math.toArray
import org.joml.Vector3d

class TransformComponent internal constructor() : IFrame by Frame(), CosimulationComponent {

    override val variables: Map<String, Var<*>> by lazy {
        val tmp = Vector3d()
        mapOf<String, Var<*>>(
            "localPosition" to RealLambdaVar(3,
                getter = { ref -> getLocalTranslation(tmp).toArray(ref) },
                setter = { values -> setLocalTranslation(tmp.fromArray(values)) }
            ))
    }
}
