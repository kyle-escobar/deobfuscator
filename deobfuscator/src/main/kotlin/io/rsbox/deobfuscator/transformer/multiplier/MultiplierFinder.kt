package io.rsbox.deobfuscator.transformer.multiplier

import io.rsbox.deobfuscator.Transformer
import io.rsbox.deobfuscator.asm.ClassPool
import org.objectweb.asm.Opcodes.ASM9
import org.objectweb.asm.Type
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.Interpreter
import org.objectweb.asm.tree.analysis.SourceInterpreter
import org.objectweb.asm.tree.analysis.SourceValue
import org.objectweb.asm.tree.analysis.Value

class MultiplierFinder : Transformer {

    private val multipliers = Multipliers()
    private val interpreter = MulInterpreter(multipliers)
    private val analyzer = Analyzer(interpreter)

    override fun run(pool: ClassPool) {
        pool.classes.forEach { cls ->
            cls.methods.forEach { method ->
                analyzer.analyze(cls.name, method)
            }
        }
        println("Finished")
    }

    class Multipliers {

    }

    open class MulValue(val src: SourceValue) : Value {

        override fun getSize(): Int = src.size

        override fun hashCode(): Int = src.hashCode()

        override fun equals(other: Any?): Boolean = other is MulValue && other.src == src

        class Two(src: SourceValue, val value1: MulValue, val value2: MulValue) : MulValue(src)
    }

    class MulInterpreter(private val multipliers: Multipliers) : Interpreter<MulValue>(ASM9) {

        private val src = SourceInterpreter()

        override fun newValue(type: Type?) = src.newValue(type)?.let { MulValue(it) }

        override fun newOperation(insn: AbstractInsnNode) = MulValue(src.newOperation(insn))

        override fun merge(value1: MulValue, value2: MulValue) = MulValue(src.merge(value1.src, value2.src))

        override fun returnOperation(insn: AbstractInsnNode, value: MulValue, expected: MulValue) {}

        override fun naryOperation(insn: AbstractInsnNode, values: MutableList<out MulValue>) = MulValue(src.naryOperation(insn, values.map { it.src }))

        override fun ternaryOperation(insn: AbstractInsnNode, value1: MulValue, value2: MulValue, value3: MulValue) = MulValue(src.ternaryOperation(insn, value1.src, value2.src, value3.src))

        override fun binaryOperation(insn: AbstractInsnNode, value1: MulValue, value2: MulValue) = MulValue(src.binaryOperation(insn, value1.src, value2.src))

        override fun unaryOperation(insn: AbstractInsnNode, value: MulValue) = MulValue(src.unaryOperation(insn, value.src))

        override fun copyOperation(insn: AbstractInsnNode, value: MulValue) = MulValue(src.copyOperation(insn, value.src))

    }
}