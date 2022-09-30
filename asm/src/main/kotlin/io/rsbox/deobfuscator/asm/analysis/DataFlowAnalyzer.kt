package io.rsbox.deobfuscator.asm.analysis

import io.rsbox.deobfuscator.asm.util.UniqueQueue
import org.jgrapht.Graph
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.EdgeReversedGraph
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.MethodNode

abstract class DataFlowAnalyzer<T>(private val method: MethodNode, private val backwards: Boolean = false) {

    private val graph: Graph<Int, DefaultEdge> = ControlFlowAnalyzer().buildGraph(method).let {
        if(backwards) EdgeReversedGraph(it)
        else it
    }

    private val inSets = mutableMapOf<Int, T>()
    private val outSets = mutableMapOf<Int, T>()

    open fun createEntrySet(): T = createInitialSet()
    abstract fun createInitialSet(): T
    abstract fun join(set1: T, set2: T): T
    abstract fun transfer(set: T, insn: AbstractInsnNode): T

    fun getInSet(insn: AbstractInsnNode): T? = getInSet(method.instructions.indexOf(insn))
    fun getInSet(index: Int): T? = inSets[index]

    fun getOutSet(insn: AbstractInsnNode): T? = getOutSet(method.instructions.indexOf(insn))
    fun getOutSet(index: Int): T? = outSets[index]

    fun analyze() {
        val entrySet = createEntrySet()
        val initialSet = createInitialSet()

        val workList = UniqueQueue<Int>()
        workList.addAll(graph.vertexSet().filter { graph.inDegreeOf(it) == 0 })

        while(true) {
            val node = workList.removeFirstOrNull() ?: break

            val predecessors = graph.incomingEdgesOf(node).map { edge ->
                outSets[graph.getEdgeSource(edge)] ?: initialSet
            }

            val inSet = if(predecessors.isEmpty()) {
                entrySet
            } else {
                predecessors.reduce(this::join)
            }

            inSets[node] = inSet

            val outSet = transfer(inSet, method.instructions[node])
            if(outSets[node] != outSet) {
                outSets[node] = outSet
                for(edge in graph.outgoingEdgesOf(node)) {
                    val successor = graph.getEdgeTarget(edge)
                    workList.add(successor)
                }
            }
        }
    }
}