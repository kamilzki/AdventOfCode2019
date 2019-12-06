package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils

class OrbitMapSolver(input: List<String>) {
    companion object {
        const val COM = "COM"
    }
    private val objectWithOrbits = input.map {
        val objects = it.split(')')
        objects[0] to objects[1]
    }.groupBy { it.first }
        .map { it.key to it.value.map { (_, second) -> second } }
        .toMap()

    private val getCenterOfMass = objectWithOrbits[COM] ?: error("Not found getCenterOfMass!")

    fun countAllOrbits() = countOrbitsForNext(getCenterOfMass)

    private fun countOrbitsForNext(list: List<String>, depth: Int = 1): Int {
        return list.map {
            val ownOrbits = objectWithOrbits[it]
            if (ownOrbits != null) {
                countOrbitsForNext(ownOrbits, depth + 1)
            } else 0
        }.sum() + list.size * depth
    }

    fun requiredTransfers(start: String, stop: String): Int {
        val pathStart = findPath(start, getCenterOfMass)!!
        val pathStop = findPath(stop, getCenterOfMass)!!
        val intersectionObject = pathStart.intersect(pathStop).last()
        return pathStart.getDistanceFrom(intersectionObject) + pathStop.getDistanceFrom(intersectionObject)
    }

    private fun List<String>.getDistanceFrom(obj: String): Int = this.size - this.indexOf(obj) - 1

    private fun findPath(toFind: String, current: List<String>, path: List<String> = listOf(COM)): List<String>? {
        if (current.contains(toFind))
            return path

        return current.mapNotNull {
            val ownOrbits = objectWithOrbits[it]
            if (ownOrbits != null)
                findPath(toFind, ownOrbits, path + it)
            else
                null
        }.firstOrNull()
    }
}

fun main() {
    val orbitMapSolver = OrbitMapSolver(Utils.readInputByLines("day06"))
    println("All orbits: " + orbitMapSolver.countAllOrbits())
    println("Number of transfers required: " + orbitMapSolver.requiredTransfers("YOU", "SAN"))
}

