package site.paulo.shortestpath.algorithm

import site.paulo.shortestpath.data.model.MatrixGraph
import site.paulo.shortestpath.data.model.Node
import site.paulo.shortestpath.data.model.Point
import java.util.*
import kotlin.collections.HashMap

class Djikstra(private val matrixGraph: MatrixGraph,
               private val startPoint: Point,
               private val endPoint: Point) {

    /**
     * Shortest distance from S to V
     */
    private val shortestPath = HashMap<String, Double>()
    private val remaining = PriorityQueue<Node>()

    init {
        initShortestPaths()
    }

    fun run() {
        while(remaining.isNotEmpty()) {
            val lowest = remaining.poll()
            searchPath(lowest)
        }

        printPath()
    }

    private fun searchPath(currentNode: Node?) {
        if(currentNode == null) return
        for (adjacentNode in currentNode.getAdjacentNodes()) {
            val edge = currentNode.edges[adjacentNode.name]
            if(edge != null && !edge.visited) {
                val currentShortestPath = currentNode.shortestPath
                val shortestToAdjacent = adjacentNode.shortestPath

                var shortestPathFromAdjacent = Double.POSITIVE_INFINITY
                    if (shortestToAdjacent != Double.POSITIVE_INFINITY)
                        shortestPathFromAdjacent = shortestToAdjacent + edge.weight

                if(shortestPathFromAdjacent == Double.POSITIVE_INFINITY) {
                    setShortestPath(adjacentNode, currentShortestPath + edge.weight)
                    adjacentNode.previous = edge
                } else if (currentShortestPath > shortestPathFromAdjacent) {
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
    private fun initShortestPaths() {
        with(startPoint) {
            if ((row >= 0 && row < matrixGraph.columns) &&
                ((col >= 0 && col < matrixGraph.rows))
            ) {
                matrixGraph.table.forEach { row ->
                    row.forEach {
                        if (it != null) setShortestPath(it, Double.POSITIVE_INFINITY)
                    }
                }

                val startNode = matrixGraph.getNode(startPoint)
                if (startNode != null) {
                    setShortestPath(startNode, 0.0)
                    for (edge in startNode.edges.values) {
                        setShortestPath(edge.getOpposite(startNode), edge.weight)
                        edge.getOpposite(startNode).previous = edge
                        edge.visited = true
                    }
                }
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
     * Prints shortest path in System.out
     */
    private fun printPath() {
        val initialNode = matrixGraph.getNode(startPoint)
        val endNode = matrixGraph.getNode(endPoint)
        var currentNode: Node? = endNode
        val stackOfNodes: Stack<Node> = Stack() //used to reverse order to print

        println("Printing shortest path from ${initialNode?.name} to ${endNode?.name}:")
        while(currentNode != null) {
            stackOfNodes.push(currentNode)
            currentNode = currentNode.previous?.getOpposite(currentNode)
        }

        print(stackOfNodes.pop().name)
        while(!stackOfNodes.empty()) {
            print(" -> ${stackOfNodes.pop().name}")
        }
        println()
    }


}