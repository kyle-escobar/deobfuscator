package io.rsbox.deobfuscator.transformer.multiplier

import java.math.BigInteger

object ModMath {

    private val SHIFT_64 = BigInteger.ONE.shiftLeft(64)
    private val SHIFT_32 = BigInteger.ONE.shiftLeft(32)

    fun Int.invert(): Int = this.toBigInteger().modInverse(SHIFT_32).toInt()
    fun Long.invert(): Long = this.toBigInteger().modInverse(SHIFT_64).toLong()

    fun Number.invert(): Number = when(this) {
        is Int -> this.invert()
        is Long -> this.invert()
        else -> throw IllegalArgumentException()
    }

    fun Int.isInvertible() = this and 1 == 1
    fun Long.isInvertible() = this.toInt().isInvertible()

    fun Number.isInvertible() = when(this) {
        is Int -> this.isInvertible()
        is Long -> this.isInvertible()
        else -> throw IllegalArgumentException()
    }

    fun gcd(vararg values: BigInteger): BigInteger {
        var result = values[0].gcd(values[1])
        for(i in 2 until values.size) {
            result = result.gcd(values[i])
        }
        return result
    }

    fun gcd(vararg values: Int): Int = gcd(*values.map { it.toBigInteger() }.toTypedArray()).toInt()
    fun gcd(vararg values: Long): Long = gcd(*values.map { it.toBigInteger() }.toTypedArray()).toLong()
}