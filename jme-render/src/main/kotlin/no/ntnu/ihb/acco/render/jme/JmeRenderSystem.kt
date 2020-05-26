package no.ntnu.ihb.acco.render.jme

import com.jme3.asset.AssetManager
import com.jme3.scene.Node
import no.ntnu.ihb.acco.components.TransformComponent
import no.ntnu.ihb.acco.core.Entity
import no.ntnu.ihb.acco.core.Family
import no.ntnu.ihb.acco.core.IteratingSystem
import no.ntnu.ihb.acco.math.Quaternion
import no.ntnu.ihb.acco.math.Vector3
import no.ntnu.ihb.acco.render.GeometryComponent
import no.ntnu.ihb.acco.render.jme.objects.RenderNode


internal class JmeRenderSystem(
    private val root: Node,
    private val assetManager: AssetManager
) : IteratingSystem(
    Family.all(TransformComponent::class.java, GeometryComponent::class.java).build()
) {

    private val tmpVec = Vector3()
    private val tmpQuat = Quaternion()
    private val map: MutableMap<Entity, RenderNode> = mutableMapOf()

    override fun entityAdded(entity: Entity) {
        val geometry = entity.getComponent(GeometryComponent::class.java)
        map.computeIfAbsent(entity) {
            geometry.createGeometry(assetManager).also {
                root.attachChild(it)
            }
        }
    }

    override fun entityRemoved(entity: Entity) {
        root.detachChild(map.getValue(entity))
    }

    override fun processEntity(entity: Entity, currentTime: Double, stepSize: Double) {

        val node = map.getValue(entity)

        val transform = entity.getComponent(TransformComponent::class.java)

        node.localRotation.set(transform.getWorldQuaternion(tmpQuat))
        node.localTranslation.set(transform.getWorldPosition(tmpVec))
        node.forceRefresh(true, true, true)

        val geometry = entity.getComponent(GeometryComponent::class.java)
        node.setVisible(geometry.visible)
        node.setWireframe(geometry.wireframe)

    }
}
