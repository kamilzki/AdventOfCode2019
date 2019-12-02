package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils

class ProgramRunner(programValues: List<Int>) {
    private val memory = programValues.toMutableList()
    companion object {
        private const val ADD_OPTCODE = 1
        private const val MUL_OPTCODE = 2
        private const val END_OPTCODE = 99
    }

    fun runProgram(): MutableList<Int> {
        solveSubprogram(0)
        return memory
    }

    private tailrec fun solveSubprogram(instructionPointer: Int) {
        if (memory[instructionPointer] == END_OPTCODE)
            return
        memory[memory[instructionPointer + 3]] = computeValueForOpcode(instructionPointer)
        solveSubprogram(instructionPointer + 4)
    }

    private fun computeValueForOpcode(position: Int): Int {
        return when (val optcode = memory[position]) {
            ADD_OPTCODE -> getValueAt(position + 1) + getValueAt(position + 2)
            MUL_OPTCODE -> getValueAt(position + 1) * getValueAt(position + 2)
            else -> throw IllegalStateException("Wrong opcode value to compute: $optcode")
        }
    }

    private fun getValueAt(position: Int) = memory[memory[position]]
}

class GravityAssistSolver(private val programValues: List<Int>, private val min: Int = 0, private val max: Int = 99) {
    fun solve(resultToFound: Int): Pair<Int, Int>? {
        generatePairs(min).takeWhile { it.second <= max }.forEach {
            val currentValues = programValues.toMutableList()
            currentValues.changeNoun(it.first)
            currentValues.changeVerb(it.second)
            val programRunner = ProgramRunner(currentValues)
            if (programRunner.runProgram()[0] == resultToFound)
                return it
        }
        return null
    }

    private fun generatePairs(min: Int) =
        generateSequence(Pair(min, min), { getNextPair(it.first, it.second) })

    private fun getNextPair(min: Int, max: Int) = if (min >= 99) Pair(0, max + 1) else Pair(min + 1, max)
}

private fun <E> MutableList<E>.changeNoun(value: E) {
    this[1] = value
}

private fun <E> MutableList<E>.changeVerb(value: E) {
    this[2] = value
}

private fun backTo1202AlarmState(programValues: List<Int>): MutableList<Int> {
    val mutable = programValues.toMutableList()
    mutable.changeNoun(12)
    mutable.changeVerb(2)
    return mutable
}

fun main() {
    val programValues = Utils.readInput("day02").split(',').map { it.trimEnd().toInt() }
    val programRunner = ProgramRunner(backTo1202AlarmState(programValues))
    println("Part 1: " + programRunner.runProgram()[0])

    val gravityAssistSolver = GravityAssistSolver(programValues)
    val solution = gravityAssistSolver.solve(19690720) ?: throw IllegalArgumentException("No solution!")
    println("Part 2: " + (100 * solution.first + solution.second))
}
