package info.laht.acco.core

interface EntityListener {

    fun entityAdded(entity: Entity)
    fun entityRemoved(entity: Entity)

}

abstract class EntityAdapter : EntityListener {

    override fun entityAdded(entity: Entity) {}

    override fun entityRemoved(entity: Entity) {}

}
