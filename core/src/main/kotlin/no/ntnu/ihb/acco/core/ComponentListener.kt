package no.ntnu.ihb.acco.core

interface ComponentListener {

    fun onComponentAdded(entity: Entity, component: Component)

    fun onComponentRemoved(entity: Entity, component: Component)

}
