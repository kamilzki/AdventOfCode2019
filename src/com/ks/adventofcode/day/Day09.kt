package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils

fun main() {
    val program = Utils.readInput("day09")
            .split(',')
            .map { it.trimEnd().toLong() }
    println("BOOST keycode produced: " + run(program, listOf(1)))
    println("Coordinates of the distress signal: " + run(program, listOf(2)))
}

private fun run(program: List<Long>, input: List<Int>): MutableList<Long> {
    val intcodeComputer = IntcodeComputer(program.toList())
    intcodeComputer.runProgram(input)
    return intcodeComputer.outputOfProgram
}
