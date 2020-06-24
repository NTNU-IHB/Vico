package no.ntnu.ihb.acco.core


class EntityManager internal constructor(
    private val connectionManager: ConnectionManager
) {

    private val root = RootEntity()
    private val families: MutableMap<Family, MutableSet<Entity>> = mutableMapOf()
    private val entityListeners: MutableList<EntityListener> = mutableListOf()

    fun addEntity(entity: Entity) = root.addEntity(entity)
    fun addAllEntities(entity: Entity, vararg additionalEntities: Entity) =
        root.addAllEntities(entity, *additionalEntities)

    fun removeEntity(entity: Entity) = root.removeEntity(entity)
    fun removeAllEntities(entity: Entity, vararg additionalEntities: Entity) =
        root.removeAllEntities(entity, *additionalEntities)

    fun getEntitiesFor(family: Family): Set<Entity> {
        return families.computeIfAbsent(family) {
            root.findAllInDescendants { family.test(it) }.toMutableSet()
        }
    }

    fun getEntityByName(name: String, hierarchical: Boolean = true): Entity {
        return root.findInDescendants {
            if (hierarchical) {
                it.name == name
            } else {
                it.originalName == name
            }
        }
    }

    fun getEntitiesByTag(tag: String): List<Entity> {
        return root.findAllInDescendants { it.tag == tag }
    }

    fun addEntityListener(entityListener: EntityListener) {
        entityListeners.add(entityListener)
    }

    fun removeEntityListener(entityListener: EntityListener) {
        entityListeners.remove(entityListener)
    }

    inner class RootEntity : Entity(""), ComponentListener {

        init {
            tag = "root"
        }

        override fun descendantAdded(entity: Entity) {
            super.descendantAdded(entity)
            entity.addComponentListener(this)
            updateFamilyMemberShip(entity)
        }

        override fun descendantRemoved(entity: Entity) {
            super.descendantRemoved(entity)
            entity.removeComponentListener(this)

            families.forEach { (family, entities) ->
                if (entities.remove(entity)) {
                    entityListeners.forEach {
                        if (it.family == family) {
                            it.entityRemoved(entity)
                        }
                    }
                }
            }

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

        override fun onComponentAdded(entity: Entity, component: Component) {
            updateFamilyMemberShip(entity)
        }

        override fun onComponentRemoved(entity: Entity, component: Component) {
            updateFamilyMemberShip(entity)
            connectionManager.onComponentRemoved(component)
        }

    }

}
