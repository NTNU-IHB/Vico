package info.laht.acco

import java.io.File
import java.net.URI

interface ModelResolver {

    fun resolve(base: File, uri: URI): Model?

    companion object {

        private val resolvers = mutableSetOf<ModelResolver>()

        init {

            /*listOf(
                "no.ntnu.ihb.vico.fmi.FmuResolver",
                "info.laht.acco.fmi.fmuproxy.ProxyResolver"
            ).forEach { className ->
                try {
                    Class.forName(className).also {
                        resolvers.add(it.getDeclaredConstructor().newInstance() as ModelResolver)
                    }
                } catch (ex: ClassNotFoundException) {
                    // ignore
                }
            }*/

        }

        @JvmStatic
        fun addResolver(resolver: ModelResolver): Boolean {
            return resolvers.add(resolver)
        }

        fun resolve(file: File): Model {
            check(resolvers.isNotEmpty()) { "No model resolvers have been added!" }
            return resolve(file.absoluteFile.parentFile, file.toURI())
        }

        fun resolve(base: File, uri: URI): Model {
            check(resolvers.isNotEmpty()) { "No model resolvers have been added!" }
            return resolvers.mapNotNull { it.resolve(base, uri) }.firstOrNull()
                ?: throw Error("Unable to resolve component with URI: $uri")
        }

    }

}
