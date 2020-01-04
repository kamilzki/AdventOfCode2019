package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SpaceShuffleTest {

    @Test
    internal fun `positive cut - technique`() {
        //given
        val instructions = listOf("cut 3")

        //when
        val result = SpaceShuffle(10).shuffle(instructions)

        //then
        assertEquals(listOf(3, 4, 5, 6, 7, 8, 9, 0, 1, 2), result)
    }

    @Test
    internal fun `negative cut - technique`() {
        //given
        val instructions = listOf("cut -4")

        //when
        val result = SpaceShuffle(10).shuffle(instructions)

        //then
        assertEquals(listOf(6, 7, 8, 9, 0, 1, 2, 3, 4, 5), result)
    }

    @Test
    internal fun `deal into new stack - technique`() {
        //given
        val instructions = listOf("deal into new stack")

        //when
        val result = SpaceShuffle(10).shuffle(instructions)

        //then
        assertEquals(listOf(9, 8, 7, 6, 5, 4, 3, 2, 1, 0), result)
    }

    @Test
    internal fun `deal with increment - technique`() {
        //given
        val instructions = listOf("deal with increment 3")

        //when
        val result = SpaceShuffle(10).shuffle(instructions)

        //then
        assertEquals(listOf(0, 7, 4, 1, 8, 5, 2, 9, 6, 3), result)
    }

    @Test
    internal fun `should match example a`() {
        //given
        val instructions = listOf(
            "cut 6",
            "deal with increment 7",
            "deal into new stack"
        )

        //when
        val result = SpaceShuffle(10).shuffle(instructions)

        //then
        assertEquals(listOf(3, 0, 7, 4, 1, 8, 5, 2, 9, 6), result)
    }

    @Test
    internal fun `should match example b`() {
        //given
        val instructions = listOf(
            "deal into new stack",
            "cut -2",
            "deal with increment 7",
            "cut 8",
            "cut -4",
            "deal with increment 7",
            "cut 3",
            "deal with increment 9",
            "deal with increment 3",
            "cut -1"
        )

        //when
        val result = SpaceShuffle(10).shuffle(instructions)

        //then
        assertEquals(listOf(9, 2, 5, 8, 1, 4, 7, 0, 3, 6), result)
    }

    @Test
    internal fun `should solve part 1`() {
        //given
        val instructions = Utils.readInputByLines("day22")

        //when
        val result = SpaceShuffle().shuffle(instructions)

        //then
        assertEquals(3749, result.indexOf(2019))
    }
}
