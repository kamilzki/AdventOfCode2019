package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import kotlin.math.sign

class ArcadeGame(input: List<Long>, freeToPlay: Boolean = false) {
    private val intcodeComputer =
        if (!freeToPlay) IntcodeComputer(input) else IntcodeComputer(listOf(playForFree) + input.drop(1))

    companion object {
        private const val playForFree = 2L
        private const val score = -1L
        private const val paddle = 3L
        private const val ball = 4L
    }

    fun getNumberOfTiles(tile: Int): Int {
        start()
        val groupBy = getTiles().groupBy { it[2] }
        return (groupBy[tile.toLong()] ?: error("Not found any tiles - $tile")).count()
    }

    fun winTheGame(): Long {
        start()
        return makeMove()
    }

    private tailrec fun makeMove(): Long {
        if (intcodeComputer.isHalt()) {
            return getTiles().first { (x, _, _) -> x == score }[2]
        }

        val (_, input) = getTiles().fold(-1L to emptyList<Int>()) { (paddleX, input), (x, _, type) ->
            when (type) {
                ball -> x to listOf((x - paddleX).sign)
                paddle -> x to input
                else -> paddleX to input
            }
        }

        intcodeComputer.runProgram(input)
        return makeMove()
    }

    private fun getTiles() = intcodeComputer.outputOfProgram.chunked(3)

    private fun start() {
        intcodeComputer.runProgram()
    }
}

fun main() {
    val input = Utils.readInput("day13")
        .split(',')
        .map { it.trimEnd().toLong() }

    println("Number of block tiles: " + ArcadeGame(input).getNumberOfTiles(2))
    println("Score after win: " + ArcadeGame(input, true).winTheGame())
}
