package no.ntnu.ihb.acco.components

import org.joml.Vector3d
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TransformComponentTest {

    @Test
    fun testProperty() {

        val t = TransformComponent()
        assertEquals(Vector3d(), t.getLocalTranslation())

        val write = DoubleArray(3) { i -> i.toDouble() }
        t.writeReal("localPosition", DoubleArray(3) { i -> i.toDouble() })
        assertEquals(Vector3d().set(write), t.getLocalTranslation())

        val read = DoubleArray(3)
        t.readReal("localPosition", read)
        assertArrayEquals(write, read)

    }

}
