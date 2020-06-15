package no.ntnu.ihb.acco.core

import no.ntnu.ihb.acco.util.ObservableSet
import no.ntnu.ihb.acco.util.Tag
import no.ntnu.ihb.acco.util.toTag

class EntityManager {

    val root = RootEntity()

    private val families: MutableMap<Family, ObservableSet<Entity>> = mutableMapOf()

    fun addEntity(entity: Entity) = root.addEntity(entity)
    fun addAllEntities(entity: Entity, vararg additionalEntities: Entity) =
        root.addAllEntities(entity, *additionalEntities)

    fun removeEntity(entity: Entity) = root.removeEntity(entity)
    fun removeAllEntities(entity: Entity, vararg additionalEntities: Entity) =
        root.removeAllEntities(entity, *additionalEntities)

    fun getEntitiesFor(family: Family): ObservableSet<Entity> {
        return families.computeIfAbsent(family) {
            root.findAllInDescendants { family.test(it) }
            ObservableSet(root.findAllInDescendants { family.test(it) }.toMutableSet())
        }
    }

    fun getEntityByName(name: String): Entity {
        return root.findInDescendants { it.name == name }
    }

    fun getEntitiesByTag(tag: Tag): List<Entity> {
        return root.findAllInDescendants { it.tag == tag }
    }

    inner class RootEntity : Entity("") {

        init {
            tag = "root".toTag()
        }

        override fun descendantAdded(entity: Entity) {
            super.descendantAdded(entity)
            updateFamilyMemberShip(entity)
        }

        override fun descendantRemoved(entity: Entity) {
            super.descendantRemoved(entity)
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
