package io.rsbox.deobfuscator.asm.file

import io.rsbox.deobfuscator.asm.ClassPool
import io.rsbox.deobfuscator.asm.isIgnored
import io.rsbox.deobfuscator.asm.reflect.ClassInfo
import io.rsbox.deobfuscator.asm.toByteArray
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStream

class ClassNodeFileLoader(private val pool: ClassPool) : ClassFileLoader() {

    private val outputStreamMap = hashMapOf<String, ByteArrayOutputStream>()

    override fun loadClass(name: String): ClassInfo {
        val node = pool.findClass(name)
        if(node != null) {
            val inputStream = node.toByteArray().inputStream()
            return loadClassFromStream(File("$name.class"), inputStream)
        }
        return super.loadClass(name)
    }

    override fun outputStreamFor(name: String): OutputStream {
        return outputStreamMap.getOrPut(name) { ByteArrayOutputStream() }
    }

    override fun outputStreamFor(info: ClassInfo): OutputStream {
        return outputStreamFor(info.name())
    }

    override fun done() {
        outputStreamMap.forEach { (className, outputStream) ->
            val node = pool.findClass(className) ?: throw IllegalStateException("Could not find class: $className.")
            val isIgnored = node.isIgnored()
            pool.removeClass(node)
            pool.addClass(outputStream.toByteArray())
            if(isIgnored) {
                val c = pool.getClass(className)!!
                pool.ignoreClass(c)
            }
        }
        pool.build()
    }
}