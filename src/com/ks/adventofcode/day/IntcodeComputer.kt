package com.ks.adventofcode.day

class IntcodeComputer(programValues: List<Int>) {
    var state: State = State.WAIT
    val outputOfProgram = mutableListOf<Int>()
    private val memory = programValues.toMutableList()
    private var instructionPointer = 0

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

    sealed class State {
        object RUNNING : State()
        object WAIT : State()
        object HALT : State()
    }

    sealed class Optcode {
        companion object {
            operator fun invoke(value: Int): Optcode {
                return when (value) {
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
        }

        sealed class Execution : Optcode() {
            abstract fun executeOptcodeInstruction(
                pointer: Int,
                memory: MutableList<Int>,
                params: List<Mode>,
                inputInstructions: MutableList<Int>
            ): Int?

            abstract fun getPointerMove(): Int
            internal fun saveTo(pointer: Int) = pointer + getPointerMove() - 1

            object Add : Execution() {
                override fun executeOptcodeInstruction(
                    pointer: Int,
                    memory: MutableList<Int>,
                    params: List<Mode>,
                    inputInstructions: MutableList<Int>
                ): Int? {
                    val newValue = memory.getValue(pointer + 1, params[0]) +
                            memory.getValue(pointer + 2, params[1])
                    memory.setValue(saveTo(pointer), newValue)
                    return null
                }

                override fun getPointerMove(): Int = 4
            }

            object Mul : Execution() {
                override fun executeOptcodeInstruction(
                    pointer: Int,
                    memory: MutableList<Int>,
                    params: List<Mode>,
                    inputInstructions: MutableList<Int>
                ): Int? {
                    val newValue = memory.getValue(pointer + 1, params[0]) *
                            memory.getValue(pointer + 2, params[1])
                    memory.setValue(saveTo(pointer), newValue)
                    return null
                }

                override fun getPointerMove(): Int = 4
            }

            object Input : Execution() {
                override fun executeOptcodeInstruction(
                    pointer: Int,
                    memory: MutableList<Int>,
                    params: List<Mode>,
                    inputInstructions: MutableList<Int>
                ): Int? {
                    val value = inputInstructions.removeAt(0)
                    memory.setValue(saveTo(pointer), value)
                    return null
                }

                override fun getPointerMove(): Int = 2
            }

            object Output : Execution() {
                override fun executeOptcodeInstruction(
                    pointer: Int,
                    memory: MutableList<Int>,
                    params: List<Mode>,
                    inputInstructions: MutableList<Int>
                ): Int? {
                    return memory.getValue(pointer + 1, params[0])
                }

                override fun getPointerMove(): Int = 2
            }

            object LessThan : Execution() {
                override fun executeOptcodeInstruction(
                    pointer: Int,
                    memory: MutableList<Int>,
                    params: List<Mode>,
                    inputInstructions: MutableList<Int>
                ): Int? {
                    val first = memory.getValue(pointer + 1, params[0])
                    val second = memory.getValue(pointer + 2, params[1])
                    memory.setValue(saveTo(pointer), if (first < second) 1 else 0)
                    return null
                }

                override fun getPointerMove(): Int = 4
            }

            object Equals : Execution() {
                override fun executeOptcodeInstruction(
                    pointer: Int,
                    memory: MutableList<Int>,
                    params: List<Mode>,
                    inputInstructions: MutableList<Int>
                ): Int? {
                    val first = memory.getValue(pointer + 1, params[0])
                    val second = memory.getValue(pointer + 2, params[1])
                    memory.setValue(LessThan.saveTo(pointer), if (first == second) 1 else 0)
                    return null
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

    fun runProgram(inputInstructions: List<Int> = emptyList()): Int {
        outputOfProgram.clear()
        state = State.RUNNING
        solveSubprogram(inputInstructions.toMutableList())
        return memory[0]
    }

    private tailrec fun solveSubprogram(remainingInputs: MutableList<Int>) {
        val optcodeWithParams = memory[instructionPointer].toString().padStart(5, '0')
        val (params, optcode) = optcodeWithParams.splitToInstructionParts()
        when (optcode) {
            is Optcode.Jump -> {
                instructionPointer = optcode.jumpInstruction(instructionPointer, memory, params)
                solveSubprogram(remainingInputs)
            }
            is Optcode.Execution -> {
                if (optcode is Optcode.Execution.Input && remainingInputs.isEmpty()) {
                    state = State.WAIT
                    return
                } else {
                    val result =
                        optcode.executeOptcodeInstruction(instructionPointer, memory, params, remainingInputs)
                    if (result != null) {
                        outputOfProgram.add(result)
                    }
                    instructionPointer += optcode.getPointerMove()
                    solveSubprogram(remainingInputs)
                }
            }
            is Optcode.End -> state = State.HALT
        }
    }

    private fun String.splitToInstructionParts() = let {
        val index = 3
        take(index).map { c -> Mode(c.toInt() - 48) }.reversed() to Optcode(substring(index).toInt())
    }
}
