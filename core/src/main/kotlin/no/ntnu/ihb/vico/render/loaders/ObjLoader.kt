package info.laht.krender.loaders

import info.laht.krender.mesh.Trimesh
import java.io.File
import java.util.regex.Pattern


class ObjLoader {

    val supportedExtension: String
        get() = "obj"

    fun load(source: File): Trimesh {
        //MeshLoader.testExtension(supportedExtension, source.extension)
        val parse = load(source.readText())
        return parse
    }

    fun load(text: String): Trimesh {

        val indices = mutableListOf<Int>()
        val vertices = mutableListOf<Float>()
        val normals = mutableListOf<Float>()

        run {
            val compile = Pattern.compile(VERTEX_PATTERN)
            val matcher = compile.matcher(text)
            while (matcher.find()) {
                val result = matcher.group()
                    .replace("  ", " ")
                    .split(" ").toTypedArray()
                vertices.add(result[1].toFloat())
                vertices.add(result[2].toFloat())
                vertices.add(result[3].toFloat())
            }
        }
        run {
            val compile = Pattern.compile(NORMAL_PATTERN)
            val matcher = compile.matcher(text)
            while (matcher.find()) {
                val result = matcher.group()
                    .replace("  ", " ")
                    .split(" ").toTypedArray()
                normals.add(result[1].toFloat())
                normals.add(result[2].toFloat())
                normals.add(result[3].toFloat())
            }
        }
        run {
            val compile = Pattern.compile(FACE_PATTERN1)
            val matcher = compile.matcher(text)
            while (matcher.find()) {
                val result = matcher.group().split(" ").toTypedArray()
                indices.add(result[1].toInt() - 1)
                indices.add(result[2].toInt() - 1)
                indices.add(result[3].toInt() - 1)
            }
        }
        run {
            val compile = Pattern.compile(FACE_PATTERN2)
            val matcher = compile.matcher(text)
            while (matcher.find()) {
                val result = matcher.group().split(" ").toTypedArray()
                indices.add(result[1].split("/").toTypedArray()[0].toInt() - 1)
                indices.add(result[2].split("/").toTypedArray()[0].toInt() - 1)
                indices.add(result[3].split("/").toTypedArray()[0].toInt() - 1)
            }
        }
        run {
            val compile = Pattern.compile(FACE_PATTERN3)
            val matcher = compile.matcher(text)
            while (matcher.find()) {
                val result = matcher.group().split(" ").toTypedArray()
                indices.add(result[1].split("/").toTypedArray()[0].toInt() - 1)
                indices.add(result[2].split("/").toTypedArray()[0].toInt() - 1)
                indices.add(result[3].split("/").toTypedArray()[0].toInt() - 1)
            }
        }
        run {
            val compile = Pattern.compile(FACE_PATTERN4)
            val matcher = compile.matcher(text)
            while (matcher.find()) {
                val result = matcher.group().split(" ").toTypedArray()
                indices.add(result[1].split("//").toTypedArray()[0].toInt() - 1)
                indices.add(result[2].split("//").toTypedArray()[0].toInt() - 1)
                indices.add(result[3].split("//").toTypedArray()[0].toInt() - 1)
            }
        }
        return Trimesh(
            indices = indices,
            vertices = vertices,
            normals = normals
        )
    }

    companion object {

        private const val VERTEX_PATTERN =
            "v( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)"
        private const val NORMAL_PATTERN =
            "vn( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)"

        // f vertex vertex vertex ...
        private const val FACE_PATTERN1 = "f( +-?\\d+)( +-?\\d+)( +-?\\d+)( +-?\\d+)?"

        // f vertex/uv vertex/uv vertex/uv ...
        private const val FACE_PATTERN2 =
            "f( +(-?\\d+)\\/(-?\\d+))( +(-?\\d+)\\/(-?\\d+))( +(-?\\d+)\\/(-?\\d+))( +(-?\\d+)\\/(-?\\d+))?"

        // f vertex/uv/normal vertex/uv/normal vertex/uv/normal ...
        private const val FACE_PATTERN3 =
            "f( +(-?\\d+)\\/(-?\\d+)\\/(-?\\d+))( +(-?\\d+)\\/(-?\\d+)\\/(-?\\d+))( +(-?\\d+)\\/(-?\\d+)\\/(-?\\d+))( +(-?\\d+)\\/(-?\\d+)\\/(-?\\d+))?"

        // f vertex//normal vertex//normal vertex//normal ...
        private const val FACE_PATTERN4 =
            "f( +(-?\\d+)\\/\\/(-?\\d+))( +(-?\\d+)\\/\\/(-?\\d+))( +(-?\\d+)\\/\\/(-?\\d+))( +(-?\\d+)\\/\\/(-?\\d+))?"
    }

}
