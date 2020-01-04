package com.ks.adventofcode.day

import com.ks.adventofcode.day.SpaceShuffle.ShuffleTechnique.*
import java.util.*

class SpaceShuffle(size: Int = 10007) {
    companion object {
        const val cut = "cut "
        const val increment = "deal with increment "
        const val newStackInstruction = "deal into new stack"
    }

    private val cardsRange = 0 until size

    fun shuffle(input: List<String>): List<Int> {

        tailrec fun nextShuffle(deckState: List<Int>, queue: ArrayDeque<ShuffleTechnique>): List<Int> {
            if (queue.isEmpty())
                return deckState

            return nextShuffle(queue.pop().shuffle(deckState), queue)
        }

        return nextShuffle(cardsRange.toList(), ArrayDeque(input.prepareInstructions()))
    }

    private fun List<String>.prepareInstructions() = this.map { it.parseInstruction() }

    private fun String.parseInstruction(): ShuffleTechnique {
        return when {
            this == newStackInstruction -> NewStack
            this.startsWith(cut) -> Cut(this.drop(cut.length).toInt())
            this.startsWith(increment) -> Increment(this.drop(increment.length).toInt())
            else -> throw IllegalArgumentException("Unknown instruction: $this")
        }
    }

    sealed class ShuffleTechnique {
        abstract fun shuffle(deck: List<Int>): List<Int>

        object NewStack : ShuffleTechnique() {
            override fun shuffle(deck: List<Int>): List<Int> = deck.reversed()
        }

        data class Cut(val value: Int) : ShuffleTechnique() {
            override fun shuffle(deck: List<Int>): List<Int> {
                val firstCut = if (value < 0) deck.size + value else value
                return deck.subList(firstCut, deck.size) + deck.subList(0, firstCut)
            }
        }

        data class Increment(val value: Int) : ShuffleTechnique() {
            override fun shuffle(deck: List<Int>): List<Int> {
                val shuffled = IntArray(deck.size)
                var pos = 0
                deck.forEach {
                    shuffled[pos] = it
                    pos = (pos + value) % deck.size
                    pos += if (deck.size % value == 0) 1 else 0
                }
                return shuffled.toList()
            }
        }
    }
}
