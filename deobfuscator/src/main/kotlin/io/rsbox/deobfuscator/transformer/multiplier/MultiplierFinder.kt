package io.rsbox.deobfuscator.transformer.multiplier

import com.google.common.collect.MultimapBuilder
import io.rsbox.deobfuscator.Transformer
import io.rsbox.deobfuscator.asm.ClassPool
import io.rsbox.deobfuscator.transformer.multiplier.ModMath.invert
import io.rsbox.deobfuscator.transformer.multiplier.ModMath.isInvertible
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.analysis.*

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

        println()
    }

    class Multipliers {
        val muls = MultimapBuilder.hashKeys().arrayListValues().build<String, Mul>()
        val decoders = hashMapOf<String, Number>()
        val fieldMuls = hashSetOf<FieldInfo>()
    }

    sealed class Mul(val number: Number) {
        class Encoder(number: Number) : Mul(number)
        class Decoder(number: Number) : Mul(number)
    }

    data class FieldInfo(val setter: String, val getter: String, val number: Number)

    data class FieldMul(val field: MulValue, val number: MulValue) {
        companion object {
            fun fromMulValue(value: MulValue.Two): FieldMul? {
                var mul: MulValue? = null
                var field: MulValue? = null
                if(value.one.isMulNumber() && value.two.isFieldGetter()) {
                    mul = value.one
                    field = value.two
                } else if(value.one.isFieldGetter() && value.two.isMulNumber()) {
                    mul = value.two
                    field = value.one
                }
                if(mul != null && field != null && (mul.ldcNumber.isInvertible() && mul.ldcNumber.invert() != mul.ldcNumber)) {
                    return FieldMul(field, mul)
                }
                return null
            }
        }
    }

    open class MulValue(val value: SourceValue) : Value {

        override fun getSize(): Int = value.size
        override fun hashCode(): Int = value.hashCode()
        override fun equals(other: Any?): Boolean = other is MulValue && value == other.value

        /*
         * Utility functions
         */
        fun isMulNumber() = value.insn.let { it != null && it is LdcInsnNode && (it.cst is Int || it.cst is Long) }
        fun isFieldGetter() = value.insn.let { it != null && it.opcode in listOf(GETSTATIC, GETFIELD) }
        fun isMultiply() = value.insn.let { it != null && it.opcode in listOf(IMUL, LMUL) }
        fun isAddition() = value.insn.let { it != null && it.opcode in listOf(IADD, LADD, ISUB, LSUB) }
        val ldcNumber get() = value.insns.single().let { it as LdcInsnNode; it.cst as Number }
        val fieldIdentifier get() = value.insns.single().let { it as FieldInsnNode; it.fieldIdentifier }

        class Two(value: SourceValue, val one: MulValue, val two: MulValue) : MulValue(value)
    }

    class MulInterpreter(private val multipliers: Multipliers) : Interpreter<MulValue>(ASM9) {

        private val src = SourceInterpreter()

        private val ldcs = hashSetOf<MulValue>()
        private val puts = hashMapOf<MulValue, MulValue>()

        override fun newValue(p0: Type?): MulValue? {
            return src.newValue(p0)?.let { MulValue(it) }
        }

        override fun newOperation(p0: AbstractInsnNode): MulValue {
            return MulValue(src.newOperation(p0))
        }

        override fun merge(p0: MulValue, p1: MulValue): MulValue {
            return MulValue(src.merge(p0.value, p1.value))
        }

        override fun returnOperation(p0: AbstractInsnNode?, p1: MulValue?, p2: MulValue?) {}

        override fun naryOperation(p0: AbstractInsnNode, p1: MutableList<out MulValue>): MulValue {
            return MulValue(src.naryOperation(p0, p1.map { it.value }))
        }

        override fun ternaryOperation(p0: AbstractInsnNode, p1: MulValue, p2: MulValue, p3: MulValue): MulValue {
            return MulValue(src.ternaryOperation(p0, p1.value, p2.value, p3.value))
        }

        override fun binaryOperation(insn: AbstractInsnNode, value1: MulValue, value2: MulValue) =
            MulValue.Two(src.binaryOperation(insn, value1.value, value2.value), value1, value2).also {
                when(insn.opcode) {
                    PUTFIELD -> setField(it, value2)
                }
            }

        override fun unaryOperation(insn: AbstractInsnNode, value: MulValue) =
            MulValue(src.unaryOperation(insn, value.value)).also {
                if(insn.opcode == PUTSTATIC) setField(it, value)
            }

        override fun copyOperation(insn: AbstractInsnNode, value: MulValue): MulValue {
            return when(insn.opcode) {
                DUP, DUP2, DUP2_X1, DUP_X1 -> value
                else -> MulValue(src.copyOperation(insn, value.value))
            }
        }

        private fun setField(put: MulValue, value: MulValue) {
            puts[value] = put
            if(!value.isMulNumber() && value is MulValue.Two) {
                distribute(put.value.insn as FieldInsnNode, value)
            }
        }

        private fun distribute(put: FieldInsnNode, value: MulValue.Two) {
            if(value.isMulNumber()) {
                val fieldMul = FieldMul.fromMulValue(value)
                if(fieldMul != null && ldcs.add(fieldMul.number)) {
                    multipliers.muls.remove(fieldMul.field.fieldIdentifier, Mul.Decoder(fieldMul.number.ldcNumber))
                    multipliers.fieldMuls.add(FieldInfo(put.fieldIdentifier, fieldMul.field.fieldIdentifier, fieldMul.number.ldcNumber))
                }
            }
        }
    }
}

val SourceValue.insn: AbstractInsnNode? get() = insns.singleOrNull()
val FieldInsnNode.fieldIdentifier: String get() = "$owner.$name"