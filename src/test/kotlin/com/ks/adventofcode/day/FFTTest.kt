package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class FFTTest {

    @Nested
    inner class Part1 {

        @Test
        internal fun `should match example a`() {
            //given
            val input = "12345678"

            //when
            val result = FFT.calculate(input, 1)

            //then
            assertEquals("48226158", result)
        }

        @Test
        internal fun `should match example b`() {
            //given
            val input = "12345678"

            //when
            val result = FFT.calculate(input, 4)

            //then
            assertEquals("01029498", result)
        }

        @Test
        internal fun `should match example c`() {
            //given
            val input = "80871224585914546619083218645595"

            //when
            val result = FFT.calculate(input)

            //then
            assertEquals("24176176", result)
        }

        @Test
        internal fun `should match example d`() {
            //given
            val input = "19617804207202209144916044189917"

            //when
            val result = FFT.calculate(input)

            //then
            assertEquals("73745418", result)
        }

        @Test
        internal fun `should match example e`() {
            //given
            val input = "69317163492948606335995924319873"

            //when
            val result = FFT.calculate(input)

            //then
            assertEquals("52432133", result)
        }

        @Test
        internal fun `should correctly solve part 1`() {
            //given
            val input = Utils.readInput("day16")

            //when
            val result = FFT.calculate(input)

            //then
            assertEquals("85726502", result)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `should match example a`() {
            //given
            val input = "03036732577212944063491565474664"

            //when
            val result = FFT.part2(input)

            //then
            assertEquals("84462026", result)
        }

        @Test
        internal fun `should match example b`() {
            //given
            val input = "02935109699940807407585447034323"

            //when
            val result = FFT.part2(input)

            //then
            assertEquals("78725270", result)
        }

        @Test
        internal fun `should correctly solve part 2`() {
            //given
            val input = Utils.readInput("day16")

            //when
            val result = FFT.part2(input)

            //then
            assertEquals("92768399", result)
        }
    }
}
