package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RepairDroidTest {

    @Test
    internal fun `should correctly solve part 1`() {
        //given
        val input = Utils.readIntcodeInput("day15")

        //when
        val result = RepairDroid(input).foundOxygenSystem()

        //then
        assertEquals(374, result)
    }

    @Test
    internal fun `should correctly solve part 2`() {
        //given
        val input = Utils.readIntcodeInput("day15")

        //when
        val result = RepairDroid(input).getTimeToFillOxygen()

        //then
        assertEquals(482, result)
    }
}
