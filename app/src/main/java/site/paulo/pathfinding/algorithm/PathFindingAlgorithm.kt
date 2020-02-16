package site.paulo.pathfinding.algorithm

import site.paulo.pathfinding.data.model.Node
import site.paulo.pathfinding.ui.component.graphview.PathFindingAlgorithms
import java.util.*

interface PathFindingAlgorithm {
    fun run()
    fun getPath(): Stack<Node>
    fun getVisitedOrder(): LinkedList<Node>
    fun getType(): PathFindingAlgorithms
}