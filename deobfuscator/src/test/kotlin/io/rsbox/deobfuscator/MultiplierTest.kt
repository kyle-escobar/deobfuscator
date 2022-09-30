package io.rsbox.deobfuscator

import io.rsbox.deobfuscator.asm.ClassPool
import io.rsbox.deobfuscator.transformer.multiplier.MultiplierFinder

object MultiplierTest {

    @JvmStatic
    fun main(args: Array<String>) {
        val testClassBytes = MultiplierTest::class.java.getResourceAsStream("/MultiplierTestClass.class")!!.readAllBytes()
        val pool = ClassPool()

        pool.addClass(testClassBytes)
        pool.build()
        println("Successfully loaded test multiplier class.")

        println("Running Multiplier finder transformer.")
        val transformer = MultiplierFinder()
        transformer.run(pool)

        println("Finsihed!")
    }
}