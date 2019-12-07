package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils


fun main() {
    val program = Utils.readInput("day05")
        .split(',')
        .map { it.trimEnd().toInt() }
    IntcodeComputer(program, 1).runProgram()
    IntcodeComputer(program, 5).runProgram()
}
