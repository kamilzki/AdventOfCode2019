package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import kotlin.math.absoluteValue

object FFT {
    private val basePattern = listOf(0, 1, 0, -1)
    private val getPatternValue = { k: Int -> basePattern[k % basePattern.size] }

    fun part2(input: String, phases: Int = 100): String {
        val values = prepareInput(input)
        val offset = input.take(7).toInt()
        val repeatedValues = (offset until 10_000 * values.size)
            .map { values[it % values.size] }
            .toIntArray()

        val result = calculateSecondHalfPhase(repeatedValues, phases)

        return result.take(8).joinToString("")
    }

    private fun calculateSecondHalfPhase(values: IntArray, phases: Int): IntArray {
        var current = values

        repeat(phases) {
            current = current.foldRightIndexed(IntArray(current.size) { 0 } to 0) { idx, el, (result, sum) ->
                val currentSum = sum + el
                result[idx] = currentSum.lastDigit()
                result to currentSum
            }.first
        }

        return current
    }

    fun calculate(input: String, phases: Int = 100): String {
        val values = prepareInput(input)
        return calculatePhase(values, phases).joinToString("")
            .take(8)
    }

    private fun prepareInput(input: String) = input.trimEnd().map { Character.getNumericValue(it.toInt()) }

    private tailrec fun calculatePhase(values: List<Int>, phases: Int, currentPhase: Int = 1): List<Int> {
        if (currentPhase > phases)
            return values

        return calculatePhase(calculateDigit(values), phases, currentPhase + 1)
    }

    private tailrec fun calculateDigit(
        values: List<Int>,
        toCalculate: Int = values.size,
        current: Int = 1,
        result: List<Int> = emptyList()
    ): List<Int> {
        if (current > toCalculate)
            return result

        val currentPattern = pattern(current).drop(1)
            .take(values.size)
            .toList()

        val afterPhase = values.zip(currentPattern)
            .map { (first, second) -> first * second }
            .sum()
            .lastDigit()
        return calculateDigit(values, toCalculate, current + 1, result + afterPhase)
    }

    private fun Int.lastDigit() = (this % 10).absoluteValue

    private fun pattern(current: Int): Sequence<Int> {
        var counter = 1
        return generateSequence(getPatternValue(0)) {
            val result = if (counter % current == 0) getPatternValue(counter / current) else it
            counter++
            result
        }
    }
}

fun main() {
    val input = Utils.readInput("day16")
    println("Part1 score: " + FFT.calculate(input))
    println("Part2 score: " + FFT.part2(input))
}
