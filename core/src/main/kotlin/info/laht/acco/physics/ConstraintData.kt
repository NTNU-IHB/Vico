package info.laht.acco.physics

import info.laht.acco.components.TransformComponent
import info.laht.acco.core.Entity
import info.laht.acco.math.Matrix4
import info.laht.acco.math.Vector3

class ConstraintData(
    val e1: Entity,
    val e2: Entity?,
    val anchor: Vector3 = Vector3()
) {

    val hasConnectedBody = e2 != null

    val rb1 by lazy {
        e1.getComponent(RigidBodyComponent::class.java)
    }

    val rb2 by lazy {
        e2?.getComponent(RigidBodyComponent::class.java) ?: throw IllegalStateException()
    }

    val frame1 by lazy { Matrix4().setPosition(anchor) }

    /*val frame2 by lazy {

        e2?.let { e2 ->
            e2.getComponent(TransformComponent::class.java).getWorld().invert().mul(e1.getComponent(TransformComponent::class.java))
        } ?: null


    }*/



    init {
        require(e1.hasComponent(TransformComponent::class.java))
        require(e1.hasComponent(RigidBodyComponent::class.java))
        e2?.also { e2 ->
            require(e2.hasComponent(TransformComponent::class.java))
            require(e2.hasComponent(RigidBodyComponent::class.java))
        }
    }

}
