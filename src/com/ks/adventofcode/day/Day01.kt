package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils

object FuelCalculator {

    fun calculateFuelForSpacecraft(massModules: List<Int>) = massModules.sumBy { calculateFuel(it) }

    fun calculateFuelConsiderFuel(massModules: List<Int>): Int = massModules.sumBy {
        val fuelForMass = calculateFuel(it)
        fuelForMass + calculateFuelForFuel(fuelForMass)
    }

    private fun calculateFuel(mass: Int) = mass / 3 - 2

    private fun calculateFuelForFuel(fuel: Int): Int {
        val fuelForFuel = calculateFuel(fuel)
        return if (fuelForFuel <= 0) 0 else fuelForFuel + calculateFuelForFuel(fuelForFuel)
    }
}

fun main() {
    val massModules = Utils.readInputByLines("day01").map { it.toInt() }
    println("Part 1: ${FuelCalculator.calculateFuelForSpacecraft(massModules)}")
    println("Part 2: ${FuelCalculator.calculateFuelConsiderFuel(massModules)}")
}
