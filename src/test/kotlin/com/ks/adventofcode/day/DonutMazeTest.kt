package com.ks.adventofcode.day

import com.ks.adventofcode.util.Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class DonutMazeTest {

    @Nested
    inner class Part1 {
        @Test
        internal fun `should match example a`() {
            //given
            val plutoMaze = DonutMaze(example1)

            //when
            val result = plutoMaze.minimumSteps()

            //then
            assertEquals(23, result)
        }

        @Test
        internal fun `should match example b`() {
            //given
            val plutoMaze = DonutMaze(example2)

            //when
            val result = plutoMaze.minimumSteps()

            //then
            assertEquals(58, result)
        }

        @Test
        internal fun `should correctly solve part 1`() {
            //given
            val input = Utils.readInput("day20")
            val plutoMaze = DonutMaze(input)

            //when
            val result = plutoMaze.minimumSteps()

            //then
            assertEquals(570, result)
        }
    }

    @Nested
    inner class Part2 {

        @Test
        internal fun `should match example a`() {
            //given
            val plutoMaze = DonutMaze(example1)

            //when
            val result = plutoMaze.minimumStepsWithLevels()

            //then
            assertEquals(26, result)
        }

        @Test
        internal fun `should match example b`() {
            //given
            val plutoMaze = DonutMaze(example2)

            //when
            val toTest: () -> Unit = { plutoMaze.minimumStepsWithLevels() }

            //then
            assertThrows<IllegalArgumentException>(toTest)
        }

        @Test
        internal fun `should match example c`() {
            //given
            val plutoMaze = DonutMaze(example3)

            //when
            val result = plutoMaze.minimumStepsWithLevels()

            //then
            assertEquals(396, result)
        }

        @Test
        internal fun `should correctly solve part 2`() {
            //given
            val input = Utils.readInput("day20")
            val plutoMaze = DonutMaze(input)

            //when
            val result = plutoMaze.minimumStepsWithLevels()

            //then
            assertEquals(7056, result)
        }
    }

    private val example1 = """
                    |         A           
                    |         A           
                    |  #######.#########  
                    |  #######.........#  
                    |  #######.#######.#  
                    |  #######.#######.#  
                    |  #######.#######.#  
                    |  #####  B    ###.#  
                    |BC...##  C    ###.#  
                    |  ##.##       ###.#  
                    |  ##...DE  F  ###.#  
                    |  #####    G  ###.#  
                    |  #########.#####.#  
                    |DE..#######...###.#  
                    |  #.#########.###.#  
                    |FG..#########.....#  
                    |  ###########.#####  
                    |             Z       
                    |             Z       
                    |""".trimMargin()

    private val example2 = """
                    |                   A
                    |                   A
                    |  #################.#############
                    |  #.#...#...................#.#.#
                    |  #.#.#.###.###.###.#########.#.#
                    |  #.#.#.......#...#.....#.#.#...#
                    |  #.#########.###.#####.#.#.###.#
                    |  #.............#.#.....#.......#
                    |  ###.###########.###.#####.#.#.#
                    |  #.....#        A   C    #.#.#.#
                    |  #######        S   P    #####.#
                    |  #.#...#                 #......VT
                    |  #.#.#.#                 #.#####
                    |  #...#.#               YN....#.#
                    |  #.###.#                 #####.#
                    |DI....#.#                 #.....#
                    |  #####.#                 #.###.#
                    |ZZ......#               QG....#..AS
                    |  ###.###                 #######
                    |JO..#.#.#                 #.....#
                    |  #.#.#.#                 ###.#.#
                    |  #...#..DI             BU....#..LF
                    |  #####.#                 #.#####
                    |YN......#               VT..#....QG
                    |  #.###.#                 #.###.#
                    |  #.#...#                 #.....#
                    |  ###.###    J L     J    #.#.###
                    |  #.....#    O F     P    #.#...#
                    |  #.###.#####.#.#####.#####.###.#
                    |  #...#.#.#...#.....#.....#.#...#
                    |  #.#####.###.###.#.#.#########.#
                    |  #...#.#.....#...#.#.#.#.....#.#
                    |  #.###.#####.###.###.#.#.#######
                    |  #.#.........#...#.............#
                    |  #########.###.###.#############
                    |           B   J   C
                    |           U   P   P
                    |""".trimMargin()

    private val example3 = """
        |             Z L X W       C                 
        |             Z P Q B       K                 
        |  ###########.#.#.#.#######.###############  
        |  #...#.......#.#.......#.#.......#.#.#...#  
        |  ###.#.#.#.#.#.#.#.###.#.#.#######.#.#.###  
        |  #.#...#.#.#...#.#.#...#...#...#.#.......#  
        |  #.###.#######.###.###.#.###.###.#.#######  
        |  #...#.......#.#...#...#.............#...#  
        |  #.#########.#######.#.#######.#######.###  
        |  #...#.#    F       R I       Z    #.#.#.#  
        |  #.###.#    D       E C       H    #.#.#.#  
        |  #.#...#                           #...#.#  
        |  #.###.#                           #.###.#  
        |  #.#....OA                       WB..#.#..ZH
        |  #.###.#                           #.#.#.#  
        |CJ......#                           #.....#  
        |  #######                           #######  
        |  #.#....CK                         #......IC
        |  #.###.#                           #.###.#  
        |  #.....#                           #...#.#  
        |  ###.###                           #.#.#.#  
        |XF....#.#                         RF..#.#.#  
        |  #####.#                           #######  
        |  #......CJ                       NM..#...#  
        |  ###.#.#                           #.###.#  
        |RE....#.#                           #......RF
        |  ###.###        X   X       L      #.#.#.#  
        |  #.....#        F   Q       P      #.#.#.#  
        |  ###.###########.###.#######.#########.###  
        |  #.....#...#.....#.......#...#.....#.#...#  
        |  #####.#.###.#######.#######.###.###.#.#.#  
        |  #.......#.......#.#.#.#.#...#...#...#.#.#  
        |  #####.###.#####.#.#.#.#.###.###.#.###.###  
        |  #.......#.....#.#...#...............#...#  
        |  #############.#.#.###.###################  
        |               A O F   N                     
        |               A A D   M                     """.trimMargin()

}
