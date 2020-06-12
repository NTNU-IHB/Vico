package no.ntnu.ihb.acco.components

import no.ntnu.ihb.acco.core.Entity
import org.joml.Vector3d
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TransformComponentTest {

    @Test
    fun testProperty() {

        val e = Entity()
        val t = e.transform
        assertEquals(Vector3d(), t.getLocalTranslation())

        val write = DoubleArray(3) { i -> i.toDouble() }
        e.writeReal("localPosition", DoubleArray(3) { i -> i.toDouble() })
        assertEquals(Vector3d().set(write), t.getLocalTranslation())

        val read = DoubleArray(3)
        e.readReal("localPosition", read)
        assertArrayEquals(write, read)

    }

}
