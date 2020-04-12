package site.paulo.pathfinding.algorithm

import site.paulo.pathfinding.data.model.Node
import site.paulo.pathfinding.data.model.PathFindingAlgorithms
import java.util.*
import kotlin.collections.HashSet

class DepthFirst(private val startNode: Node, private val endNode: Node) : PathFindingAlgorithm {

    private val nodeVisited = HashSet<String>()
    private val nodeVisitedOrder = LinkedList<Node>()

    override fun run() {
        nodeVisited.clear()
        nodeVisitedOrder.clear()
        dfs(startNode)
    }

    fun dfs(node: Node) {
        nodeVisited.add(node.name)
        nodeVisitedOrder.add(node)
        node.edges.values.forEach {edge ->
            if(edge.connected && !nodeVisited.contains(endNode.name)) {
                val nodeToVisit = edge.getOpposite(node)
                if (nodeToVisit != null && !nodeVisited.contains(nodeToVisit.name)) {
                    nodeToVisit.previous = edge
                    dfs(nodeToVisit)
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
        return PathFindingAlgorithms.DEPTH_FIRST
    }
}