package no.ntnu.ihb.vico.core

private class PropertyParser(
    name: String,
    engine: Engine
) {

    var entity: Entity
    var component: Component? = null
    var propertyName: String

    init {
        val split = name.split(".")
        require(split.size >= 2)
        entity = engine.getEntityByName(split[0])
        if (split.size > 2) {
            val componentName = split[1]
            component = entity.getOrNull(componentName)
        }
        propertyName = split.drop(if (component == null) 1 else 2).joinToString(".")
    }

}

class PropertyLocator(
    private val engine: Engine
) {

    fun getProperty(fullName: String): Property {
        return PropertyParser(fullName, engine).let {
            it.component?.getProperty(it.propertyName)
                ?: it.entity.getProperty(it.propertyName)
        }
    }

    fun getIntegerProperty(fullName: String): IntProperty {
        return PropertyParser(fullName, engine).let {
            it.component?.getIntegerProperty(it.propertyName)
                ?: it.entity.getIntegerProperty(it.propertyName)
        }
    }

    fun getRealProperty(fullName: String): RealProperty {
        return PropertyParser(fullName, engine).let {
            it.component?.getRealProperty(it.propertyName)
                ?: it.entity.getRealProperty(it.propertyName)
        }
    }

    fun getBooleanProperty(fullName: String): BoolProperty {
        return PropertyParser(fullName, engine).let {
            it.component?.getBooleanProperty(it.propertyName)
                ?: it.entity.getBooleanProperty(it.propertyName)
        }
    }

    fun getStringProperty(fullName: String): StrProperty {
        return PropertyParser(fullName, engine).let {
            it.component?.getStringProperty(it.propertyName)
                ?: it.entity.getStringProperty(it.propertyName)
        }
    }

}
