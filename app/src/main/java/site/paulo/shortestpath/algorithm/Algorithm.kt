package site.paulo.shortestpath.algorithm

import site.paulo.shortestpath.data.model.Node
import java.util.*

interface Algorithm {
    fun run()
    fun getShortestPath(): Stack<Node>
    fun getVisitedOrder(): LinkedList<Node>
}