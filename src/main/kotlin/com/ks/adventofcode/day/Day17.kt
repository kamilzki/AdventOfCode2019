package com.ks.adventofcode.day

import com.ks.adventofcode.util.Point2D

class CameraScaffold(private val input: List<Long>) {

    fun part1(): Int {
        val computer = IntcodeComputer(input)
        computer.runProgram()
        val cameraOutput = cameraOutput()
        cameraOutput.print()
        return cameraOutput.scaffoldIntersections().sumBy { it.x * it.y }
    }

    fun part2(instructions: List<String>): Long {
        val manual = listOf(2L) + input.drop(1)
        val computer = IntcodeComputer(manual)
        computer.runProgram()

        val flatMap = instructions.flatMap { line -> line.map { it.toInt() } + 10 }
        computer.runProgram(flatMap)

        return computer.outputOfProgram.last()
    }

    private fun cameraOutput(): List<CharacterPoint> {
        val computer = IntcodeComputer(input)
        computer.runProgram()
        val map = computer.outputOfProgram
            .map { it.toChar() }
            .joinToString("")
            .lines()
            .mapIndexed { y, row ->
                row.mapIndexed { x, char -> CharacterPoint(Point2D(x, y), char) }
            }
            .flatten()
        return map
    }

    private fun List<CharacterPoint>.scaffoldIntersections(): List<Point2D> {
        val scaffold = this.filter { it.char == '#' }
            .map { it.point }
        return scaffold.filter { point -> scaffold.containsAll(point.getNeighbours()) }
    }

    private fun List<CharacterPoint>.print() {
        val byRows = this.groupBy { it.point.y }
        byRows.forEach { (_, list) ->
            list.forEach { (_, c) ->
                print(c)
            }
            println()
        }
    }
}

data class CharacterPoint(val point: Point2D, val char: Char)
