package no.ntnu.ihb.vico.util

import no.ntnu.ihb.vico.core.PropertyIdentifier
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
        val componentName = "component"
        val propertyName = "some.property"

        val joined = "$entityName.$componentName.$propertyName"

        val (e, c, p) = PropertyIdentifier.parse(joined)

        assertEquals(entityName, e)
        assertEquals(componentName, c)
        assertEquals(propertyName, p)

    }

}
