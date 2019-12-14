package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils

class GravityAssistSolver(private val programValues: List<Long>, private val min: Long = 0, private val max: Long = 99) {
    fun solve(resultToFound: Long): Pair<Long, Long>? {
        generatePairs(min).takeWhile { it.second <= max }.forEach {
            val currentValues = programValues.toMutableList()
            currentValues.changeNoun(it.first)
            currentValues.changeVerb(it.second)
            val programRunner = IntcodeComputer(currentValues)
            if (programRunner.runProgram() == resultToFound)
                return it
        }
        return null
    }

    private fun generatePairs(min: Long) =
            generateSequence(Pair(min, min), { getNextPair(it.first, it.second) })

    private fun getNextPair(min: Long, max: Long) = if (min >= 99L) Pair(0L, max + 1) else Pair(min + 1, max)
}

private fun <E> MutableList<E>.changeNoun(value: E) {
    this[1] = value
}

private fun <E> MutableList<E>.changeVerb(value: E) {
    this[2] = value
}

internal fun backTo1202AlarmState(programValues: List<Long>): MutableList<Long> {
    val mutable = programValues.toMutableList()
    mutable.changeNoun(12)
    mutable.changeVerb(2)
    return mutable
}

fun main() {
    val programValues = Utils.readInput("day02").split(',')
            .map { it.trimEnd().toLong() }
    val programRunner = IntcodeComputer(backTo1202AlarmState(programValues))
    println("Part 1: " + programRunner.runProgram())

    val gravityAssistSolver = GravityAssistSolver(programValues)
    val solution = gravityAssistSolver.solve(19690720L) ?: throw IllegalArgumentException("No solution!")
    println("Part 2: " + (100 * solution.first + solution.second))
}
