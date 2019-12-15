package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class MonitoringStationSolverTest {

    private fun getMapOfInput(file: String) = Utils.readInputByLines(file)
            .map { it.chunked(1) }

    @Nested
    inner class Part1 {
        @Test
        internal fun `should match example a`() {
            //given
            val map = getMapOfInput("day10a")

            //when
            val solver = MonitoringStationSolver(map)
            val (theBestAsteroid, direct) = solver.findTheBestPlace()

            //then
            assertEquals(3 to 4, theBestAsteroid.x to theBestAsteroid.y)
            assertEquals(8, direct)
        }

        @Test
        internal fun `should match example b`() {
            //given
            val map = getMapOfInput("day10b")

            //when
            val solver = MonitoringStationSolver(map)
            val (theBestAsteroid, direct) = solver.findTheBestPlace()

            //then
            assertEquals(5 to 8, theBestAsteroid.x to theBestAsteroid.y)
            assertEquals(33, direct)
        }

        @Test
        internal fun `should match example c`() {
            //given
            val map = getMapOfInput("day10c")

            //when
            val solver = MonitoringStationSolver(map)
            val (theBestAsteroid, direct) = solver.findTheBestPlace()

            //then
            assertEquals(1 to 2, theBestAsteroid.x to theBestAsteroid.y)
            assertEquals(35, direct)
        }

        @Test
        internal fun `should match example d`() {
            //given
            val map = getMapOfInput("day10d")

            //when
            val solver = MonitoringStationSolver(map)
            val (theBestAsteroid, direct) = solver.findTheBestPlace()

            //then
            assertEquals(6 to 3, theBestAsteroid.x to theBestAsteroid.y)
            assertEquals(41, direct)
        }

        @Test
        internal fun `should match example e`() {
            //given
            val map = getMapOfInput("day10e")

            //when
            val solver = MonitoringStationSolver(map)
            val (theBestAsteroid, direct) = solver.findTheBestPlace()

            //then
            assertEquals(11 to 13, theBestAsteroid.x to theBestAsteroid.y)
            assertEquals(210, direct)
        }

        @Test
        internal fun `should correctly solve part 1`() {
            //given
            val map = getMapOfInput("day10")

            //when
            val solver = MonitoringStationSolver(map)
            val (theBestAsteroid, direct) = solver.findTheBestPlace()

            //then
            assertEquals(27 to 19, theBestAsteroid.x to theBestAsteroid.y)
            assertEquals(314, direct)
        }
    }

    @Nested
    inner class Part2 {
        @Test
        internal fun `should match example`() {
            //given
            val map = getMapOfInput("day10e")

            //when
            val solver = MonitoringStationSolver(map)
            val order = solver.getOrderOfAsteroidsVaporization()

            //then
            assertEquals(Asteroid(11,12), order[0])
            assertEquals(Asteroid(12, 1), order[1])
            assertEquals(Asteroid(12, 2), order[2])
            assertEquals(Asteroid(12, 8), order[9])
            assertEquals(Asteroid(16, 0), order[19])
            assertEquals(Asteroid(16, 9), order[49])
            assertEquals(Asteroid(10, 16), order[99])
            assertEquals(Asteroid(9, 6), order[198])
            assertEquals(Asteroid(8, 2), order[199])
            assertEquals(Asteroid(10, 9), order[200])
            assertEquals(Asteroid(11, 1), order[298])
        }

        @Test
        internal fun `should correctly solve part 2`() {
            //given
            val map = getMapOfInput("day10")

            //when
            val solver = MonitoringStationSolver(map)
            val order = solver.getOrderOfAsteroidsVaporization()

            //then
            assertEquals(Asteroid(15, 13), order[199])
        }

    }

}
