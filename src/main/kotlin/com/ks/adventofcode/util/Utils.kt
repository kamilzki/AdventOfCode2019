package com.ks.adventofcode.util

import java.io.File

object Utils {
    fun readInput(dayCode: String): String = dayCode.toResource().readText()

    fun readInputByLines(dayCode: String): List<String> = File(dayCode.toURI()).readLines()

    fun readIntcodeInput(dayCode: String) = Utils.readInput(dayCode).split(',')
        .map { it.trimEnd().toLong() }

    private fun String.toResource() = Utils.javaClass.classLoader.getResource(this)
        ?: throw IllegalArgumentException("Cannot find Resource: $this")

    private fun String.toURI() = this.toResource().toURI()
}
