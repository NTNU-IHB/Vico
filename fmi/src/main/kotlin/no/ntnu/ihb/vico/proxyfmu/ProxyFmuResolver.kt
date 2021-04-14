package no.ntnu.ihb.vico.proxyfmu

import no.ntnu.ihb.vico.model.ModelResolver
import no.ntnu.ihb.vico.model.SlaveProvider
import java.io.File
import java.net.URI

class ProxyFmuResolver internal constructor() : ModelResolver {

    override fun resolve(base: File, uri: URI): SlaveProvider? {

        if (uri.scheme != "proxyfmu") return null

        val query = uri.query
        val auth = parseAuthority(uri.authority)
        return when {
            query.startsWith("file=") -> {
                val fileStr = query.substring(5)
                if (!fileStr.startsWith("file:///")) {
                    File(base, fileStr)
                } else {
                    File(fileStr)
                }
            }
            query.startsWith("url=") -> {
                val url = query.substring(4)
                //connection.loadUrl(URL(url))
                TODO()
            }
            else -> throw RuntimeException()
        }.let { ProxySlaveProvider(auth, it) }

    }

    private fun parseAuthority(auth: String): RemoteInfo {

        val colonIdx = auth.indexOf(":")
        return if (colonIdx == -1) {
            return RemoteInfo(auth)
        } else {
            auth.split(":").let {
                check(it.size == 2)
                RemoteInfo(it[0], it[1].toInt())
            }
        }

    }

}
