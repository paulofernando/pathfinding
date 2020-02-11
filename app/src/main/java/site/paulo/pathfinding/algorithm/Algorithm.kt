package site.paulo.pathfinding.algorithm

import site.paulo.pathfinding.data.model.Node
import java.util.*

interface Algorithm {
    fun run()
    fun getShortestPath(): Stack<Node>
    fun getVisitedOrder(): LinkedList<Node>
}