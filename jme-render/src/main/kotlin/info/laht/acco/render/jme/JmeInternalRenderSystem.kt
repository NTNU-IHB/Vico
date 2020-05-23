package info.laht.acco.render.jme

import com.jme3.asset.AssetManager
import com.jme3.scene.Node
import info.laht.acco.components.TransformComponent
import info.laht.acco.core.Entity
import info.laht.acco.core.Family
import info.laht.acco.core.IteratingSystem
import info.laht.acco.math.Quaternion
import info.laht.acco.math.Vector3
import info.laht.acco.render.GeometryComponent
import info.laht.acco.render.jme.objects.RenderNode


internal class JmeInternalRenderSystem(
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
        val transform = entity.getComponent(TransformComponent::class.java)
        val node = map.computeIfAbsent(entity) {
            geometry.createGeometry(assetManager).also {
                root.attachChild(it)
            }
        }
        node.localRotation.set(transform.getWorldQuaternion(tmpQuat))
        node.localTranslation.set(transform.getWorldPosition(tmpVec))
        node.forceRefresh(true, true, true)
    }

    override fun entityRemoved(entity: Entity) {
        root.detachChild(map.getValue(entity))
    }

    override fun processEntity(entity: Entity, currentTime: Double, stepSize: Double) {

        val node = map.getValue(entity)

        val transform = entity.getComponent(TransformComponent::class.java)

        node.localRotation.set(transform.quaternion)
        node.localTranslation.set(transform.getWorldPosition(tmpVec))
        node.forceRefresh(true, true, true)

        val geometry = entity.getComponent(GeometryComponent::class.java)
        node.setVisible(geometry.visible)
        node.setWireframe(geometry.wireframe)

    }
}
