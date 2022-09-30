package io.rsbox.deobfuscator.asm

import io.rsbox.deobfuscator.asm.cfg.FlowGraph
import io.rsbox.deobfuscator.asm.editor.ClassEditor
import io.rsbox.deobfuscator.asm.reflect.ClassInfo
import io.rsbox.deobfuscator.asm.util.field
import io.rsbox.deobfuscator.asm.util.nullField
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode

internal fun ClassNode.init(pool: ClassPool) {
    this.pool = pool
    methods.forEach { it.init(this) }
    fields.forEach { it.init(this) }
}

internal fun ClassNode.reset() {
    superClass = null
    childClasses.clear()
    interfaceClasses.clear()
    implementerClasses.clear()

    methods.forEach { it.reset() }
    fields.forEach { it.reset() }
}

internal fun ClassNode.build() {
    /*
     * Build class hierarchy
     */
    superClass = pool.findClass(superName)
    if(superClass != null) {
        superClass!!.childClasses.add(this)
    }

    interfaces.mapNotNull { pool.findClass(it) }.forEach { interf ->
        interfaceClasses.add(interf)
        interf.childClasses.add(this)
    }

    info = pool.loader.loadClass(name)
    editor = pool.context.editClass(info)

    methods.forEach { it.build() }
    fields.forEach { it.build() }
}

var ClassNode.pool: ClassPool by field()

var ClassNode.superClass: ClassNode? by nullField()
val ClassNode.childClasses: MutableList<ClassNode> by field { mutableListOf() }
val ClassNode.interfaceClasses: MutableList<ClassNode> by field { mutableListOf() }
val ClassNode.implementerClasses: MutableList<ClassNode> by field { mutableListOf() }

val ClassNode.identifier get() = name
val ClassNode.type get() = Type.getObjectType(name)

var ClassNode.info: ClassInfo by field()
var ClassNode.editor: ClassEditor by field()

fun ClassNode.getMethod(name: String, desc: String) = methods.firstOrNull { it.name == name && it.desc == desc }
fun ClassNode.getField(name: String, desc: String) = fields.firstOrNull { it.name == name && it.desc == desc }

fun ClassNode.isIgnored() = pool.getIgnoredClass(name) != null

fun ClassNode.toByteArray(): ByteArray {
    val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
    this.accept(writer)
    return writer.toByteArray()
}