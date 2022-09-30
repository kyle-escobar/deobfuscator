package io.rsbox.deobfuscator.asm.util

class UniqueQueue<T> {

    private val queue = ArrayDeque<T>()
    private val set = mutableSetOf<T>()

    fun add(value: T): Boolean {
        if(set.add(value)) {
            queue.addLast(value)
            return true
        }
        return false
    }

    fun addAll(values: Iterable<T>) {
        for(value in values) {
            add(value)
        }
    }

    fun removeFirstOrNull(): T? {
        val value = queue.removeFirstOrNull()
        if(value != null) {
            set.remove(value)
            return value
        }
        return null
    }

    fun clear() {
        queue.clear()
        set.clear()
    }
}