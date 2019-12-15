package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import java.awt.geom.Point2D
import kotlin.math.atan2

class MonitoringStationSolver(private val map: List<List<String>>) {
    private val asteroids = map.mapIndexed { y, row ->
        row.mapIndexedNotNull { x, element -> if (element == ASTEROID) x to y to Asteroid(x, y) else null }
    }
            .flatten()
            .toMap()

    companion object {
        const val ASTEROID = "#"
    }

    fun findTheBestPlace(): Pair<Asteroid, Int> {
        val asteroidWithDirects = asteroids.values.map {
            it to it.getVisibleAsteroids(getAsteroidsWithout(it))
        }

        val (asteroid, set) = asteroidWithDirects.maxBy { it.second.count() }
                ?: error("No asteroids!")
        return asteroid to set.count()
    }

    fun getOrderOfAsteroidsVaporization(): List<Asteroid> {
        val (station, _) = findTheBestPlace()
        return station.getGroupedInformation(getAsteroidsWithout(station))
                .toSortedMap()
                .flatMap {
                    it.value.sortedBy { info -> info.distance }
                            .withIndex()
                }
                .sortedBy { it.index }
                .map { it.value.asteroid }

    }

    private fun getAsteroidsWithout(asteroid: Asteroid) =
            asteroids.values.filterNot { a -> a == asteroid }
}

data class Asteroid(val x: Int, val y: Int) {
    data class SpaceInformation(val asteroid: Asteroid, val degree: Double, val distance: Double)

    fun getVisibleAsteroids(asteroids: List<Asteroid>): List<SpaceInformation> {
        return getGroupedInformation(asteroids).map { it.value.maxBy { s -> s.distance }!! }
    }

    fun getGroupedInformation(asteroids: List<Asteroid>) = asteroids.map {
        SpaceInformation(
                it,
                getDegree(it),
                getDistance(it)
        )
    }.groupBy { it.degree }

    private fun getDegree(to: Asteroid): Double {
        val degree = Math.toDegrees(atan2(
                (to.y - this.y).toDouble(),
                (to.x - this.x).toDouble()
        ))
        return 90 + if (degree < -90) degree + 360.0 else degree
    }

    private fun getDistance(to: Asteroid) = Point2D.distance(
            this.x.toDouble(),
            this.y.toDouble(),
            to.x.toDouble(),
            to.y.toDouble()
    )
}


fun main() {
    val input = Utils.readInputByLines("day10")
            .map { it.chunked(1) }
    val stationSolver = MonitoringStationSolver(input)
    val (theBest, numOfVisible) = stationSolver.findTheBestPlace()
    println("The best $theBest can detect $numOfVisible")

    val asteroid200 = stationSolver.getOrderOfAsteroidsVaporization()[199]
    println("The 200th asteroid $asteroid200 - ${asteroid200.x * 100 + asteroid200.y}")
}
