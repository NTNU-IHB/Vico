package no.ntnu.ihb.vico.core

sealed class Connector {

    abstract val component: Component
    abstract val property: Property

    companion object {

        fun inferConnectorType(component: Component, property: Property): Connector {
            return when (property) {
                is IntProperty -> IntConnector(component, property)
                is RealProperty -> RealConnector(component, property)
                is StrProperty -> StrConnector(component, property)
                is BoolProperty -> BoolConnector(component, property)
            }
        }

    }

}

class IntConnector(
    override val component: Component,
    override val property: IntProperty
) : Connector() {
    constructor(component: Component, name: String) : this(
        component, component.getIntegerProperty(name)
    )
}

class RealConnector(
    override val component: Component,
    override val property: RealProperty,
    internal val modifier: RealModifier? = null
) : Connector() {
    constructor(component: Component, name: String, modifier: RealModifier? = null) : this(
        component, component.getRealProperty(name), modifier
    )
}

class StrConnector(
    override val component: Component,
    override val property: StrProperty
) : Connector() {
    constructor(component: Component, name: String) : this(
        component, component.getStringProperty(name)
    )
}

class BoolConnector(
    override val component: Component,
    override val property: BoolProperty
) : Connector() {
    constructor(component: Component, name: String) : this(
        component, component.getBooleanProperty(name)
    )
}
