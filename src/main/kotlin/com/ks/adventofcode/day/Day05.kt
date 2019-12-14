package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils


fun main() {
    val program = Utils.readInput("day05")
        .split(',')
        .map { it.trimEnd().toLong() }
    val intcodeComputer = IntcodeComputer(program)
    intcodeComputer.runProgram(listOf(1))
    println("Part 1: " + intcodeComputer.outputOfProgram.last())

    val intcodeComputer2 = IntcodeComputer(program)
    intcodeComputer2.runProgram(listOf(5))
    println("Part 2: " + intcodeComputer2.outputOfProgram.last())
}
