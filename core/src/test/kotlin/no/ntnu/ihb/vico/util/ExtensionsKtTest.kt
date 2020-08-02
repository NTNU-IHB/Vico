package no.ntnu.ihb.vico.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ExtensionsKtTest {

    @Test
    fun insert() {

        val original = "Hello, pants!"
        val insert = "smarty"

        val modified = original.insert(original.indexOf(' '), insert)

        assertEquals("Hello, smartypants!", modified)

    }

    @Test
    fun extractEntityAndPropertyName() {
        val entityName = "entity"
        val propertyName = "propertyName"

        val joined = "$entityName.$propertyName"

        val (e, p) = joined.extractEntityAndPropertyName()

        assertEquals(entityName, e)
        assertEquals(propertyName, p)

    }

}
