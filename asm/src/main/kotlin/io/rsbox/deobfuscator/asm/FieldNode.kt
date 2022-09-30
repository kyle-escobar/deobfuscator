package io.rsbox.deobfuscator.asm

import io.rsbox.deobfuscator.asm.editor.FieldEditor
import io.rsbox.deobfuscator.asm.editor.MemberRef
import io.rsbox.deobfuscator.asm.editor.NameAndType
import io.rsbox.deobfuscator.asm.editor.Type
import io.rsbox.deobfuscator.asm.reflect.FieldInfo
import io.rsbox.deobfuscator.asm.util.field
import io.rsbox.deobfuscator.asm.util.nullField
import org.objectweb.asm.Opcodes.ACC_ABSTRACT
import org.objectweb.asm.Opcodes.ACC_STATIC
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import io.rsbox.deobfuscator.asm.editor.Type as TypeRef

internal fun FieldNode.init(owner: ClassNode) {
    this.owner = owner
}

internal fun FieldNode.reset() {}
internal fun FieldNode.build() {}

var FieldNode.owner: ClassNode by field()
val FieldNode.pool get() = owner.pool

val FieldNode.identifier get() = "${owner.identifier}.$name"
val FieldNode.type get() = Type.getType(desc)

fun FieldNode.isStatic() = (access and ACC_STATIC) != 0
fun FieldNode.isAbstract() = (access and ACC_ABSTRACT) != 0

private var FieldNode._editor: FieldEditor? by nullField()
val FieldNode.info: FieldInfo get() = editor.fieldInfo()
val FieldNode.editor: FieldEditor get() {
    if(_editor == null) {
        _editor = pool.context.editField(MemberRef(owner.editor.type(), NameAndType(name, TypeRef.getType(desc))))
    }
    return _editor!!
}

val FieldNode.virtualFields: List<FieldNode> get() {
    val ret = mutableListOf<FieldNode>()
    if(isStatic()) {
        ret.add(this)
        return ret
    }

    findBaseFields(mutableListOf(), owner, name, desc).forEach {
        findInheritedFields(ret, mutableListOf(), it.owner, it.name, it.desc)
    }

    return ret
}

private fun findBaseFields(fields: MutableList<FieldNode>, cls: ClassNode?, name: String, desc: String): MutableList<FieldNode> {
    if(cls == null) {
        return fields
    }

    val f = cls.getField(name, desc)
    if(f != null && !f.isStatic()) {
        fields.add(f)
    }

    val parentFields = findBaseFields(mutableListOf(), cls.superClass, name, desc)
    return if(parentFields.isEmpty()) fields else parentFields
}

private fun findInheritedFields(
    fields: MutableList<FieldNode>,
    visited: MutableList<ClassNode>,
    cls: ClassNode?,
    name: String,
    desc: String
) {
    if(cls == null || visited.contains(cls)) {
        return
    }
    visited.add(cls)

    val f = cls.getField(name, desc)
    if(f != null && !f.isStatic()) {
        fields.add(f)
    }

    cls.childClasses.forEach {
        findInheritedFields(fields, visited, it, name, desc)
    }
}