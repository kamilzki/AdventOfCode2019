package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SpringdroidTest {

    //given
    val input = Utils.readIntcodeInput("day21")

    @Test
    internal fun `should correctly solve part 1`() {
        //when
        val result = Springdroid(input).part1()

        //then
        assertEquals(19_361_023, result)
    }

    @Test
    internal fun `should correctly solve part 2`() {
        //when
        val result = Springdroid(input).part2()

        //then
        assertEquals(1_141_457_530, result)
    }
}
