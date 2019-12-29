package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CameraScaffoldTest {

    //given
    private val camera = CameraScaffold(Utils.readIntcodeInput("day17"))

    @Test
    internal fun `should correctly solve part 1`() {

        //when
        val result = camera.part1()

        //then
        assertEquals(12512, result)
    }

    @Test
    internal fun `should correctly solve part 2`() {
        //given
        val instructions = listOf(
            "A,B,A,C,B,C,B,C,A,C",
            "R,12,L,10,R,12",
            "L,8,R,10,R,6",
            "R,12,L,10,R,10,L,8",
            "N"
        )

        //when
        val result = camera.part2(instructions)

        //then
        assertEquals(1409507, result)
    }
}
