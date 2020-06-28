package no.ntnu.ihb.acco.core

interface EntityListener {

    val family: Family

    fun entityAdded(entity: Entity)

    fun entityRemoved(entity: Entity)

}
