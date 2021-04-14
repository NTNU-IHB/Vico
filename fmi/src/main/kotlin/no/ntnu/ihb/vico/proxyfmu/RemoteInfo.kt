package no.ntnu.ihb.vico.proxyfmu

data class RemoteInfo(
    val host: String,
    val port: Int? = null
) {

    val hasPort: Boolean = port != null
    val isLoopback: Boolean = (host == "localhost" || host == "127.0.0.1")

}
