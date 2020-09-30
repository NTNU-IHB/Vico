package no.ntnu.ihb.vico.components

import no.ntnu.ihb.vico.core.Component
import no.ntnu.ihb.vico.core.Properties
import no.ntnu.ihb.vico.core.RealLambdaProperty
import no.ntnu.ihb.vico.math.Frame
import no.ntnu.ihb.vico.math.fromArray
import no.ntnu.ihb.vico.math.toArray
import org.joml.Vector3d

class Transform : Frame(), Component {

    override val properties = Properties()

    init {
        val tmp = Vector3d()
        properties.registerProperties(
                RealLambdaProperty("localPosition", 3,
                        getter = { ref -> getLocalTranslation(tmp).toArray(ref) },
                        setter = { values -> setLocalTranslation(tmp.fromArray(values)) }
                ),
                RealLambdaProperty(
                        "worldPosition", 3,
                        getter = { ref -> getTranslation(tmp).toArray(ref) },
                        setter = { values -> setTranslation(tmp.fromArray(values)) }
                )
        )
    }

}
