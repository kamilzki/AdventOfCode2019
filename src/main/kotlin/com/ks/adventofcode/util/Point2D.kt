package com.ks.adventofcode.util

data class Point2D(val x: Int, val y: Int) {
    fun up(): Point2D = copy(y = y + 1)
    fun down(): Point2D = copy(y = y - 1)
    fun left(): Point2D = copy(x = x - 1)
    fun right(): Point2D = copy(x = x + 1)

    fun getNeighbours() = setOf(up(), right(), down(), left())
}
