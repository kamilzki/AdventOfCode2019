package com.ks.adventofcode.day

class IntcodeComputer(programValues: List<Long>) {
    val outputOfProgram = mutableListOf<Long>()
    private var state: State = State.WAIT
    private val memory = programValues.toMutableList()
    private var pointer = 0
    private var relativeBase = 0L

    sealed class Mode {
        companion object {
            operator fun invoke(value: Int) = when (value) {
                0 -> Position
                1 -> Immediate
                2 -> Relative
                else -> throw IllegalArgumentException("Wrong mode code for $value")
            }
        }

        object Position : IntcodeComputer.Mode()
        object Immediate : IntcodeComputer.Mode()
        object Relative : IntcodeComputer.Mode()
    }

    sealed class State {
        object RUNNING : State()
        object WAIT : State()
        object HALT : State()
    }

    fun isHalt() = state is State.HALT

    fun runProgram(inputInstructions: List<Int> = emptyList()): Long {
        outputOfProgram.clear()
        state = State.RUNNING
        solveSubprogram(inputInstructions.toMutableList())
        return memory[0]
    }

    private tailrec fun solveSubprogram(remainingInputs: MutableList<Int>) {
        val optcodeWithParams = memory[pointer].toString().padStart(5, '0')
        val (params, optcode) = optcodeWithParams.splitToInstructionParts()
        pointer = when (optcode) {
            is Optcode.Add -> {
                val newValue = memory.getValue(pointer + 1, params[0]) +
                        memory.getValue(pointer + 2, params[1])

                memory.setValue(optcode.saveTo(pointer), newValue, params[2])
                pointer + optcode.getPointerMove()
            }
            is Optcode.Mul -> {
                val newValue = memory.getValue(pointer + 1, params[0]) *
                        memory.getValue(pointer + 2, params[1])
                memory.setValue(optcode.saveTo(pointer), newValue, params[2])
                pointer + optcode.getPointerMove()
            }
            is Optcode.Input -> {
                if (remainingInputs.isEmpty()) {
                    state = State.WAIT
                    return
                }

                val value = remainingInputs.removeAt(0)
                memory.setValue(optcode.saveTo(pointer), value.toLong(), params[0])
                pointer + optcode.getPointerMove()
            }
            is Optcode.Output -> {
                val result = memory.getValue(pointer + 1, params[0])
                outputOfProgram.add(result)
                pointer + optcode.getPointerMove()
            }
            is Optcode.JumpTrue -> {
                val first = memory.getValue(pointer + 1, params[0])
                val second = memory.getValue(pointer + 2, params[1])
                second.getJumpPointer(first != 0L, pointer, optcode)
            }
            is Optcode.JumpFalse -> {
                val first = memory.getValue(pointer + 1, params[0])
                val second = memory.getValue(pointer + 2, params[1])
                second.getJumpPointer(first == 0L, pointer, optcode)
            }
            is Optcode.LessThan -> {
                val first = memory.getValue(pointer + 1, params[0])
                val second = memory.getValue(pointer + 2, params[1])
                memory.setValue(optcode.saveTo(pointer), if (first < second) 1 else 0, params[2])
                pointer + optcode.getPointerMove()
            }
            is Optcode.Equals -> {
                val first = memory.getValue(pointer + 1, params[0])
                val second = memory.getValue(pointer + 2, params[1])
                memory.setValue(Optcode.LessThan.saveTo(pointer), if (first == second) 1 else 0, params[2])
                pointer + optcode.getPointerMove()
            }
            is Optcode.RelativeBaseOffset -> {
                val value = memory.getValue(pointer + 1, params[0])
                relativeBase += value
                pointer + optcode.getPointerMove()
            }
            is Optcode.End -> {
                state = State.HALT
                return
            }
        }
        solveSubprogram(remainingInputs)
    }

    private fun Long.getJumpPointer(cond: Boolean, pointer: Int, optcode: Optcode) =
                if (cond) this.toInt() else optcode.getPointerMove() + pointer

    private fun String.splitToInstructionParts() = let {
        val index = 3
        take(index).map { c -> Mode(c.toInt() - 48) }.reversed() to Optcode(substring(index).toInt())
    }

    private fun MutableList<Long>.getValue(position: Int, mode: Mode): Long = when (mode) {
        Mode.Position -> this.getOrElse(this.getOrElse(position) { 0 }.toInt()) { 0L }
        Mode.Immediate -> this[position]
        Mode.Relative -> {
            val readPosition = (this[position] + relativeBase).toInt()
            if (position >= this.size) {
                this.increaseMemory(position)
            } else if (readPosition >= this.size) {
                this.increaseMemory(readPosition)
            }
            this[readPosition]
        }
    }

    private fun MutableList<Long>.increaseMemory(to: Int) {
        this.addAll(List(to - this.size + 1) { 0L })
    }

    private fun MutableList<Long>.setValue(position: Int, value: Long, mode: Mode) {
        val posFromMemory = when (mode) {
            Mode.Position -> this[position]
            Mode.Relative -> {
                this[position] + relativeBase.toInt()
            }
            else -> throw IllegalArgumentException("Wrong mode for set value: $mode")
        }.toInt()

        if (posFromMemory >= this.size) {
            this.addAll(MutableList(posFromMemory - this.size + 1) { 0L })
        }

        this[posFromMemory] = value
    }
}

sealed class Optcode {
    abstract fun getPointerMove(): Int
    fun saveTo(pointer: Int) = pointer + getPointerMove() - 1

    companion object {
        operator fun invoke(value: Int): Optcode {
            return when (value) {
                1 -> Add
                2 -> Mul
                3 -> Input
                4 -> Output
                5 -> JumpTrue
                6 -> JumpFalse
                7 -> LessThan
                8 -> Equals
                9 -> RelativeBaseOffset
                99 -> End
                else -> throw IllegalArgumentException("Wrong opcode code for $value")
            }
        }
    }

    object Add : Optcode() {
        override fun getPointerMove(): Int = 4
    }
    object Mul : Optcode() {
        override fun getPointerMove(): Int = 4
    }
    object Input : Optcode() {
        override fun getPointerMove(): Int = 2
    }
    object Output : Optcode() {
        override fun getPointerMove(): Int = 2
    }
    object JumpTrue : Optcode() {
        override fun getPointerMove(): Int = 3
    }
    object JumpFalse : Optcode() {
        override fun getPointerMove(): Int = 3
    }
    object LessThan : Optcode() {
        override fun getPointerMove(): Int = 4
    }
    object Equals : Optcode() {
        override fun getPointerMove(): Int = 4
    }
    object RelativeBaseOffset : Optcode() {
        override fun getPointerMove(): Int = 2
    }
    object End : Optcode() {
        override fun getPointerMove(): Int = 0
    }
}
