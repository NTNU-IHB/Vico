package no.ntnu.ihb.acco.physics.bullet

import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.Bullet
import com.badlogic.gdx.physics.bullet.collision.*
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState
import no.ntnu.ihb.acco.components.TransformComponent
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.System
import no.ntnu.ihb.acco.physics.ColliderComponent
import no.ntnu.ihb.acco.physics.MotionControl
import no.ntnu.ihb.acco.physics.RigidBodyComponent
import org.joml.Matrix4d


class BulletSystem : System(
    Family.all(TransformComponent::class.java).one(RigidBodyComponent::class.java).build()
) {

    private val tmpMat = Matrix4d()
    private val tmpVec = Vector3()
    private val tmpQuat = Quaternion()

    private val world: btDiscreteDynamicsWorld
    private val rbMap = mutableMapOf<Entity, btRigidBody>()

    init {

        val dbvtBroadphase = btDbvtBroadphase()
        val defaultCollisionConfiguration = btDefaultCollisionConfiguration()
        val collisionDispatcher = btCollisionDispatcher(defaultCollisionConfiguration)
        val sequentialImpulseConstraintSolver = btSequentialImpulseConstraintSolver()
        world = btDiscreteDynamicsWorld(
            collisionDispatcher,
            dbvtBroadphase,
            sequentialImpulseConstraintSolver,
            defaultCollisionConfiguration
        )
        world.gravity = Vector3(0f, 0f, -9.81f)

    }

    override fun step(currentTime: Double, stepSize: Double) {
        world.stepSimulation(stepSize.toFloat())
        entities.forEach { entity ->

            val tc = entity.getComponent(TransformComponent::class.java)
            val rc = entity.getComponent(RigidBodyComponent::class.java)
            rbMap[entity]?.also { rb ->
                tc.setTransform(tmpMat.copy(rb.worldTransform))
                rc.linearVelocity.set(rb.linearVelocity)
                rc.angularVelocity.set(rb.angularVelocity)
            }

        }

    }

    override fun entityAdded(entity: Entity) {

        val tc = entity.getComponent(TransformComponent::class.java)
        val rc = entity.getComponent(RigidBodyComponent::class.java)

        val (shape, mass) = if (entity.hasComponent(ColliderComponent::class.java)) {
            val collider = entity.getComponent(ColliderComponent::class.java)
            collider.convert() to rc.mass.toFloat()
        } else {
            btEmptyShape() to 1f
        }

        val motionState = btDefaultMotionState(Matrix4().copy(tc.getWorldMatrix(tmpMat)))
        rbMap[entity] = btRigidBody(mass, motionState, shape).also { rb ->
            rb.activationState = CollisionConstants.DISABLE_DEACTIVATION
            when (rc.motionControl) {
                MotionControl.DYNAMIC -> {
                    rb.linearFactor = Vector3(1f, 1f, 1f)
                    rb.angularFactor = Vector3(1f, 1f, 1f)
                }
                MotionControl.KINEMATIC -> {
                    rb.collisionFlags = rb.collisionFlags or btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT
                }
                MotionControl.STATIC -> {
                    rb.linearFactor = Vector3.Zero
                    rb.angularFactor = Vector3.Zero
                }
            }
            world.addRigidBody(rb)
        }
    }

    override fun entityRemoved(entity: Entity) {
        rbMap[entity]?.also {
            world.removeRigidBody(it)
        }
    }

    override fun close() {
        super.close()
        world.dispose()
    }

    private companion object {

        init {
            Bullet.init()
        }

    }

}
