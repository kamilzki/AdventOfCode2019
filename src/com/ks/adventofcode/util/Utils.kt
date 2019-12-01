package com.ks.adventofcode.util

import java.io.File

object Utils {
    fun readInput(dayCode: String): List<String> = File(dayCode.toURI()).readLines()

    private fun String.toURI() = Utils.javaClass.classLoader.getResource(this)?.toURI()
        ?: throw IllegalArgumentException("Cannot find Resource: $this")
}
