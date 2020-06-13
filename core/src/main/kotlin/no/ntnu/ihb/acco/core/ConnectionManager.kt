package no.ntnu.ihb.acco.core

class ConnectionManager {

    private val connections: MutableMap<Component, MutableList<Connection>> = mutableMapOf()

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

    private inline fun forEachConnection(f: (Connection) -> Unit) {
        connections.values.forEach { list ->
            list.forEach {
                f.invoke(it)
            }
        }
    }

}
