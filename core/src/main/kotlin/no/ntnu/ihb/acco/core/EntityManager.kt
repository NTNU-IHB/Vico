package no.ntnu.ihb.acco.core

import no.ntnu.ihb.acco.util.ObservableSet

class EntityManager {

    private val root = RootEntity()

    private val families: MutableMap<Family, ObservableSet<Entity>> = mutableMapOf()

    fun addEntity(entity: Entity) = root.addEntity(entity)
    fun addAllEntities(entity: Entity, vararg additionalEntities: Entity) =
        root.addAllEntities(entity, *additionalEntities)

    fun removeEntity(entity: Entity) = root.removeEntity(entity)
    fun removeAllEntities(entity: Entity, vararg additionalEntities: Entity) =
        root.removeAllEntities(entity, *additionalEntities)

    fun getEntitiesFor(family: Family): ObservableSet<Entity> {
        return families.computeIfAbsent(family) {
            ObservableSet(root.findAllInDescendants { family.test(it) }.toMutableSet())
        }
    }

    fun getEntityByName(name: String): Entity {
        return root.findInDescendants { it.name == name }
    }

    fun getEntitiesByTag(tag: String): List<Entity> {
        return root.findAllInDescendants { it.tag == tag }
    }

    inner class RootEntity : Entity("") {

        init {
            tag = "root"
        }

        override fun descendantAdded(entity: Entity) {
            updateFamilyMemberShip(entity)
        }

        override fun descendantRemoved(entity: Entity) {
            families.values.forEach { entities ->
                entities.remove(entity)
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

    }

}
