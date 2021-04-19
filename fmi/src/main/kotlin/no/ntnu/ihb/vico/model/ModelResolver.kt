package no.ntnu.ihb.vico.model

import no.ntnu.ihb.vico.fmi.FmuResolver
import no.ntnu.ihb.vico.proxyfmu.ProxyFmuResolver
import java.io.File
import java.net.URI

interface ModelResolver {

    fun resolve(base: File, uri: URI): SlaveProvider?

    companion object {

        private val resolvers: MutableSet<ModelResolver> = mutableSetOf(
            FmuResolver(), ProxyFmuResolver()
        )

        @JvmStatic
        fun addResolver(resolver: ModelResolver): Boolean {
            return resolvers.add(resolver)
        }

        fun resolve(file: File): SlaveProvider {
            check(resolvers.isNotEmpty()) { "No model resolvers have been added!" }
            return resolve(
                file.absoluteFile.parentFile,
                file.toURI()
            )
        }

        fun resolve(base: File, uri: URI): SlaveProvider {
            check(resolvers.isNotEmpty()) { "No model resolvers have been added!" }
            return resolvers.mapNotNull { it.resolve(base, uri) }.firstOrNull()
                ?: throw Error("Unable to resolve component with URI: $uri")
        }

    }

}
