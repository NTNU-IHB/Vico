package no.ntnu.ihb.vico.ssp

/*class Component(
    private val model: Model,
    val instanceName: String,
    val stepSizeHint: Double? = null
) {

    val modelDescription
        get() = model.modelDescription

    private val connectors_ = mutableSetOf<Connector>()
    val connectors: Set<Connector> = connectors_

    private val parameterSets_ = linkedMapOf<String, ParameterSet>()
    val parameterSets: LinkedHashMap<String, ParameterSet> = parameterSets_

    private val instantiated = AtomicBoolean(false)

    fun getConnector(name: String): Connector {
        return connectors.find { it.name == name }
            ?: throw IllegalArgumentException("No connector named '$name' in component '${model.modelDescription.modelName}'!")
    }

    fun addConnector(connector: Connector) {
        connector.validate(modelDescription)
        check(connectors_.add(connector)) { "Connector '${connector.name}' has already been added to component '${model.modelDescription.modelName}'!" }
    }

    fun addParameterSet(name: String, parameterSet: ParameterSet) = apply {
        check(name !in parameterSets_) { "ParameterSet '$name' has already been added to component '${model.modelDescription.modelName}'!" }
        parameterSets_[name] = parameterSet
    }

    fun instantiate(): SlaveInstance {
        check(!instantiated.getAndSet(true))
        return model.instantiate(instanceName)
    }

    private fun getValueRefs(params: List<Parameter<*>>): LongArray {
        return params.map { param -> modelDescription.getVariableByName(param.name).valueReference }.toLongArray()
    }

    internal fun applyParameterSet(slave: VariableAccessor, parameterSetName: String?) {
        if (parameterSets.isNotEmpty()) {
            val parameterSet = parameterSetName?.let { parameterSets[parameterSetName] } ?: parameterSets.values.first()
            with(parameterSet) {
                LOG.info("Applying parameters from ParameterSet '$parameterSetName' to slave '${instanceName}':")
                integerParameters.also { params ->
                    val vr = getValueRefs(params)
                    val values = params.map { param -> param.value }.toIntArray()
                    params.forEachIndexed { i, param ->
                        LOG.info("\tSetting ${param.name}=${values[i]}")
                    }
                    slave.writeInteger(vr, values)
                }
                realParameters.also { params ->
                    val vr = getValueRefs(params)
                    val values = params.map { param -> param.value }.toDoubleArray()
                    params.forEachIndexed { i, param ->
                        LOG.info("\tSetting ${param.name}=${values[i]}")
                    }
                    slave.writeReal(vr, values)
                }
                booleanParameters.also { params ->
                    val vr = getValueRefs(params)
                    val values = params.map { param -> param.value }.toBooleanArray()
                    params.forEachIndexed { i, param ->
                        LOG.info("\tSetting ${param.name}=${values[i]}")
                    }
                    slave.writeBoolean(vr, values)
                }
                stringParameters.also { params ->
                    val vr = getValueRefs(params)
                    val values = params.map { param -> param.value }.toTypedArray()
                    params.forEachIndexed { i, param ->
                        LOG.info("\tSetting ${param.name}=${values[i]}")
                    }
                    slave.writeString(vr, values)
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Component

        if (instanceName != other.instanceName) return false

        return true
    }

    override fun hashCode(): Int {
        return instanceName.hashCode()
    }

    override fun toString(): String {
        return "Component(modelName=${model.modelDescription.modelName}, instanceName='$instanceName', stepSizeHint=${stepSizeHint
            ?: "None"})"
    }

    private companion object {
        val LOG: Logger = LoggerFactory.getLogger(Component::class.java)
    }

}*/
