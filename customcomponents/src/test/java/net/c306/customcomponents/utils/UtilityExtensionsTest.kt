package net.c306.customcomponents.utils

import org.junit.Assert.assertEquals
import org.junit.Test

internal class UtilityExtensionsTest {
    
    private val allSmallText = "this text is all small"
    private val allSmallTextFirstUpper = "This text is all small"
    private val nonFirstCapText = "tHIS tEXT iS nOT cAPS fIRST"
    private val nonFirstCapTextFirstUpper = "THIS tEXT iS nOT cAPS fIRST"
    private val allCapsText = "THIS TEXT IS ALL CAPS"
    private val allCapsTextFirstLower = "tHIS TEXT IS ALL CAPS"
    private val firstCapText = "This text is not caps first"
    private val firstCapTextFirstLower = "this text is not caps first"
    private val firstCapTextEllipsized = "This...first"
    
    @Test
    fun firstLower() {
        assertEquals(allSmallText.firstLower(), allSmallText)
        assertEquals(nonFirstCapText.firstLower(), nonFirstCapText)
        assertEquals(allCapsText.firstLower(), allCapsTextFirstLower)
        assertEquals(firstCapText.firstLower(), firstCapTextFirstLower)
    }
    
    @Test
    fun firstUpper() {
        assertEquals(allSmallText.firstUpper(), allSmallTextFirstUpper)
        assertEquals(nonFirstCapText.firstUpper(), nonFirstCapTextFirstUpper)
        assertEquals(allCapsText.firstUpper(), allCapsText)
        assertEquals(firstCapText.firstUpper(), firstCapText)
    }
    
    @Test
    fun ellipsize() {
        assertEquals(firstCapText.ellipsize(12, 5), firstCapTextEllipsized)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun ellipsizeFails() {
        assertEquals(firstCapText.ellipsize(3, 3), firstCapTextEllipsized)
    }
}