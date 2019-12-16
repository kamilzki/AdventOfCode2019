package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils

class EmergencyHullPaintingRobot(private val computer: IntcodeComputer) {

    fun numberOfPanelsPainted(colorBegin: Int) = getPaintingPath(colorBegin).count()

    fun getPaintedRegistrationIdentifier(): String {
        val painted = getPaintingPath(1)
        val xList = painted.keys.map { it.first }
        val yList = painted.keys.map { it.second }

        return (yList.max()!! downTo yList.min()!!).joinToString("\n") { y ->
            (xList.min()!!..xList.max()!!).joinToString("") { x -> if (painted[(x to y)] == 1) "#" else " " }
        }
    }

    private fun getPaintingPath(startingWith: Int = 0): MutableMap<Pair<Int, Int>, Int> {
        val location = PaintedPanel(0, 0, startingWith)
        val ship = mutableMapOf(location.position to startingWith)
        computer.runProgram(listOf(startingWith))

        return paint(location, ship)
    }

    private tailrec fun paint(location: PaintedPanel, ship: MutableMap<Pair<Int, Int>, Int>): MutableMap<Pair<Int, Int>, Int> {
        if (computer.state is IntcodeComputer.State.HALT)
            return ship

        val (color, side) = computer.getColorAndSide()
        ship[location.position] = color

        val next = location.getPointNextTo(side, color)

        val colorInput = ship.getOrDefault(next.position, 0)
        computer.runProgram(listOf(colorInput))

        return paint(next, ship)
    }

    private fun IntcodeComputer.getColorAndSide(): Pair<Int, Int> {
        val out = this.outputOfProgram
        return out[0].toInt() to out[1].toInt()
    }
}

data class PaintedPanel(val x: Int, val y: Int, val color: Int, val direction: Direction = Direction.UP) {
    companion object {
        const val LEFT = 0
        const val RIGHT = 1
    }

    val position: Pair<Int, Int> = x to y

    fun getPointNextTo(side: Int, color: Int) = when (direction) {
        Direction.UP -> when (side) {
            LEFT -> getLeft(color)
            RIGHT -> getRight(color)
            else -> throw IllegalArgumentException("Wrong side!")
        }
        Direction.RIGHT -> when (side) {
            LEFT -> getUp(color)
            RIGHT -> getDown(color)
            else -> throw IllegalArgumentException("Wrong side!")
        }
        Direction.DOWN -> when (side) {
            LEFT -> getRight(color)
            RIGHT -> getLeft(color)
            else -> throw IllegalArgumentException("Wrong side!")
        }
        Direction.LEFT -> when (side) {
            LEFT -> getDown(color)
            RIGHT -> getUp(color)
            else -> throw IllegalArgumentException("Wrong side!")
        }
    }

    private fun getUp(color: Int) = copy(y = y + 1, color = color, direction = Direction.UP)
    private fun getDown(color: Int) = copy(y = y - 1, color = color, direction = Direction.DOWN)
    private fun getLeft(color: Int) = copy(x = x - 1, color = color, direction = Direction.LEFT)
    private fun getRight(color: Int) = copy(x = x + 1, color = color, direction = Direction.RIGHT)
}

sealed class Direction {
    object UP : Direction()
    object RIGHT : Direction()
    object DOWN : Direction()
    object LEFT : Direction()
}

fun main() {
    val instruction = Utils.readInput("day11")
            .split(',')
            .map {
                it.trimEnd().toLong()
            }

    val panelsPainted = EmergencyHullPaintingRobot(IntcodeComputer(instruction)).numberOfPanelsPainted(0)
    println("Painted at least once panels: $panelsPainted")

    val identifier = EmergencyHullPaintingRobot(IntcodeComputer(instruction)).getPaintedRegistrationIdentifier()
    println("Identifier:\n$identifier")
}
