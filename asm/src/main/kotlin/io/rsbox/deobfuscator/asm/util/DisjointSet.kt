package io.rsbox.deobfuscator.asm.util

interface DisjointSet<T> : Iterable<DisjointSet.Partition<T>> {

    interface Partition<T> : Iterable<T>

    val elements: Int
    val partitions: Int

    fun add(element: T): Partition<T>
    fun union(element1: Partition<T>, element2: Partition<T>)

    operator fun get(element: T): Partition<T>?

}