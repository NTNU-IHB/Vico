package no.ntnu.ihb.vico.fmi

import no.ntnu.ihb.vico.model.ModelResolver
import no.ntnu.ihb.vico.model.SlaveProvider
import java.io.File
import java.net.URI

class FmuResolver internal constructor() : ModelResolver {

    fun resolve(file: File): SlaveProvider? = resolve(file.parentFile, file.toURI())

    override fun resolve(base: File, uri: URI): SlaveProvider? {

        if (!uri.path.endsWith(".fmu")) return null
        try {
            return if (!uri.isAbsolute) {
                FmiSlaveProvider(File(uri))
            } else {
                FmiSlaveProvider(uri.toURL())
            }
        } catch (ex: Exception) {
            throw RuntimeException("Failed to resolve FMU: $uri")
        }

    }

}
