package no.ntnu.ihb.vico.render

internal fun FloatArray.setXY(index: Int, x: Float, y: Float) = apply {
    val i = index * 2

    this[i + 0] = x
    this[i + 1] = y
}

internal fun FloatArray.setXYZ(index: Int, x: Float, y: Float, z: Float) = apply {
    val i = index * 3

    this[i + 0] = x
    this[i + 1] = y
    this[i + 2] = z
}

internal fun <E> MutableList<E>.push(e1: E, e2: E) {
    add(e1)
    add(e2)
}

internal fun <E> MutableList<E>.push(e1: E, e2: E, e3: E) {
    add(e1)
    add(e2)
    add(e3)
}
