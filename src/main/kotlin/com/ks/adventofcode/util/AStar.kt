package com.ks.adventofcode.util

import kotlin.math.abs

interface AStarGrid {
    fun heuristic(from: Point2D, to: Point2D): Int
    fun getPossibleMoves(point: Point2D): List<Point2D>
}

class Grid(private val legalPositions: Set<Point2D>) : AStarGrid {
    override fun heuristic(from: Point2D, to: Point2D): Int = abs(from.x - to.x) + abs(from.y - to.y)

    override fun getPossibleMoves(point: Point2D): List<Point2D> {
        return point.getNeighbours().filter { legalPositions.contains(it) }
    }
}

class AStar(private val grid: AStarGrid) {

    fun search(
        from: Point2D,
        to: Point2D
    ): Int {
        return searchForNext(
            to,
            setOf(from),
            setOf(),
            mutableMapOf(from to 0),
            mutableMapOf(from to grid.heuristic(from, to))
        )
    }

    private tailrec fun searchForNext(
        to: Point2D,
        toCheck: Set<Point2D>,
        checked: Set<Point2D>,
        costFromStart: MutableMap<Point2D, Int>,
        estimatedCost: MutableMap<Point2D, Int>
    ): Int {
        val current = toCheck.minBy { estimatedCost.getValue(it) }
            ?: throw IllegalArgumentException("Not found path to $to")

        if (current == to)
            return estimatedCost.getValue(to)

        val unvisited = grid.getPossibleMoves(current)
            .filterNot { checked.contains(it) }
        unvisited.onEach { neighbour ->
            val cost = costFromStart.getValue(current) + 1
            if (cost < costFromStart.getOrDefault(neighbour, Int.MAX_VALUE)) {
                estimatedCost[neighbour] = cost + grid.heuristic(neighbour, to)
                costFromStart[neighbour] = cost
            }
        }

        return searchForNext(
            to,
            toCheck + unvisited - current,
            checked + current,
            costFromStart,
            estimatedCost)
    }
}
