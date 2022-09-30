package io.rsbox.deobfuscator

import io.rsbox.deobfuscator.asm.ClassPool
import io.rsbox.deobfuscator.transformer.DeadCodeRemover
import io.rsbox.deobfuscator.transformer.RuntimeExceptionRemover
import io.rsbox.deobfuscator.transformer.multiplier.MultiplierFinder
import org.tinylog.kotlin.Logger
import java.io.File
import kotlin.reflect.full.createInstance

object Deobfuscator {

    private val pool = ClassPool()
    private var testModeEnabled = false

    private val transformers = mutableListOf<Transformer>()

    init {
        addTransformer<RuntimeExceptionRemover>()
        addTransformer<DeadCodeRemover>()
        addTransformer<MultiplierFinder>()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        if(args.size < 2) {
            throw IllegalArgumentException("Missing required program arguments. deobfuscator.jar <input-jar> <output-jar>")
        }

        val inputJar = File(args[0])
        val outputJar = File(args[1])

        if(args.size >= 3) {
            testModeEnabled = args[2] == "--test"
        }

        /*
         * Initialize
         */
        if(testModeEnabled) {
            Logger.info("Enabling post testing mode.")
        }

        Logger.info("Loading classes from jar: ${inputJar.path}.")

        pool.clear()
        pool.addJarClasses(inputJar)
        pool.classes.forEach { cls ->
            if(cls.name.contains("/")) {
                pool.ignoreClass(cls)
            }
        }
        pool.build()
        Logger.info("Found ${pool.classes.size} classes. Ignored ${pool.ignoredClasses.size} classes.")

        run()

        /*
         * Export / saves classes to jar
         */
        Logger.info("Saving transformed classes to jar: ${outputJar.path}.")
        pool.saveToJar(outputJar)

        Logger.info("Deobfuscator has completed.")

        if(testModeEnabled) {
            /*
             * Run a instance of TestClient using the output jar file.
             */
            TestClient(outputJar).start()
        }
    }

    private fun run() {
        Logger.info("Starting RSBox deobfuscator.")

        transformers.forEach { transformer ->
            val start = System.currentTimeMillis()
            transformer.run(pool)
            val delta = System.currentTimeMillis() - start
            Logger.info("Finished running transformer: ${transformer::class.simpleName} in ${delta}ms.")
        }

        Logger.info("Successfully completed deobfuscation transforms.")
    }

    private inline fun <reified T : Transformer> addTransformer() {
        val inst = T::class.createInstance()
        transformers.add(inst)
    }
}