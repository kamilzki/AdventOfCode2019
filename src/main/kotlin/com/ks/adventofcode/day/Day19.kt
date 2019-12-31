package com.ks.adventofcode.day

class TractorBeam(private val program: List<Long>) {

    companion object {
        const val pulled = 1L
    }

    fun scanWithinRange(toX: Int, toY: Int): Int = (0..toX).sumBy { x ->
        (0..toY).count { y ->
            withinRange(x, y)
        }
    }

    tailrec fun minDistanceToFitSquare(size: Int, x: Int = nextCorrectX(0, size -1), y: Int = 0): Pair<Int, Int> =
        if (withinRange(x + size - 1, y))
            x to y
        else
            minDistanceToFitSquare(size, nextCorrectX(x, y + size), y + 1)

    private fun withinRange(x: Int, y: Int): Boolean = IntcodeComputer(program).run {
        this.runProgram(listOf(x, y))
        this.outputOfProgram.first() == pulled
    }

    private tailrec fun nextCorrectX(x: Int, y: Int): Int = if (withinRange(x, y)) x else nextCorrectX(x + 1, y)
}
