package no.ntnu.ihb.acco.core


typealias ComponentClass = Class<out Component>

abstract class Component : Properties()

internal fun <E : Component> instantiate(componentClass: Class<out E>): E {
    val ctor = componentClass.getDeclaredConstructor()
    return ctor.newInstance()
}

