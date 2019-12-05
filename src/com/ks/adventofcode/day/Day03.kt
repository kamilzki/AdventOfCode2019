package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import kotlin.math.absoluteValue

class WireCircuitSolver(input: List<String>) {
    companion object {
        private const val RIGHT = 'R'
        private const val UP = 'U'
        private const val DOWN = 'D'
        private const val LEFT = 'L'
    }

    data class WirePoint(val x: Int, val y: Int) {
        fun getManhattanDistanceToCentralPort(): Int = x.absoluteValue + y.absoluteValue

        fun getPointNextTo(direction: Char) = when (direction) {
            UP -> this.getUp()
            DOWN -> this.getDown()
            LEFT -> this.getLeft()
            RIGHT -> this.getRight()
            else -> throw IllegalArgumentException("Invalid direction: $direction")
        }

        private fun getUp(): WirePoint = copy(y = y + 1)
        private fun getDown(): WirePoint = copy(y = y - 1)
        private fun getLeft(): WirePoint = copy(x = x - 1)
        private fun getRight(): WirePoint = copy(x = x + 1)
    }

    private val firstWire: List<WirePoint> = tracePath(input[0])
    private val secondWire: List<WirePoint> = tracePath(input[1])
    private val intersections: Set<WirePoint> = getIntersectionPoints()

    private fun tracePath(line: String): List<WirePoint> = line.split(",")
        .fold(listOf()) { acc, s ->
            val direction = s[0]
            val moveTo = s.drop(1).toInt()
            val element = if (acc.isEmpty()) WirePoint(0, 0) else acc.last()
            acc + generateWirePoints(element, direction).take(moveTo)
        }

    private fun getIntersectionPoints(): Set<WirePoint> {
        val intersect = firstWire.intersect(secondWire)
        require(intersect.isNotEmpty()) { "Wires don't have any intersection points!" }
        return intersect
    }

    fun findTheClosestToTheCentralPoint(): Int = intersections.map { it.getManhattanDistanceToCentralPort() }.min()!!

    fun findTheFewestStepsToGetIntersection(): Int = intersections.map { intersectionPoint ->
        firstWire.indexOf(intersectionPoint) + secondWire.indexOf(intersectionPoint) + 2
    }.min()!!

    private fun generateWirePoints(init: WirePoint, direction: Char) =
        generateSequence(init.getPointNextTo(direction)) { it.getPointNextTo(direction) }
}

fun main() {
    val d = WireCircuitSolver(Utils.readInputByLines("day03"))
    println(d.findTheClosestToTheCentralPoint())
    println(d.findTheFewestStepsToGetIntersection())
}
