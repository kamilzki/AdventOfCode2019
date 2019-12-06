package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils


fun main() {
    val program = Utils.readInput("day05")
        .split(',')
        .map { it.trimEnd().toInt() }
    ShipComputer(program).runProgram()
}
