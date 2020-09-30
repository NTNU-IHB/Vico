package no.ntnu.ihb.vico.render.util

import java.io.File
import java.net.URL

interface InputSource {

    val extension: String

    fun readBytes(): ByteArray
    fun readText(): String

}

abstract class ExternalSource : InputSource {

    abstract val fullPath: String

    val location: String
        get() = fullPath.replace(filename, "")

    val filename: String
        get() {
            val index = fullPath.lastIndexOf(File.separatorChar)
            return fullPath.substring(index + 1)
        }

    val basename: String
        get() {
            val i1 = filename.lastIndexOf(File.separatorChar)
            val i2 = filename.lastIndexOf('.')
            return fullPath.substring(i1 + 1, i2)
        }

}

class FileSource(
    val file: File
) : ExternalSource() {

    override val extension: String
        get() = file.extension

    override val fullPath: String
        get() = file.absolutePath

    override fun readBytes(): ByteArray {
        return file.readBytes()
    }

    override fun readText(): String {
        return file.readText()
    }

    override fun toString(): String {
        return "FileSource(file=$file)"
    }

}

class URLSource(
    val url: URL
) : ExternalSource() {

    override val extension: String
        get() {
            val path = fullPath
            return path.substring(path.lastIndexOf(".") + 1)
        }

    override val fullPath: String
        get() = url.toExternalForm()

    override fun readBytes(): ByteArray {
        return url.openStream().buffered().use { it.readBytes() }
    }

    override fun readText(): String {
        return url.openStream().bufferedReader().use { it.readText() }
    }

    override fun toString(): String {
        return "URLSource(url=$url)"
    }

}

class StringSource(
    private val string: String,
    override val extension: String
) : InputSource {

    override fun readBytes(): ByteArray {
        throw UnsupportedOperationException()
    }

    override fun readText(): String {
        return string
    }

}
