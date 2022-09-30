package io.rsbox.deobfuscator.asm

import io.rsbox.deobfuscator.asm.trans.ValueFolding
import java.io.File

object ClassPoolTest {

    @JvmStatic
    fun main(args: Array<String>) {
        println("Testing class pool.")

        val pool = ClassPool()
        pool.addJarClasses(File("gamepack.jar"))
        pool.build()
        println("Successfully loaded ${pool.classes.size} into class pool.")

        val cls = pool.findClass("client")!!
        val method = cls.getMethod("init", "()V")!!

        pool.commit()

        method.virtualMethods.forEach { println(it.identifier) }

        println("Saving to out jar.")
        pool.saveToJar(File("gamepack-out.jar"))
    }
}