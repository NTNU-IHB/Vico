package info.laht.acco.fmi.fmuproxy

import no.ntnu.ihb.fmuproxy.AbstractRpcFmuClient
import no.ntnu.ihb.fmuproxy.thrift.ThriftFmuClient
import java.io.File
import java.net.URL

class ProxyConnection(
    host: String,
    port: Int
) {

    private val client = ThriftFmuClient.socketClient(host, port)

    fun loadFile(file: File): AbstractRpcFmuClient {
        return client.load(file)
    }

    fun loadUrl(url: URL): AbstractRpcFmuClient {
        return client.load(url)
    }

    fun loadGuid(guid: String): AbstractRpcFmuClient {
        return client.load(guid)
    }

}
