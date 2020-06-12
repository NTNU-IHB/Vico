package no.ntnu.ihb.acco.core

import no.ntnu.ihb.acco.util.ObservableSet


class EntityManager internal constructor(
    private val engine: Engine
) : Iterable<Entity> {

    private val entities: MutableList<Entity> = mutableListOf()
    private val entityListeners: MutableList<EntityListener> = mutableListOf()
    private val families: MutableMap<Family, ObservableSet<Entity>> = mutableMapOf()

    fun getEntityByName(name: String): Entity {
        return entities.find { it.name == name }
            ?: throw IllegalArgumentException("No entities named '$name' present!")
    }

    fun getEntityByTag(tag: Tag): List<Entity> {
        return entities.filter { it.tag == tag }
    }

    fun getEntitiesFor(family: Family): ObservableSet<Entity> {
        return families.computeIfAbsent(family) { ObservableSet(entities.filter { family.test(it) }.toMutableSet()) }
    }

    fun addEntity(entity: Entity, vararg entities: Entity) {
        engine.safeContext {
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
        engine.safeContext {
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

    private fun updateFamilyMemberShip(entity: Entity) {
        families.forEach { (family, entities) ->
            if (family.test(entity)) {
                entities.add(entity)
            } else {
                entities.remove(entity)
            }
        }
    }

    /* private fun ensureUnique(name: String): String {
         var i = 1
         var uniqueName = name
         while (entities.find { it.name == uniqueName } != null) {
             uniqueName += "(${i++})"
         }
         return uniqueName
     }*/


    override fun iterator(): Iterator<Entity> {
        return entities.iterator()
    }

}
