package site.paulo.pathfinding.algorithm

import site.paulo.pathfinding.data.model.graph.GraphTypes
import site.paulo.pathfinding.data.model.Node
import site.paulo.pathfinding.data.model.PathFindingAlgorithms
import java.util.*


interface PathFindingAlgorithm {
    fun run(graphType: GraphTypes = GraphTypes.TRADITIONAL)
    fun getPath(): Stack<Node>
    fun getVisitedOrder(): LinkedList<Node>
    fun getType(): PathFindingAlgorithms
}