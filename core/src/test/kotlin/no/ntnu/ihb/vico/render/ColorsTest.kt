package no.ntnu.ihb.vico.render

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ColorConstantsTest() {

    @Test
    fun testGetByName() {
        Assertions.assertEquals(ColorConstants.aliceblue, ColorConstants.getByName("aliceblue"))
    }

    @Test
    fun testGetByNameThrows() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            ColorConstants.getByName("fail")
        }
    }

}
