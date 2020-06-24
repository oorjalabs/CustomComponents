package net.c306.customcomponents.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.math.BigDecimal

class CommonUtilsTest {

    @Test
    fun getNamedVersion() {
        val buildConfigVersionName = "2020.0624.0.1.16"
        val expectedVersionName = BigDecimal("0.1")
        assertEquals(CommonUtils.getNamedVersion(buildConfigVersionName), expectedVersionName)
    }

    @Test
    fun getNamedVersionFallback() {
        val buildConfigVersionName = "0.1.16"
        val expectedVersionName = BigDecimal("0.1")
        val calculatedVersionName = CommonUtils.getNamedVersion(buildConfigVersionName)
        assertNotEquals(calculatedVersionName, expectedVersionName)
        assertEquals(calculatedVersionName, BigDecimal(-1))
    }
}