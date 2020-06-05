package no.ntnu.ihb.acco.core

class ConnectionManager : EventSystem(Family.all) {

    private val connections: MutableMap<Component, MutableList<Connection<*, *>>> = mutableMapOf()

    init {
        listen(Connection.CONNECTION_NEEDS_UPDATE)
    }

    override fun init(currentTime: Double) {
        connections.values.flatten().forEach { it.transferData() }
    }

    fun add(connection: Connection<*, *>) {
        connections.computeIfAbsent(connection.source.component) {
            mutableListOf()
        }.add(connection)
    }

    override fun eventReceived(evt: Event) {
        when (evt.type) {
            Connection.CONNECTION_NEEDS_UPDATE -> {
                val key = evt.target<Component>()
                connections[key]?.forEach { it.transferData() }
            }
        }
    }

}
