package io.rsbox.deobfuscator.asm.analysis

import io.rsbox.deobfuscator.asm.owner
import org.jgrapht.Graph
import org.jgrapht.graph.AsUnmodifiableGraph
import org.jgrapht.graph.DefaultDirectedGraph
import org.jgrapht.graph.DefaultEdge
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.BasicInterpreter
import org.objectweb.asm.tree.analysis.BasicValue

class ControlFlowAnalyzer : Analyzer<BasicValue>(BasicInterpreter()) {

    private val graph = DefaultDirectedGraph<Int, DefaultEdge>(DefaultEdge::class.java)

    override fun newControlFlowEdge(insnIndex: Int, successorIndex: Int) {
        graph.addVertex(insnIndex)
        graph.addVertex(successorIndex)
        graph.addEdge(insnIndex, successorIndex)
    }

    override fun newControlFlowExceptionEdge(insnIndex: Int, successorIndex: Int): Boolean {
        newControlFlowEdge(insnIndex, successorIndex)
        return true
    }

    fun buildGraph(method: MethodNode): Graph<Int, DefaultEdge> {
        analyze(method.owner.name, method)
        return AsUnmodifiableGraph(graph)
    }
}