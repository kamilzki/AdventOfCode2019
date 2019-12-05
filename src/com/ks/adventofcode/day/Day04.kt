package com.ks.adventofcode.day

class VenusFuelDepotSolver(private val range: IntRange) {
    fun getNumberOfPossiblePassword() = range.map { it.toString() }
        .count { it.haveTwoAdjacentDigitsTheSame() && it.digitsNeverDecrease() }

    fun getNumberOfPossiblePasswordWithNewDetail() = range.map { it.toString() }
        .count { it.digitsNeverDecrease() && it.notPartOfLargerGroup() }

    private fun String.haveTwoAdjacentDigitsTheSame(): Boolean = this.zipWithNext { a, b -> a == b }.any { it }
    private fun String.digitsNeverDecrease(): Boolean = this.zipWithNext { a, b -> a.toInt() <= b.toInt() }.all { it }
    private fun String.notPartOfLargerGroup(): Boolean = this.groupBy { it }.any { it.value.size == 2}
}

fun main() {
    val input = "347312-805915"
    val (start, end) = input.split('-').map { it.toInt() }
    val solver = VenusFuelDepotSolver(IntRange(start, end))
    println("Possible passwords: " + solver.getNumberOfPossiblePassword())
    println("Possible passwords with details from Elf: " + solver.getNumberOfPossiblePasswordWithNewDetail())
}
