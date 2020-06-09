package no.ntnu.ihb.acco.core

class ConnectionManager(
    engine: Engine
) : EventListener {

    private val connections: MutableMap<Component, MutableList<Connection>> = mutableMapOf()

    init {
        engine.addEventListener(Connection.CONNECTION_NEEDS_UPDATE, this)
    }

    fun update() {
        forEachConnection { c ->
            c.transferData()
        }
    }

    fun updateConnection(key: Component) {
        connections[key]?.forEach { it.transferData() }
    }

    fun addConnection(connection: Connection) {
        connections.computeIfAbsent(connection.source) {
            mutableListOf()
        }.add(connection)
    }

    override fun invoke(evt: Event) {
        when (evt.type) {
            Connection.CONNECTION_NEEDS_UPDATE -> {
                val key = evt.target<Component>()
                connections[key]?.forEach { it.transferData() }
            }
        }
    }

    private inline fun forEachConnection(f: (Connection) -> Unit) {
        connections.values.forEach { list ->
            list.forEach {
                f.invoke(it)
            }
        }
    }

}
