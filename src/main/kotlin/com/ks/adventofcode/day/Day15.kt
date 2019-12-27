package com.ks.adventofcode.day

import com.ks.adventofcode.util.AStar
import com.ks.adventofcode.util.Grid
import com.ks.adventofcode.util.Point2D
import com.ks.adventofcode.util.Utils
import java.lang.IllegalArgumentException


class RepairDroid(input: List<Long>) {
    companion object {
        private val begin = Point2D(0, 0)
        private const val wall: Char = '#'
        private const val free: Char = '.'
        private const val oxygenSystem: Char = 'O'
    }

    private val computer = IntcodeComputer(input)
    private val map = move(MovementPoint(begin, NoMovement))
    private var oxygenPoint = map.entries.first { it.value == oxygenSystem }.key

    fun foundOxygenSystem(): Int {
        val grid = Grid(map.entries
            .filter { it.value == free || it.value == oxygenSystem }
            .map { it.key }
            .toSet())
        return AStar(grid).search(begin, oxygenPoint)
    }

    fun getTimeToFillOxygen(): Int {
        val oxygenState = map.entries
            .filter { it.value == free }
            .map { it.key }
            .toSet()

        return spreadOxygen(setOf(oxygenPoint), oxygenState - oxygenPoint, 0)
    }

    private tailrec fun spreadOxygen(withAir: Set<Point2D>, withoutAir: Set<Point2D>, time: Int): Int {
        if (withoutAir.isEmpty())
            return time

        val toSpread = withoutAir.intersect(withAir.flatMap { it.getNeighbours() })
        return spreadOxygen(withAir + toSpread, withoutAir - toSpread, time + 1)
    }


    private fun move(
        from: MovementPoint,
        mapOfLocations: MutableMap<Point2D, Char> = mutableMapOf()
    ): Map<Point2D, Char> {
        val fromM = from.getNeighbours()

        fromM.filterNot { it.point in mapOfLocations }.forEach { m ->
            if (m.movement is Movement)
                computer.runProgram(listOf(m.movement.command))
            else
                throw IllegalStateException("Sth go wrong!")

            val status = Status(computer.outputOfProgram.first())
            mapOfLocations[m.point] = when (status) {
                is Status.OxygenStation -> {
                    oxygenPoint = m.point
                    oxygenSystem
                }
                is Status.Moved -> free
                is Status.Wall -> wall
            }

            if (status is Status.OxygenStation || status is Status.Moved) {
                move(m, mapOfLocations)
                computer.runProgram(listOf(m.movement.getOpposite().command))
            }
        }

        return mapOfLocations
    }
}

data class MovementPoint(val point: Point2D, val movement: AbstractMovement) {
    fun getNeighbours(): List<MovementPoint> {
        return Movement.movements.map { MovementPoint(getPoint(it), it) }
    }

    private fun getPoint(m: Movement) = when (m) {
        is Movement.North -> point.up()
        is Movement.South -> point.down()
        is Movement.West -> point.left()
        is Movement.East -> point.right()
    }
}

sealed class AbstractMovement
object NoMovement : AbstractMovement()

sealed class Movement(val command: Int) : AbstractMovement() {
    companion object {
        val movements = setOf(North, South, West, East)

        operator fun invoke(command: Int) = when (command) {
            North.command -> North
            South.command -> South
            West.command -> West
            East.command -> East
            else -> throw IllegalArgumentException("Wrong command: $command")
        }
    }

    abstract fun getOpposite(): Movement

    object North : Movement(1) {
        override fun getOpposite(): Movement = South
    }

    object South : Movement(2) {
        override fun getOpposite(): Movement = North
    }

    object West : Movement(3) {
        override fun getOpposite(): Movement = East
    }

    object East : Movement(4) {
        override fun getOpposite(): Movement = West
    }
}

sealed class Status {
    companion object {
        operator fun invoke(code: Long) = when (code) {
            0L -> Wall
            1L -> Moved
            2L -> OxygenStation
            else -> throw IllegalArgumentException("Wrong code: $code")
        }
    }

    object Wall : Status()
    object Moved : Status()
    object OxygenStation : Status()
}

fun main() {
    val input = Utils.readIntcodeInput("day15")

    val droid = RepairDroid(input)
    val cost = droid.foundOxygenSystem()
    println("Minimum moves to get Oxygen system $cost")
    println("Time to fill oxygen in all location: ${droid.getTimeToFillOxygen()}")
}
