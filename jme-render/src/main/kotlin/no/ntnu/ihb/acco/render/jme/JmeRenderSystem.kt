package no.ntnu.ihb.acco.render.jme

import com.jme3.asset.AssetManager
import com.jme3.scene.Node
import no.ntnu.ihb.acco.components.TransformComponent
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.System
import no.ntnu.ihb.acco.render.GeometryComponent
import no.ntnu.ihb.acco.render.jme.objects.RenderNode
import org.joml.Matrix4d
import org.joml.Quaterniond
import org.joml.Vector3d


internal class JmeRenderSystem(
    private val root: Node,
    private val assetManager: AssetManager
) : System(
    Family.all(TransformComponent::class.java, GeometryComponent::class.java).build(), 1, Int.MAX_VALUE
) {

    private val tmpVec = Vector3d()
    private val tmpQuat = Quaterniond()
    private val tmpMatrix = Matrix4d()
    private val map: MutableMap<Entity, RenderNode> = mutableMapOf()

    override fun entityAdded(entity: Entity) {
        map.computeIfAbsent(entity) {

            val transform = entity.getComponent(TransformComponent::class.java)
            val geometry = entity.getComponent(GeometryComponent::class.java)
            geometry.createGeometry(assetManager).also { node ->

                val world = transform.getWorldMatrix(tmpMatrix)
                node.localTranslation.set(world.getTranslation(tmpVec))
                node.localRotation.set(world.getNormalizedRotation(tmpQuat))
                node.forceRefresh(true, true, true)
                node.setLocalTranslation(transform.getTranslation(tmpVec))

                root.attachChild(node)
            }
        }
    }

    override fun entityRemoved(entity: Entity) {
        root.detachChild(map.getValue(entity))
    }

    override fun step(currentTime: Double, stepSize: Double) {
        entities.forEach { entity ->

            val node = map.getValue(entity)
            val transform = entity.getComponent(TransformComponent::class.java)

            val world = transform.getWorldMatrix(tmpMatrix)
            node.localTranslation.set(world.getTranslation(tmpVec))
            node.localRotation.set(world.getNormalizedRotation(tmpQuat))
            node.forceRefresh(true, true, true)
            node.setLocalTranslation(transform.getTranslation(tmpVec))

            val geometry = entity.getComponent(GeometryComponent::class.java)
            node.setVisible(geometry.visible)
            node.setWireframe(geometry.wireframe)

        }
    }

}
