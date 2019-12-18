package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class MoonSimulatorTest {

    @Nested
    inner class Part1 {

        @Test
        internal fun `should match example a`() {
            //given
            val input = Utils.readInputByLines("day12a")
            val ms = MoonSimulator(input)

            //when
            val result = ms.getTotalEnergy(10)

            //then
            assertEquals(179, result)
        }

        @Test
        internal fun `should match example b`() {
            //given
            val input = Utils.readInputByLines("day12b")
            val ms = MoonSimulator(input)

            //when
            val result = ms.getTotalEnergy(100)

            //then
            assertEquals(1940, result)
        }

        @Test
        internal fun `should correctly solve part 1`() {
            //given
            val input = Utils.readInputByLines("day12")
            val ms = MoonSimulator(input)

            //when
            val result = ms.getTotalEnergy(1000)

            //then
            assertEquals(11384, result)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `should match example a`() {
            //given
            val input = Utils.readInputByLines("day12a")
            val ms = MoonSimulator(input)

            //when
            val result = ms.whenComeToTheSameState()

            //then
            assertEquals(2772, result)
        }

        @Test
        internal fun `should match example b`() {
            //given
            val input = Utils.readInputByLines("day12b")
            val ms = MoonSimulator(input)

            //when
            val result = ms.whenComeToTheSameState()

            //then
            assertEquals(4_686_774_924, result)
        }

        @Test
        internal fun `should correctly solve part 2`() {
            //given
            val input = Utils.readInputByLines("day12")
            val ms = MoonSimulator(input)

            //when
            val result = ms.whenComeToTheSameState()

            //then
            assertEquals(452_582_583_272_768, result)
        }
    }
}
