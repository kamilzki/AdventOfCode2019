package com.ks.adventofcode.day

import com.ks.adventofcode.util.Point2D
import java.util.*

typealias LevelPoint = Pair<Point2D, Int>

class DonutMaze(input: String) {
    private val start: Point2D
    private val end: Point2D
    private val free: Set<Point2D>
    private val portalJumps: Map<Point2D, Point2D>
    private val outwardX: Set<Int>
    private val outwardY: Set<Int>

    init {

        tailrec fun removeDeadEnds(
            currentFree: Set<Point2D>,
            portalsAndEntries: Set<Point2D>,
            deadEnds: Set<Point2D> = setOf()
        ): Set<Point2D> {
            val newDeadEnds = currentFree.mapNotNull {
                if (it in portalsAndEntries) return@mapNotNull null
                val neighbours = it.getNeighbours().intersect(currentFree)
                if (neighbours.size == 1) it else null
            }.toSet()
            return if (newDeadEnds.isNotEmpty())
                removeDeadEnds(currentFree - newDeadEnds, portalsAndEntries, deadEnds + newDeadEnds)
            else
                currentFree
        }

        val inputByLines = input.lines()
            .dropLastWhile { it.isBlank() }

        this.outwardX = setOf(2, inputByLines[2].lastIndexOf('#'))
        this.outwardY = setOf(2, inputByLines.size - 3)

        val maze = inputByLines
            .mapIndexed { y, line ->
                line.mapIndexedNotNull { x, c -> if (c == '.' || c in 'A'..'Z') Point2D(x, y) to c else null }
            }.flatten()
            .toMap()

        val portalName = { (p1, c1): Pair<Point2D, Char>, (p2, c2): Pair<Point2D, Char> ->
            when {
                p1.x == p2.x -> if (p1.y > p2.y) "$c1$c2" else "$c2$c1"
                p1.x > p2.x -> "$c1$c2"
                else -> "$c2$c1"
            }
        }

        val portalsAndEntries = maze.filter { it.value != '.' }
            .mapNotNull { (point, value) ->
                val neighbours = point.getNeighbours().mapNotNull {
                    val m = maze[it]
                    if (m != null) it to m else null
                }

                if (neighbours.size < 2)
                    return@mapNotNull null

                val (point1, r1) = neighbours[0]
                val (point2, r2) = neighbours[1]

                if (r1 == '.' && r2 in 'A'..'Z') {
                    val s = portalName(point to value, point2 to r2)
                    point1 to s
                } else if (r2 == '.' && r1 in 'A'..'Z') {
                    val s = portalName(point to value, point1 to r1)
                    point2 to s
                } else
                    null
            }

        this.free = removeDeadEnds(
            maze.filter { it.value == '.' }
                .map { it.key }
                .toSet(),
            portalsAndEntries.toMap().keys
        )

        this.start = portalsAndEntries.asSequence()
            .first { (_, type) -> type == "AA" }
            .first
        this.end = portalsAndEntries.asSequence()
            .first { (_, type) -> type == "ZZ" }
            .first

        val portals = portalsAndEntries.filterNot { it.first == start || it.first == end }
            .toMap()
        this.portalJumps = portals.map { current ->
            current.key to portals.asSequence().first {
                it.value == current.value && it.key != current.key
            }.key
        }.toMap()
    }

    fun minimumSteps() = bfs()

    private tailrec fun bfs(
        visited: MutableList<Point2D> = mutableListOf(start),
        queue: ArrayDeque<Pair<Point2D, Int>> = ArrayDeque(listOf(start to 0))
    ): Int {
        require(queue.isNotEmpty()) { "Not found a path to end" }

        val (current, steps) = queue.pop()
        if (current == end) return steps

        current.moves()
            .onEach { move ->
                if (move !in visited) {
                    visited.add(move)
                    queue.addLast(move to steps + 1)
                }
            }
        return bfs(visited, queue)
    }

    private fun Point2D.moves(): List<Point2D> {
        val neighbors = this.getNeighbours().filter { it in free }
        return portalJumps[this]?.let { neighbors + it } ?: neighbors
    }

    fun minimumStepsWithLevels() = bfsWithLevels()

    private tailrec fun bfsWithLevels(
        discovered: MutableSet<Pair<Point2D, Int>> = mutableSetOf(start to 0),
        queue: ArrayDeque<Pair<LevelPoint, Int>> = ArrayDeque(listOf(start to 0 to 0))
    ): Int {
        require(queue.isNotEmpty()) { "Not found a path to end" }

        val (current, steps) = queue.pop()
        if (current == end to 0) return steps

        current.moves()
            .filterNot { it in discovered || it.second > portalJumps.size }
            .onEach { neighbor ->
                discovered.add(neighbor)
                queue.addLast(neighbor to steps + 1)
            }

        return bfsWithLevels(discovered, queue)
    }

    private fun Pair<Point2D, Int>.moves(): List<Pair<Point2D, Int>> {
        val neighbors = this.first
            .getNeighbours()
            .filter { it in free }
            .map { it to this.second }
        return portalJumps[this.first]?.let {
            val level = this.second + if (first.x in outwardX || first.y in outwardY) -1 else 1
            if (level >= 0)
                neighbors + (it to level)
            else
                neighbors
        } ?: neighbors
    }
}
