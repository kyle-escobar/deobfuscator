package io.rsbox.deobfuscator.asm

import io.rsbox.deobfuscator.asm.cfg.FlowGraph
import io.rsbox.deobfuscator.asm.editor.MemberRef
import io.rsbox.deobfuscator.asm.editor.MethodEditor
import io.rsbox.deobfuscator.asm.editor.NameAndType
import io.rsbox.deobfuscator.asm.util.field
import io.rsbox.deobfuscator.asm.util.nullField
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode
import io.rsbox.deobfuscator.asm.editor.Type as TypeRef

internal fun MethodNode.init(owner: ClassNode) {
    this.owner = owner
}

internal fun MethodNode.reset() {}
internal fun MethodNode.build() {}

var MethodNode.owner: ClassNode by field()
val MethodNode.pool get() = owner.pool

val MethodNode.identifier get() = "${owner.identifier}.$name$desc"
val MethodNode.type get() = Type.getMethodType(desc)
val MethodNode.parameterTypes get() = type.argumentTypes.toList()
val MethodNode.returnType get() = type.returnType

fun MethodNode.isStatic() = (access and ACC_STATIC) != 0
fun MethodNode.isInterface() = (access and ACC_INTERFACE) != 0
fun MethodNode.isAbstract() = (access and ACC_ABSTRACT) != 0

private var MethodNode._editor: MethodEditor? by nullField()
val MethodNode.info get() = editor.methodInfo()
val MethodNode.editor: MethodEditor get() {
    if(_editor == null) {
        _editor = pool.context.editMethod(MemberRef(owner.editor.type(), NameAndType(name, TypeRef.getType(desc))))
    }
    return _editor!!
}

private var MethodNode._cfg: FlowGraph? by nullField()
val MethodNode.cfg: FlowGraph get() {
    if(_cfg == null) {
        _cfg = FlowGraph(editor)
        _cfg!!.initialize()
    }
    return _cfg!!
}

val MethodNode.virtualMethods: List<MethodNode> get() {
    val ret = mutableListOf<MethodNode>()
    if(isStatic()) {
        ret.add(this)
        return ret
    }

    findBaseMethods(mutableListOf(), owner, name, desc).forEach {
        findInheritedMethods(ret, mutableSetOf(), it.owner, it.name, it.desc)
    }

    return ret
}

private fun findBaseMethods(methods: MutableList<MethodNode>, cls: ClassNode?, name: String, desc: String): MutableList<MethodNode> {
    if(cls == null) {
        return methods
    }

    val m = cls.getMethod(name, desc)
    if(m != null && !m.isStatic()) {
        methods.add(m)
    }

    val parentMethods = findBaseMethods(mutableListOf(), cls.superClass, name, desc)
    cls.interfaceClasses.forEach { parentMethods.addAll(findBaseMethods(mutableListOf(), it, name, desc)) }

    return if(parentMethods.isEmpty()) methods else parentMethods
}

private fun findInheritedMethods(
    methods: MutableList<MethodNode>,
    visited: MutableSet<ClassNode>,
    cls: ClassNode? ,
    name: String,
    desc: String
) {
    if(cls == null || visited.contains(cls)) {
        return
    }
    visited.add(cls)

    val m = cls.getMethod(name, desc)
    if(m != null && !m.isStatic()) {
        methods.add(m)
    }

    cls.childClasses.forEach {
        findInheritedMethods(methods, visited, it, name, desc)
    }
}