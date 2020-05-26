package no.ntnu.ihb.acco.core

interface ComponentListener {

    fun componentAdded(component: Component)
    fun componentRemoved(component: Component)

}

abstract class ComponentAdapter : ComponentListener {

    override fun componentAdded(component: Component) {}

    override fun componentRemoved(component: Component) {}

}
