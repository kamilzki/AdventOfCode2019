package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ArcadeGameTest {

    private val input = Utils.readInput("day13")
        .split(',')
        .map { it.trimEnd().toLong() }

    @Test
    internal fun `should correctly solve part 1`() {
        //given
        val game = ArcadeGame(input)

        //when
        val result = game.getNumberOfTiles(2)

        //then
        assertEquals(462, result)
    }

    @Test
    internal fun `should correctly solve part 2`() {
        //given
        val game = ArcadeGame(input, true)

        //when
        val result = game.winTheGame()

        //then
        assertEquals(23981, result)
    }
}
