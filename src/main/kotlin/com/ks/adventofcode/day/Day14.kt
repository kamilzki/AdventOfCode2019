package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils

class Nanofactory(input: List<String>) {
    companion object {
        const val rawMaterial = "ORE"
    }

    private val reactions = input.map {
        val (left, right) = it.split(" => ")
        val components = left.split(", ").map { c ->
            val (amount, type) = getComponent(c)
            Chemical(type, amount)
        }.toSet()
        val result = getComponent(right)
        result.second to Reaction(Chemical(result.second, result.first), components)
    }.toMap()

    fun countRawMaterial(quantity: Long, chemicalType: String = "FUEL"): Long {
        return count(mutableMapOf(chemicalType to quantity))
    }

    fun maximumFuelToProduce(maxRawMaterial: Long = 1_000_000_000_000): Long {
        var start = 0L
        var stop = maxRawMaterial
        var max = Long.MIN_VALUE

        do {
            if ((start + 1) == stop)
                start = stop

            val position = start + (stop - start) / 2
            val result = countRawMaterial(position, "FUEL")
            if (result <= maxRawMaterial) {
                if (max < position)
                    max = position
                start = position
            } else
                stop = position

        } while (start < stop)

        return max
    }

    private tailrec fun count(chemicals: MutableMap<String, Long>): Long {
        val (key, necessaryKey) = chemicals.entries.firstOrNull { (key, n) -> key != rawMaterial && n > 0 }
            ?: return chemicals[rawMaterial] ?: 0
        val (cKey, componentsKey) = checkNotNull(reactions[key]) { "null key=$key" }
        val reactionTimes = (necessaryKey + cKey.amount - 1) / cKey.amount
        val result = necessaryKey - cKey.amount * reactionTimes
        chemicals[key] = result

        componentsKey.onEach { (type, necessary)  ->
            chemicals[type] = chemicals.getOrElse(type) { 0 } + reactionTimes * necessary
        }

        return count(chemicals)
    }

    private fun getComponent(c: String): Pair<Int, String> {
        val (num, name) = c.split(" ")
        return num.toInt() to name
    }
}
    data class Reaction(val result: Chemical, val components: Set<Chemical>)

    data class Chemical(val type: String, val amount: Int)

    fun main() {
        val input = Utils.readInputByLines("day14")
        val nanofactory = Nanofactory(input)
        println("ORM to produce 1 FUEL:" + nanofactory.countRawMaterial(1L, "FUEL"))
        println("Max fuel to produce with 1 trillion ORM: " + nanofactory.maximumFuelToProduce())
    }
