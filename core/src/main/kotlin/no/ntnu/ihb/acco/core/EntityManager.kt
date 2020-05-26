package no.ntnu.ihb.acco.core


class EntityManager internal constructor(
    private val ctx: EngineContext
) : Iterable<Entity> {

    private val entities = mutableListOf<Entity>()
    private val entityListeners = mutableListOf<EntityListener>()
    private val families = mutableMapOf<Family, MutableSet<Entity>>()

    fun createEntity(name: String): Entity {
        return Entity(name)
    }

    fun getEntityByName(name: String): Entity {
        return entities.find { it.name == name }
            ?: throw IllegalArgumentException("No entities named '$name' present!")
    }

    fun getEntityByTag(tag: String): List<Entity> {
        return entities.filter { it.tag == tag }
    }

    fun getEntitiesFor(family: Family): Set<Entity> {
        return families.computeIfAbsent(family) { entities.filter { family.test(it) }.toMutableSet() }
    }

    fun addEntity(entity: Entity, vararg entities: Entity) {
        ctx.safeContext {
            internalAddEntity(entity)
            entities.forEach { internalAddEntity(it) }
        }
    }

    private fun internalAddEntity(entity: Entity) {
        entities.add(entity)
        updateFamilyMemberShip(entity)
        entity.addComponentListener(object : ComponentListener {
            override fun componentAdded(component: Component) {
                updateFamilyMemberShip(entity)
            }

            override fun componentRemoved(component: Component) {
                updateFamilyMemberShip(entity)
            }
        })
        entityListeners.forEach { l ->
            l.entityAdded(entity)
        }
    }

    fun removeEntity(entity: Entity, vararg entities: Entity) {
        ctx.safeContext {
            internalRemoveEntity(entity)
            entities.forEach {
                internalRemoveEntity(it)
            }
        }
    }

    private fun internalRemoveEntity(entity: Entity) {
        if (entities.remove(entity)) {
            entity.reset()
            families.values.forEach { entities ->
                entities.remove(entity)
            }
            entityListeners.forEach { l ->
                l.entityRemoved(entity)
            }
        }
    }

    /*fun add(entityListener: EntityListener, family: Family = Family.empty) {
        ctx.safeContext {
            entityListeners.add(entityListener)
            families.putIfAbsent(family, mutableListOf())
        }
    }

    fun remove(entityListener: EntityListener) {
        ctx.safeContext { entityListeners.remove(entityListener) }
    }*/


    private fun updateFamilyMemberShip(entity: Entity) {
        families.forEach { (family, entities) ->
            if (family.test(entity)) {
                entities.add(entity)
            } else {
                entities.remove(entity)
            }
        }
    }

    private fun ensureUnique(name: String): String {
        var i = 1
        var uniqueName = name
        while (entities.find { it.name == uniqueName } != null) {
            uniqueName += "(${i++})"
        }
        return uniqueName
    }


    override fun iterator(): Iterator<Entity> {
        return entities.iterator()
    }

}
