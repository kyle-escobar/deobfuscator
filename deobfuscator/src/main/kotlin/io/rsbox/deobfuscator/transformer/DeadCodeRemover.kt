package io.rsbox.deobfuscator.transformer

import io.rsbox.deobfuscator.Transformer
import io.rsbox.deobfuscator.asm.ClassPool
import io.rsbox.deobfuscator.asm.owner
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.BasicInterpreter
import org.tinylog.kotlin.Logger

class DeadCodeRemover : Transformer {

    private var count = 0

    override fun run(pool: ClassPool) {
        pool.classes.forEach { cls ->
            cls.methods.forEach { method ->
                val insns = method.instructions.toArray()
                val frames = Analyzer(BasicInterpreter()).analyze(method.owner.name, method)
                for(i in frames.indices) {
                    if(frames[i] == null) {
                        method.instructions.remove(insns[i])
                        count++
                    }
                }
            }
        }
        Logger.info("Removed $count dead instructions.")
    }
}