package info.laht.acco.fmi

import info.laht.acco.Model
import info.laht.acco.ModelResolver
import java.io.File
import java.net.URI

class FmuResolver internal constructor() : ModelResolver {

    fun resolve(file: File): Model? = resolve(file.parentFile, file.toURI())

    override fun resolve(base: File, uri: URI): Model? {

        if (!uri.path.endsWith(".fmu")) return null

        return if (!uri.isAbsolute) {
            FmiModel(File(uri))
        } else {
            FmiModel(uri.toURL())
        }

    }

}
