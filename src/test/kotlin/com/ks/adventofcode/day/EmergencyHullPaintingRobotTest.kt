package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class EmergencyHullPaintingRobotTest {

    private fun getInput(file: String) = Utils.readInput(file)
            .split(',')
            .map {
                it.trimEnd()
                        .toLong()
            }

    @Nested
    inner class Part1 {
        @Test
        internal fun `should correctly solve part 1`() {
            //given
            val intcodeComputer = IntcodeComputer(getInput("day11"))

            //when
            val robot = EmergencyHullPaintingRobot(intcodeComputer)
            val result = robot.numberOfPanelsPainted(0)

            //then
            assertEquals(1883, result)
        }
    }

    @Nested
    inner class Part2 {
        @Test
        internal fun `should correctly solve part 2`() {
            //given
            val intcodeComputer = IntcodeComputer(getInput("day11"))

            //when
            val robot = EmergencyHullPaintingRobot(intcodeComputer)
            val result = robot.getPaintedRegistrationIdentifier()

            //then
            val expected = """
            |  ##  ###  #  #  ##  #  # ###  #### #  #   
            | #  # #  # #  # #  # #  # #  # #    #  #   
            | #  # #  # #  # #    #  # #  # ###  ####   
            | #### ###  #  # # ## #  # ###  #    #  #   
            | #  # #    #  # #  # #  # # #  #    #  #   
            | #  # #     ##   ###  ##  #  # #    #  #   
            """.trimMargin()
            assertEquals(expected, result)
        }
    }
}
