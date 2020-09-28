package info.laht.krender.mesh

interface Shape

interface SphereShape : Shape {
    val radius: Float
}

interface PlaneShape : Shape {
    val width: Float
    val height: Float
}

interface BoxShape : Shape {
    val width: Float
    val height: Float
    val depth: Float
}

interface CircleShape : Shape {
    val radius: Float
}

interface CylinderShape : Shape {
    val radius: Float
    val height: Float
}

interface CapsuleShape : Shape {
    val radius: Float
    val height: Float
}

interface HeightmapShape : Shape {
    val width: Float
    val height: Float
    val widthSegments: Int
    val heightSegments: Int
    val heights: FloatArray
}

interface TrimeshShape : Shape {
    val indices: List<Int>
    val vertices: List<Float>
    val normals: List<Float>
    val uvs: List<Float>
}
