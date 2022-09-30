package io.rsbox.deobfuscator.asm

import io.rsbox.deobfuscator.asm.context.CachingBloatContext
import io.rsbox.deobfuscator.asm.file.ClassNodeFileLoader
import io.rsbox.deobfuscator.asm.reflect.ClassInfo
import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

class ClassPool {

    private val classMap = hashMapOf<String, ClassNode>()
    private val ignoredClassMap = hashMapOf<String, ClassNode>()

    internal val loader = ClassNodeFileLoader(this)
    internal val context = CachingBloatContext(loader, listOf<ClassInfo>(), false)

    val classes get() = classMap.values.toList()
    val ignoredClasses get() = ignoredClassMap.values.toList()

    fun addClass(node: ClassNode) {
        node.init(this)
        classMap[node.name] = node
    }

    fun removeClass(node: ClassNode) {
        classMap.remove(node.name)
        ignoredClassMap.remove(node.name)
    }


    fun addClass(bytes: ByteArray) {
        val node = ClassNode()
        val reader = ClassReader(bytes)
        reader.accept(node, ClassReader.SKIP_FRAMES)
        addClass(node)
    }

    fun addJarClasses(file: File) {
        JarFile(file).use { jar ->
            jar.entries().asSequence().forEach { entry ->
                if(entry.name.endsWith(".class")) {
                    addClass(jar.getInputStream(entry).readAllBytes())
                }
            }
        }
    }

    fun ignoreClass(node: ClassNode) {
        if(ignoredClasses.contains(node)) {
            throw IllegalArgumentException("Class ${node.name} is already ignored.")
        }
        classMap.remove(node.name)
        ignoredClassMap[node.name] = node
    }

    fun unignoreClass(node: ClassNode) {
        if(!ignoredClasses.contains(node)) {
            throw IllegalArgumentException("Class ${node.name} is not ignored.")
        }
        ignoredClassMap.remove(node.name)
        classMap[node.name] = node
    }

    fun build() {
        val allClasses = mutableListOf<ClassNode>().also {
            it.addAll(classes)
            it.addAll(ignoredClasses)
        }
        allClasses.forEach { it.reset() }
        allClasses.forEach { it.build() }
    }

    fun getClass(name: String) = classMap[name]
    fun getIgnoredClass(name: String) = ignoredClassMap[name]
    fun findClass(name: String) = getClass(name) ?: getIgnoredClass(name)

    fun clear() {
        classMap.clear()
        ignoredClassMap.clear()
    }

    fun saveToJar(file: File) {
        if(file.exists()) file.deleteRecursively()
        JarOutputStream(FileOutputStream(file)).use { jos ->
            val allClasses = mutableListOf<ClassNode>().also {
                it.addAll(classes)
                it.addAll(ignoredClasses)
            }
            allClasses.forEach { cls ->
                jos.putNextEntry(JarEntry(cls.name + ".class"))
                jos.write(cls.toByteArray())
                jos.closeEntry()
            }
        }
    }

    fun commit() {
        context.commitDirty()
        loader.done()
    }
}