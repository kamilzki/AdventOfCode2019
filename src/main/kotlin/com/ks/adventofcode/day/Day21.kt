package com.ks.adventofcode.day

class Springdroid(private val input: List<Long>) {

    fun part1(): Long {
        val instructions = listOf(
            "NOT C J",
            "AND D J",
            "NOT A T",
            "OR T J",
            "WALK"
        )

        return runInstruction(instructions)
    }

    fun part2(): Long {
        val instructions = listOf(
            "NOT C J",
            "AND D J",
            "AND H J",
            "NOT B T",
            "AND D T",
            "OR T J",
            "NOT A T",
            "OR T J",
            "RUN"
        )

        return runInstruction(instructions)
    }

    private fun runInstruction(instructions: List<String>): Long {
        val computer = IntcodeComputer(input)
        computer.runProgram(instructions.flatMap { line -> line.map { it.toInt() } + 10 })
        return computer.outputOfProgram.last()
    }
}
