package no.ntnu.ihb.vico.core

class PropertyLocator(
    private val engine: Engine
) {

    fun getProperty(fullName: String): Property {
        val split = fullName.split(".")
        require(split.size >= 2)
        val entity = engine.getEntityByName(split[0])
        var component: Component? = null
        if (split.size > 2) {
            val componentName = split[1]
            component = entity.getOrNull(componentName)
        }
        val propertyName = split.drop(if (component == null) 1 else 2).joinToString(".")
        return component?.getProperty(propertyName) ?: entity.getProperty(propertyName)
    }

    fun getIntegerProperty(fullName: String): IntProperty {
        val split = fullName.split(".")
        require(split.size >= 2)
        val entity = engine.getEntityByName(split[0])
        var component: Component? = null
        if (split.size > 2) {
            val componentName = split[1]
            component = entity.getOrNull(componentName)
        }
        val propertyName = split.drop(if (component == null) 1 else 2).joinToString(".")
        return component?.getIntegerProperty(propertyName) ?: entity.getIntegerProperty(propertyName)
    }

    fun getRealProperty(fullName: String): RealProperty {
        val split = fullName.split(".")
        require(split.size >= 2)
        val entity = engine.getEntityByName(split[0])
        var component: Component? = null
        if (split.size > 2) {
            val componentName = split[1]
            component = entity.getOrNull(componentName)
        }
        val propertyName = split.drop(if (component == null) 1 else 2).joinToString(".")
        return component?.getRealProperty(propertyName) ?: entity.getRealProperty(propertyName)
    }

    fun getBooleanProperty(fullName: String): BoolProperty {
        val split = fullName.split(".")
        require(split.size >= 2)
        val entity = engine.getEntityByName(split[0])
        var component: Component? = null
        if (split.size > 2) {
            val componentName = split[1]
            component = entity.getOrNull(componentName)
        }
        val propertyName = split.drop(if (component == null) 1 else 2).joinToString(".")
        return component?.getBooleanProperty(propertyName) ?: entity.getBooleanProperty(propertyName)
    }

    fun getStringProperty(fullName: String): StrProperty {
        val split = fullName.split(".")
        require(split.size >= 2)
        val entity = engine.getEntityByName(split[0])
        var component: Component? = null
        if (split.size > 2) {
            val componentName = split[1]
            component = entity.getOrNull(componentName)
        }
        val propertyName = split.drop(if (component == null) 1 else 2).joinToString(".")
        return component?.getStringProperty(propertyName) ?: entity.getStringProperty(propertyName)
    }

}
