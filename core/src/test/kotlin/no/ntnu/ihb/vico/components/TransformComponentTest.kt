package no.ntnu.ihb.vico.components

import no.ntnu.ihb.vico.core.Entity
import org.joml.Vector3d
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class TransformComponentTest {

    @Test
    fun testProperty() {

        val e = Entity()
        val frame = e.addComponent<Transform>().frame
        assertEquals(Vector3d(), frame.getLocalTranslation())

        val write = DoubleArray(3) { i -> i.toDouble() }
        val localPosition = e.getRealProperty("localPosition")
        localPosition.write(DoubleArray(3) { i -> i.toDouble() })
        assertEquals(Vector3d().set(write), frame.getLocalTranslation())

        val read = DoubleArray(3)
        localPosition.read(read)
        assertArrayEquals(write, read)

        assertThrows<NoSuchElementException> {
            e.getRealProperty("fail")
        }

    }

}
