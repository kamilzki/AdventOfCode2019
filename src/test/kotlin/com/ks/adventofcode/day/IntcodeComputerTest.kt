package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class IntcodeComputerTest {

    @Nested
    inner class Day02 {
        private val program = Utils.readInput("day02").split(',')
            .map { it.trimEnd().toLong() }

        @Test
        internal fun `should correctly solve part 1`() {
            //given
            val intcodeComputer = IntcodeComputer(backTo1202AlarmState(program))

            //when
            val result = intcodeComputer.runProgram()

            //then
            assertEquals(4090701, result)
        }

        @Test
        internal fun `should correctly solve part 2`() {
            //given
            val gravityAssistSolver = GravityAssistSolver(program)

            //when
            val result = gravityAssistSolver.solve(19690720L) ?: throw IllegalArgumentException("No solution!")

            //then
            assertEquals(6421, (100 * result.first + result.second))
        }
    }

    @Nested
    inner class Day05 {
        private val program = Utils.readInput("day05")
            .split(',')
            .map { it.trimEnd().toLong() }

        @Test
        fun `should correctly solve part 1`() {
            //given
            val intcodeComputer = IntcodeComputer(program)

            //when
            intcodeComputer.runProgram(listOf(1))
            val result = intcodeComputer.outputOfProgram.last()

            //then
            assertEquals(8332629, result)
        }

        @Test
        fun `should correctly solve part 2`() {
            //given
            val intcodeComputer = IntcodeComputer(program)

            //when
            intcodeComputer.runProgram(listOf(5))
            val result = intcodeComputer.outputOfProgram.last()

            //then
            assertEquals(8805067, result)
        }
    }

    @Nested
    inner class Day09 {
        private val program = Utils.readInput("day09")
            .split(',')
            .map { it.trimEnd().toLong() }

        @Test
        fun `should correctly solve part 1`() {
            //given
            val intcodeComputer = IntcodeComputer(program)

            //when
            intcodeComputer.runProgram(listOf(1))
            val result = intcodeComputer.outputOfProgram.last()

            //then
            assertEquals(4006117640, result)
        }

        @Test
        fun `should correctly solve part 2`() {
            //given
            val intcodeComputer = IntcodeComputer(program)

            //when
            intcodeComputer.runProgram(listOf(2))
            val result = intcodeComputer.outputOfProgram.last()

            //then
            assertEquals(88231, result)
        }
    }
}
