package site.paulo.pathfinding.algorithm

import site.paulo.pathfinding.data.model.Edge
import site.paulo.pathfinding.data.model.GraphTypes
import site.paulo.pathfinding.data.model.Node
import site.paulo.pathfinding.data.model.PathFindingAlgorithms
import java.util.*
import kotlin.collections.HashSet

class BreadthFirst(private val startNode: Node, private val endNode: Node) : PathFindingAlgorithm {

    private val frontier = LinkedList<Node>()
    private val nodeVisited = HashSet<String>()
    private val nodeVisitedOrder = LinkedList<Node>()

    init {
        addToFrontier(null, startNode)
    }

    override fun run(graphType: GraphTypes) {
        while (frontier.isNotEmpty()) {
            val currentNode = frontier.poll()
            if(currentNode == endNode) break
            currentNode?.edges?.values?.forEach {edge ->
                if(edge.connected) {
                    val nodeToVisit = edge.getOpposite(currentNode)
                    if (!nodeVisited.contains(nodeToVisit.name)) {
                        addToFrontier(edge, nodeToVisit)
                    }
                }
            }
        }
    }

    override fun getPath(): Stack<Node> {
        var currentNode: Node? = endNode
        val stackOfNodes: Stack<Node> = Stack() //used to reverse order to print

        while (currentNode != null) {
            stackOfNodes.push(currentNode)
            currentNode = currentNode.previous?.getOpposite(currentNode)
        }

        if (stackOfNodes.peek() == startNode) return stackOfNodes
        return Stack()
    }

    override fun getVisitedOrder(): LinkedList<Node> {
        return nodeVisitedOrder
    }

    override fun getType(): PathFindingAlgorithms {
        return PathFindingAlgorithms.BREADTH_FIRST
    }

    private fun addToFrontier(edge: Edge?, currentNode: Node) {
        frontier.add(currentNode)
        nodeVisited.add(currentNode.name)
        nodeVisitedOrder.add(currentNode)
        currentNode.previous = edge
    }

}