package no.ntnu.ihb.vico.physics

import no.ntnu.ihb.vico.components.Transform
import no.ntnu.ihb.vico.core.Entity
import org.joml.Matrix4d
import org.joml.Vector3d
import org.joml.Vector3dc

class ConstraintData(
    val e1: Entity,
    val e2: Entity?,
    val anchor: Vector3dc = Vector3d()
) {

    val hasConnectedBody = e2 != null

    val rb1 by lazy {
        e1.get<RigidBodyComponent>()
    }

    val rb2 by lazy {
        e2?.get<RigidBodyComponent>() ?: throw IllegalStateException()
    }

    val frame1 by lazy { Matrix4d().setTranslation(anchor) }

    /*val frame2 by lazy {

        e2?.let { e2 ->
            e2.getComponent(TransformComponent::class.java).getWorld().invert().mul(e1.getComponent(TransformComponent::class.java))
        } ?: null


    }*/



    init {
        require(e1.has(Transform::class.java))
        require(e1.has(RigidBodyComponent::class.java))
        e2?.also { e2 ->
            require(e2.has(Transform::class.java))
            require(e2.has(RigidBodyComponent::class.java))
        }
    }

}
