package com.ks.adventofcode.day

import com.ks.adventofcode.util.Point2D
import java.util.*

typealias TunnelState = Pair<Set<Point2D>, Set<Char>>

class TunnelNavigator(input: List<String>) {

    private val tunnels = input.mapIndexed { y, line ->
        line.mapIndexed { x, el -> Point2D(x, y) to TunnelElement(el) }
    }.flatten()
        .filterNot { it.second is Wall }
        .toMap()
        .toMutableMap()

    private val doors = tunnels.mapNotNull { (key, value) -> if (value is Door) key to value else null }.toMap()
    private val keys = tunnels.mapNotNull { (key, value) -> if (value is Key) key to value.char else null }.toMap()

    fun solve(): Int {
        val entrances = tunnels.entries
            .filter { it.value is Entrance }
            .map { it.key }
            .toSet()

        return calculateMinSteps(entrances)
    }

    private fun calculateMinSteps(
        begin: Set<Point2D>,
        currentKeys: Set<Char> = setOf(),
        seen: MutableMap<TunnelState, Int> = mutableMapOf()
    ): Int {
        val state: TunnelState = Pair(begin, currentKeys)
        seen[state]?.let { return it }

        val steps = state.reachableFromCurrentState().map { (key, value) ->
            val (to, steps, from) = value
            steps + calculateMinSteps((begin - from) + to, currentKeys + key, seen)
        }.min() ?: 0
        seen[state] = steps
        return steps
    }

    private fun TunnelState.reachableFromCurrentState(): Map<Char, Triple<Point2D, Int, Point2D>> =
        this.first.map { point ->
            val findReachableKeys = findKeys(
                this.second,
                ArrayDeque<Point2D>().apply { add(point) },
                mutableMapOf(point to 0),
                mutableMapOf()
            )
            findReachableKeys.map { (key, value) ->
                key to Triple(value.first, value.second, point)
            }
        }.flatten()
            .toMap()

    private tailrec fun findKeys(
        currentKeys: Set<Char>,
        queue: ArrayDeque<Point2D>,
        distanceFromStart: MutableMap<Point2D, Int>,
        distanceToKey: MutableMap<Char, Pair<Point2D, Int>>
    ): MutableMap<Char, Pair<Point2D, Int>> {
        if (queue.isEmpty())
            return distanceToKey

        val nextMove = queue.pop()
        nextMove.getNeighbours()
            .filter { it in tunnels && it !in distanceFromStart }
            .onEach { move ->
                distanceFromStart[move] = distanceFromStart[nextMove]!! + 1
                val door = doors[move]
                if (door == null || door.keyToDoor in currentKeys) {
                    val key = keys[move]
                    if (key != null && key !in currentKeys) {
                        distanceToKey[key] = Pair(move, distanceFromStart[move]!!)
                    } else {
                        queue.add(move)
                    }
                }
            }

        return findKeys(currentKeys, queue, distanceFromStart, distanceToKey)
    }
}

sealed class TunnelElement(open val char: Char) {
    companion object {
        operator fun invoke(char: Char): TunnelElement = when (char) {
            Wall.char -> Wall
            Free.char -> Free
            Entrance.char -> Entrance
            in ('a'..'z') -> Key(char)
            in ('A'..'Z') -> Door(char)
            else -> throw IllegalArgumentException("Wrong char $char")
        }
    }
}

data class Door(override val char: Char) : TunnelElement(char) {
    val keyToDoor = char.toLowerCase()
}

data class Key(override val char: Char) : TunnelElement(char)

object Wall : TunnelElement('#')
object Free : TunnelElement('.')
object Entrance : TunnelElement('@')
