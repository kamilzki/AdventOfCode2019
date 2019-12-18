package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import kotlin.math.absoluteValue
import kotlin.math.sign

class MoonSimulator(input: List<String>) {

    companion object {
        private const val beginFind = -1L
    }

    private val moons = input.map { moon -> moon.split(',').map { it.takeLastWhile { c -> c != '=' } } }
        .map { it.subList(0, it.size - 1) + it.last().dropLast(1) }
        .map { list -> list.map { it.toInt() } }
        .map { Moon(it[0], it[1], it[2]) }
    private val pairs = moons.map { moon -> moon to moons.filterNot { it == moon } }

    fun getTotalEnergy(steps: Int): Int {
        (0 until steps).forEach { _ ->
            simulateStep()
        }
        return moons.fold(0) { acc, moon -> acc + moon.getTotalEnergy() }
    }

    private fun simulateStep() {
        pairs.map { (m1, rest) ->
            m1 to m1.getVelocity(rest)
        }.onEach {  (m, v) ->
            m.changeState(v)
        }
    }

    private fun Moon.getVelocity(moons: List<Moon>): Velocity {
        return moons.map { m ->
            Velocity(
                getValueForDimension(x, m.x),
                getValueForDimension(y, m.y),
                getValueForDimension(z, m.z)
            )
        }.reduce { acc, velocity -> acc + velocity }
    }

    private fun getValueForDimension(value: Int, another: Int) = (another - value).sign

    fun whenComeToTheSameState(): Long {
        val startingX = moons.map { it.x }
        val startingY = moons.map { it.y }
        val startingZ = moons.map { it.z }
        val stepCount = 1L

        return next(startingX, startingY, startingZ, beginFind, beginFind, beginFind, stepCount)
    }

    private tailrec fun next(
        beginX: List<Int>,
        beginY: List<Int>,
        beginZ: List<Int>,
        cycleX: Long,
        cycleY: Long,
        cycleZ: Long,
        count: Long
    ): Long {
        if (cycleX != beginFind && cycleY != beginFind && cycleZ != beginFind)
            return lcm(cycleX, lcm(cycleY, cycleZ))

        val newCount = count + 1
        simulateStep()
        return next(
            beginX,
            beginY,
            beginZ,
            if (cycleX == beginFind && beginX == moons.map { it.x }) newCount else cycleX,
            if (cycleY == beginFind && beginY == moons.map { it.y }) newCount else cycleY,
            if (cycleZ == beginFind && beginZ == moons.map { it.z }) newCount else cycleZ,
            newCount
        )
    }

    private fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
    private fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b
}

data class Moon(var x: Int, var y: Int, var z: Int) {
    private var velocity = Velocity()

    fun changeState(v: Velocity) {
        velocity = Velocity(velocity.x + v.x, velocity.y + v.y, velocity.z + v.z)
        x += velocity.x
        y += velocity.y
        z += velocity.z
    }

    private fun getPotential() = x.absoluteValue + y.absoluteValue + z.absoluteValue
    private fun getKinetic() = velocity.getEnergy()
    fun getTotalEnergy() = getPotential() * getKinetic()
}

data class Velocity(val x: Int = 0, val y: Int = 0, val z: Int = 0) {
    operator fun plus(other: Velocity) = Velocity(x + other.x, y + other.y, z + other.z)
    fun getEnergy() = x.absoluteValue + y.absoluteValue + z.absoluteValue
}

fun main() {
    val input = Utils.readInputByLines("day12")

    println("Total energy after 1000 steps: " + MoonSimulator(input).getTotalEnergy(1000))
    println("Come to the same state after " + MoonSimulator(input).whenComeToTheSameState() + " steps")
}
