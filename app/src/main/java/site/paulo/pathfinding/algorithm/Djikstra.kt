package site.paulo.pathfinding.algorithm

import site.paulo.pathfinding.data.model.MatrixGraph
import site.paulo.pathfinding.data.model.Node
import java.util.*
import kotlin.collections.HashMap

open class Djikstra (
    var matrixGraph: MatrixGraph,
    var startNode: Node,
    var endNode: Node
) : PathFindingAlgorithm {

    /**
     * Shortest distance from S to V
     */
    private val shortestPath = HashMap<String, Double>()
    /**
     * Priority queue based on shortest path with all nodes in the graph
     */
    protected val remaining = PriorityQueue<Node>()
    /**
     * Node visited order
     */
    private val nodeVisitedOrder = LinkedList<Node>()

    init {
        initShortestPaths()
    }

    override fun run() {
        if (remaining.isEmpty()) return

        var lowest = remaining.poll()
        while ((lowest != endNode) && (lowest.shortestPath != Double.POSITIVE_INFINITY)) {
            searchPath(lowest)
            lowest = remaining.poll()
        }
        printPath(getPath())
        clearVisitedEdges() //cleaning edges to next execution
    }

    private fun searchPath(currentNode: Node?) {
        if (currentNode == null) return
        nodeVisitedOrder.add(currentNode)
        for (adjacentNode in currentNode.getAdjacentNodes()) {
            val edge = currentNode.edges[adjacentNode.name]
            if (edge != null && !edge.visited && edge.connected) {
                val shortestPathFromAdjacent = adjacentNode.shortestPath + edge.weight
                if (shortestPathFromAdjacent == Double.POSITIVE_INFINITY) {
                    setShortestPath(adjacentNode, currentNode.shortestPath + edge.weight)
                    adjacentNode.previous = edge
                } else if (currentNode.shortestPath > shortestPathFromAdjacent) {
                    setShortestPath(currentNode, shortestPathFromAdjacent)
                    currentNode.previous = edge
                }

                edge.visited = true
            }
        }
    }

    /**
     * Initializes shortest paths with infinity
     */
    open fun initShortestPaths() {
        matrixGraph.table.forEach { row ->
            row.forEach { if (it != null) setShortestPath(it, Double.POSITIVE_INFINITY) }
        }

        matrixGraph.resetNodes() //make sure that the nodes has no 'previous' from older processing
        setShortestPath(startNode, 0.0)
        for (edge in startNode.edges.values) {
            if (edge.connected) {
                setShortestPath(edge.getOpposite(startNode), edge.weight)
                edge.getOpposite(startNode).previous = edge
            }
        }
    }

    private fun setShortestPath(node: Node, weight: Double) {
        node.shortestPath = weight
        shortestPath[node.name] = weight

        //updating priority queue
        remaining.remove(node)
        remaining.add(node)
    }

    /**
     * Retrieves the shortest from start to end
     */
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

    fun clearVisitedEdges() {
        matrixGraph.table.forEach { row ->
            row.forEach {
                it?.edges?.values?.forEach{ it.visited = false}
            }
        }
    }

    /**
     * Prints shortest path in System.out
     */
    fun printPath(path: Stack<Node>) {
        println("Printing shortest path from ${startNode.name} to ${endNode?.name}:")
        if (path.empty()) {
            println("No path found from ${startNode.name} to ${endNode?.name}")
            return
        }

        println("Visiting: ${path.size} nodes...")
        print(path.pop().name)
        while(path.isNotEmpty()) {
            print(" -> ${path.pop().name}")
        }
        println()
    }

}