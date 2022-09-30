package io.rsbox.deobfuscator.transformer

import io.rsbox.deobfuscator.Transformer
import io.rsbox.deobfuscator.asm.ClassPool
import org.objectweb.asm.Type
import org.tinylog.kotlin.Logger
import java.lang.RuntimeException

class RuntimeExceptionRemover : Transformer {

    private var count = 0

    override fun run(pool: ClassPool) {
        pool.classes.forEach { cls ->
            cls.methods.forEach { method ->
                val tryCatchBlocks = method.tryCatchBlocks.toList()
                tryCatchBlocks.forEach { tcb ->
                    if(tcb.type == Type.getInternalName(RuntimeException::class.java)) {
                        method.tryCatchBlocks.remove(tcb)
                        count++
                    }
                }
            }
        }
        Logger.info("Removed $count RuntimeException try-catch blocks.")
    }
}