package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils

class Amplifier(program: List<Long>, private val phase: Int) {
    var done = false
    var first = true
    private val intcodeComputer = IntcodeComputer(program)

    fun execute(inputInstructions: List<Int>): List<Long> {
        val instructions = if (first) listOf(phase) + inputInstructions else inputInstructions
        intcodeComputer.runProgram(instructions)
        first = false
        if (intcodeComputer.isHalt())
            done = true
        return intcodeComputer.outputOfProgram
    }
}

class AmplitudeSolver(private val program: List<Long>, private val phases: List<Int>) {

    fun findTheHighestSignal(feedbackLoop: Boolean = false): Int {
        val logic = if (!feedbackLoop)
            { a: List<Amplifier> -> standardConnection(a) }
        else
            { a: List<Amplifier> -> feedbackLoop(a) }

        return executeAmplifiers(logic)
    }
    private fun executeAmplifiers(logic: (List<Amplifier>) -> Int): Int {
        return possibleCombinationsOfPhases().map { combination ->
            val amplifiers = combination.map { c -> Amplifier(program, c) }
            logic(amplifiers)
        }.max()!!
    }

    private fun standardConnection(amplifiers: List<Amplifier>): Int {
        var output = 0
        amplifiers.forEach { amplifier ->
            val inputInstructions = listOf(output)
            output = amplifier.execute(inputInstructions).last().toInt()
        }
        return output
    }

    private fun feedbackLoop(amplifiers: List<Amplifier>): Int {
        var output = listOf(0)
        while (!amplifiers.last().done) {
            amplifiers.forEach { amplifier ->
                val inputInstructions = output
                output = amplifier.execute(inputInstructions).map { it.toInt() }
            }
        }
        return output.last()
    }

    private fun possibleCombinationsOfPhases(): MutableList<List<Int>> {
        val results = mutableListOf<List<Int>>()
        findCombinations(listOf(), phases, results)
        return results
    }

    private fun findCombinations(
        prefix: List<Int>,
        possible: List<Int>,
        results: MutableList<List<Int>>
    ) {
        if (possible.isEmpty()) {
            results.add(prefix)
        } else {
            possible.map { findCombinations(prefix + it, possible - it, results) }
        }
    }

}

fun main() {
    val amplifierControllerSoftware = Utils.readInput("day07")
        .split(',')
        .map { it.trimEnd().toLong() }
    part1(amplifierControllerSoftware)
    part2(amplifierControllerSoftware)
}

private fun part1(amplifierControllerSoftware: List<Long>) {
    val phases = (0..4).toList()
    val solver = AmplitudeSolver(amplifierControllerSoftware, phases)
    println("Max output for standard connection: ${solver.findTheHighestSignal()}")
}

fun part2(amplifierControllerSoftware: List<Long>) {
    val phases = (5..9).toList()
    val solver = AmplitudeSolver(amplifierControllerSoftware, phases)
    println("Max output for feedback loop: ${solver.findTheHighestSignal(true)}")
}
