package no.ntnu.ihb.acco.core

private const val DEFAULT_ENTITY_NAME = "Entity"

interface EntityListener {
    val family: Family
    fun entityAdded(entity: Entity)
    fun entityRemoved(entity: Entity)
}

interface EntityAccess {
    fun createEntity(vararg components: Component): Entity
    fun createEntity(name: String, vararg components: Component): Entity
    fun removeEntity(entity: Entity): Boolean
    fun getEntitiesFor(family: Family): Set<Entity>
    fun getEntityByName(name: String): Entity
    fun getEntityByNameOrNull(name: String): Entity?
    fun getEntitiesByTag(tag: String): List<Entity>
    fun addEntityListener(entityListener: EntityListener)
    fun removeEntityListener(entityListener: EntityListener)
}

class EntityManager internal constructor(
    private val connectionManager: ConnectionManager
) : EntityAccess {

    private val componentListener = MyComponentListener()
    private val entities: MutableSet<Entity> = mutableSetOf()
    private val families: MutableMap<Family, MutableSet<Entity>> = mutableMapOf()
    private val entityListeners: MutableList<EntityListener> = mutableListOf()

    override fun createEntity(vararg components: Component): Entity {
        return createEntity(DEFAULT_ENTITY_NAME, *components)
    }

    override fun createEntity(name: String, vararg components: Component): Entity {
        val entity = Entity(name.ensureUnique()).also {
            components.forEach { c ->
                it.addComponent(c)
            }
        }
        updateFamilyMemberShip(entity)
        entity.addComponentListener(componentListener)
        entities.add(entity)
        return entity
    }

    override fun removeEntity(entity: Entity) = entities.remove(entity).also { wasRemoved ->
        if (wasRemoved) {
            families.forEach { (family, entities) ->
                if (entities.remove(entity)) {
                    entityListeners.forEach {
                        if (it.family == family) {
                            it.entityRemoved(entity)
                        }
                    }
                }
            }
            entity.components.forEach { connectionManager.onComponentRemoved(it) }
            entity.removeComponentListener(componentListener)
        }
    }

    override fun getEntitiesFor(family: Family): Set<Entity> {
        return families.computeIfAbsent(family) {
            entities.filter { family.test(it) }.toMutableSet()
        }
    }

    override fun getEntityByName(name: String): Entity {
        return getEntityByNameOrNull(name)
            ?: throw NoSuchElementException("No entity named '$name' exists!")
    }

    override fun getEntityByNameOrNull(name: String): Entity? {
        return entities.firstOrNull { it.name == name }
    }

    override fun getEntitiesByTag(tag: String): List<Entity> {
        return entities.filter { it.tag == tag }
    }

    override fun addEntityListener(entityListener: EntityListener) {
        entityListeners.add(entityListener)
    }

    override fun removeEntityListener(entityListener: EntityListener) {
        entityListeners.remove(entityListener)
    }

    private fun updateFamilyMemberShip(entity: Entity) {
        families.forEach { (family, entities) ->
            if (family.test(entity)) {
                if (entities.add(entity)) {
                    entityListeners.forEach {
                        if (it.family == family) {
                            it.entityAdded(entity)
                        }
                    }
                }
            } else {
                if (entities.remove(entity)) {
                    entityListeners.forEach {
                        if (it.family == family) {
                            it.entityRemoved(entity)
                        }
                    }
                }
            }
        }
    }

    @Synchronized
    private fun String.ensureUnique(): String {
        var i = 1
        var uniqueName = this
        while (getEntityByNameOrNull(uniqueName) != null) {
            uniqueName += "(${i++})"
        }
        return uniqueName
    }

    private inner class MyComponentListener : ComponentListener {
        override fun onComponentAdded(entity: Entity, component: Component) {
            updateFamilyMemberShip(entity)
        }

        override fun onComponentRemoved(entity: Entity, component: Component) {
            connectionManager.onComponentRemoved(component)
            updateFamilyMemberShip(entity)
        }
    }

}
