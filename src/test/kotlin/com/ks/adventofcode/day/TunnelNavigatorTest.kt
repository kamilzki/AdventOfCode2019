package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class TunnelNavigatorTest {

    @Nested
    inner class Part1 {

        @Test
        internal fun `should match example a`() {
            //given
            val input = """
            |#########
            |#b.A.@.a#
            |#########
            """.trimMargin()

            //when
            val result = TunnelNavigator(input.lines()).solve()

            //then
            assertEquals(8, result)
        }

        @Test
        internal fun `should match example b`() {
            //given
            val input = """
            |########################
            |#f.D.E.e.C.b.A.@.a.B.c.#
            |######################.#
            |#d.....................#
            |########################
            """.trimMargin()

            //when
            val result = TunnelNavigator(input.lines()).solve()

            //then
            assertEquals(86, result)
        }

        @Test
        internal fun `should match example c`() {
            //given
            val input = """
            |########################
            |#...............b.C.D.f#
            |#.######################
            |#.....@.a.B.c.d.A.e.F.g#
            |########################
            """.trimMargin()

            //when
            val result = TunnelNavigator(input.lines()).solve()

            //then
            assertEquals(132, result)
        }

        @Test
        internal fun `should match example d`() {
            //given
            val input = """
            |#################
            |#i.G..c...e..H.p#
            |########.########
            |#j.A..b...f..D.o#
            |########@########
            |#k.E..a...g..B.n#
            |########.########
            |#l.F..d...h..C.m#
            |#################
            """.trimMargin()

            //when
            val result = TunnelNavigator(input.lines()).solve()

            //then
            assertEquals(136, result)
        }

        @Test
        internal fun `should match example e`() {
            //given
            val input = """
            |########################
            |#@..............ac.GI.b#
            |###d#e#f################
            |###A#B#C################
            |###g#h#i################
            |########################
            """.trimMargin()

            //when
            val result = TunnelNavigator(input.lines()).solve()

            //then
            assertEquals(81, result)
        }

        @Test
        internal fun `should correctly solve part 1`() {
            //given
            val input = Utils.readInputByLines("day18")

            //when
            val result = TunnelNavigator(input).solve()

            //then
            assertEquals(3146, result)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `should match example a`() {
            //given
            val input = """
            |#############
            |#g#f.D#..h#l#
            |#F###e#E###.#
            |#dCba@#@BcIJ#
            |#############
            |#nK.L@#@G...#
            |#M###N#H###.#
            |#o#m..#i#jk.#
            |#############
            """.trimMargin()

            //when
            val result = TunnelNavigator(input.lines()).solve()

            //then
            assertEquals(72, result)
        }

        @Test
        internal fun `should correctly solve part 2`() {
            //given
            val input = Utils.readInputByLines("day18updated")

            //when
            val result = TunnelNavigator(input).solve()

            //then
            assertEquals(2194, result)
        }

    }
}
