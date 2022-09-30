package io.rsbox.deobfuscator.asm.util

import kotlin.reflect.KProperty

class ExtensionField<R, T>(private val init: (R) -> T = { throw IllegalStateException("Property not initialized!") }) {
    private val store = mutableWeakIdentityHashMap<R, T>()

    operator fun getValue(self: R, prop: KProperty<*>): T = store[self] ?: setValue(self, prop, init(self))

    operator fun setValue(self: R, prop: KProperty<*>, value: T): T = value.apply {
        store[self] = this
    }
}

class NullableExtensionField<R, T>(private val init: (R) -> T? = { null }) {
    private val store = mutableWeakIdentityHashMap<R, T?>()

    operator fun getValue(self: R, prop: KProperty<*>): T? = store[self] ?: setValue(self, prop, init(self))

    operator fun setValue(self: R, prop: KProperty<*>, value: T?): T? = value.apply {
        store[self] = this
    }
}

fun <T, R> field(init: (R) -> T) = ExtensionField(init)
fun <T, R> field() = ExtensionField<R, T>()

fun <T, R> nullField(init: (R) -> T?) = NullableExtensionField(init)
fun <T, R> nullField() = NullableExtensionField<R, T?>()