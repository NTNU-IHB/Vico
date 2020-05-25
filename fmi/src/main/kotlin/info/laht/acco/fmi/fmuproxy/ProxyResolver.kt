package info.laht.acco.fmi.fmuproxy

import info.laht.acco.Model
import info.laht.acco.ModelResolver
import java.io.File
import java.net.URI
import java.net.URL

class ProxyResolver internal constructor() : ModelResolver {

    override fun resolve(base: File, uri: URI): Model? {

        if (uri.scheme != "fmu-proxy") return null

        val auth = parseAuthority(uri.authority)
        val connection = ProxyConnection(auth.first, auth.second)

        val query = uri.query
        return when {
            query.startsWith("guid=") -> {
                val guid = query.substring(5)
                connection.loadGuid(guid)
            }
            query.startsWith("file=") -> {
                val fileStr = query.substring(5)
                if (!fileStr.startsWith("file:///")) {
                    connection.loadFile(File(base, fileStr))
                } else {
                    connection.loadFile(File(fileStr))
                }
            }
            query.startsWith("url=") -> {
                val url = query.substring(4)
                connection.loadUrl(URL(url))
            }
            else -> throw RuntimeException()
        }.let { ProxyModel(it) }

    }

    private fun parseAuthority(auth: String): Pair<String, Int> {
        return auth.split(":").let {
            it[0] to it[1].toInt()
        }
    }

}
