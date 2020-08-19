package no.ntnu.ihb.vico.util

import no.ntnu.ihb.vico.core.UnboundProperty
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

        val p = UnboundProperty.parse(joined)

        assertEquals(entityName, p.entityName)
        assertEquals(componentName, p.componentName)
        assertEquals(propertyName, p.propertyName)

    }

}
