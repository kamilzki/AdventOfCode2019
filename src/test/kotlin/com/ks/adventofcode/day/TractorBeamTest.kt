package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TractorBeamTest {
    //given
    private val tractorBeam = TractorBeam(Utils.readIntcodeInput("day19"))

    @Test
    internal fun `should correctly solve part 1`() {
        //when
        val result = tractorBeam.scanWithinRange(49, 49)

        //then
        assertEquals(217, result)
    }

    @Test
    internal fun `should correctly solve part 2`() {
        //when
        val (x, y) = tractorBeam.minDistanceToFitSquare(100)
        val result = x * 10_000 + y

        //then
        assertEquals(6840937, result)
    }
}
