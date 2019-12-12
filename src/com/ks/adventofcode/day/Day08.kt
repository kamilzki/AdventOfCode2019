package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils

class SpaceImageFormatDecoder(imageData: List<Int>, private val wide: Int, tall: Int) {
    companion object {
        const val TRANSPARENT = 2
    }

    private val layers = imageData.chunked(wide * tall)

    fun foundTheFewest(fewestNumber: Int) =
            layers.minBy { it.count { num -> num == fewestNumber } } ?: error("Sth go wrong with minBy")

    fun decodeImage(): String {
        val decodedImage = layers.drop(1).fold(layers.first().toMutableList()) { acc, layer ->
            layer.forEachIndexed { index, pixel ->
                if (acc[index] == TRANSPARENT) {
                    acc[index] = pixel
                }
            }
            acc
        }

        return decodedImage.joinToString("")
                .chunked(wide)
                .joinToString("\n")
                .replace('0', ' ')
    }
}

fun main() {
    val imageData = Utils.readInput("day08")
            .chunked(1)
            .dropLast(1)
            .map { it.toInt() }

    val imageDecoder = SpaceImageFormatDecoder(imageData, 25, 6)
    val theFewestLayerWithZeros = imageDecoder.foundTheFewest(0)
    println("1 digits x 2 digits = " + theFewestLayerWithZeros.count { it == 1 } * theFewestLayerWithZeros.count { it == 2 })
    println("Decoded image: \n" + imageDecoder.decodeImage())
}
