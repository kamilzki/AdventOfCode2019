package com.ks.adventofcode.day

class ShipComputer(programValues: List<Int>) {
    private val memory = programValues.toMutableList()

    sealed class Mode {
        companion object {
            operator fun invoke(value: Int) = when (value) {
                0 -> Position
                1 -> Immediate
                else -> throw IllegalArgumentException("Wrong mode code for $value")
            }
        }

        object Position : Mode()
        object Immediate : Mode()
    }

    sealed class Optcode {
        companion object {
            operator fun invoke(value: Int) = when (value) {
                1 -> Execution.Add
                2 -> Execution.Mul
                3 -> Execution.Input
                4 -> Execution.Output
                5 -> Jump.JumpTrue
                6 -> Jump.JumpFalse
                7 -> Execution.LessThan
                8 -> Execution.Equals
                99 -> End
                else -> throw IllegalArgumentException("Wrong opcode code for $value")
            }
        }

        sealed class Execution : Optcode() {
            abstract fun executeOptcodeInstruction(pointer: Int, memory: MutableList<Int>, params: List<Mode>)
            abstract fun getPointerMove(): Int
            internal fun saveTo(pointer: Int) = pointer + getPointerMove() - 1

            object Add : Execution() {
                override fun executeOptcodeInstruction(pointer: Int, memory: MutableList<Int>, params: List<Mode>) {
                    val newValue = memory.getValue(pointer + 1, params[0]) +
                            memory.getValue(pointer + 2, params[1])
                    memory.setValue(saveTo(pointer), newValue)
                }

                override fun getPointerMove(): Int = 4
            }

            object Mul : Execution() {
                override fun executeOptcodeInstruction(pointer: Int, memory: MutableList<Int>, params: List<Mode>) {
                    val newValue = memory.getValue(pointer + 1, params[0]) *
                            memory.getValue(pointer + 2, params[1])
                    memory.setValue(saveTo(pointer), newValue)
                }

                override fun getPointerMove(): Int = 4
            }

            object Input : Execution() {
                override fun executeOptcodeInstruction(pointer: Int, memory: MutableList<Int>, params: List<Mode>) =
                    memory.setValue(saveTo(pointer), readValue())

                private fun readValue(): Int {
                    println("Write value: ")
                    return readLine()!!.toInt()
                }

                override fun getPointerMove(): Int = 2
            }

            object Output : Execution() {
                override fun executeOptcodeInstruction(pointer: Int, memory: MutableList<Int>, params: List<Mode>) {
                    println(memory.getValue(pointer + 1, params[0]))
                }

                override fun getPointerMove(): Int = 2
            }

            object LessThan : Execution() {
                override fun executeOptcodeInstruction(pointer: Int, memory: MutableList<Int>, params: List<Mode>) {
                    val first = memory.getValue(pointer + 1, params[0])
                    val second = memory.getValue(pointer + 2, params[1])
                    memory.setValue(saveTo(pointer), if (first < second) 1 else 0)
                }

                override fun getPointerMove(): Int = 4
            }

            object Equals : Execution() {
                override fun executeOptcodeInstruction(pointer: Int, memory: MutableList<Int>, params: List<Mode>) {
                    val first = memory.getValue(pointer + 1, params[0])
                    val second = memory.getValue(pointer + 2, params[1])
                    memory.setValue(LessThan.saveTo(pointer), if (first == second) 1 else 0)
                }

                override fun getPointerMove(): Int = 4
            }
        }

        sealed class Jump : Optcode() {
            abstract fun jumpInstruction(pointer: Int, memory: MutableList<Int>, params: List<Mode>): Int
            internal fun Int.getJumpPointer(cond: Boolean, pointer: Int) =
                if (cond) this else getPointerMove() + pointer

            private fun getPointerMove(): Int = 3

            object JumpTrue : Jump() {
                override fun jumpInstruction(pointer: Int, memory: MutableList<Int>, params: List<Mode>): Int {
                    val first = memory.getValue(pointer + 1, params[0])
                    val second = memory.getValue(pointer + 2, params[1])
                    return second.getJumpPointer(first != 0, pointer)
                }
            }

            object JumpFalse : Jump() {
                override fun jumpInstruction(pointer: Int, memory: MutableList<Int>, params: List<Mode>): Int {
                    val first = memory.getValue(pointer + 1, params[0])
                    val second = memory.getValue(pointer + 2, params[1])
                    return second.getJumpPointer(first == 0, pointer)
                }
            }
        }

        object End : Optcode()

        internal fun List<Int>.getValue(position: Int, mode: Mode): Int = when (mode) {
            Mode.Position -> this[this[position]]
            Mode.Immediate -> this[position]
        }

        fun MutableList<Int>.setValue(position: Int, value: Int) {
            this[this[position]] = value
        }
    }

    fun runProgram(): MutableList<Int> {
        solveSubprogram(0)
        return memory
    }

    private tailrec fun solveSubprogram(instructionPointer: Int) {
        val optcodeWithParams = memory[instructionPointer].toString().padStart(5, '0')
        val (params, optcode) = optcodeWithParams.splitToInstructionParts()

        if (optcode is Optcode.Jump) {
            solveSubprogram(optcode.jumpInstruction(instructionPointer, memory, params))
        } else if (optcode is Optcode.Execution) {
            optcode.executeOptcodeInstruction(instructionPointer, memory, params)
            solveSubprogram(instructionPointer + optcode.getPointerMove())
        }
    }

    private fun String.splitToInstructionParts() = let {
        val index = 3
        take(index).map { c -> Mode(c.toInt() - 48) }.reversed() to Optcode(substring(index).toInt())
    }
}
