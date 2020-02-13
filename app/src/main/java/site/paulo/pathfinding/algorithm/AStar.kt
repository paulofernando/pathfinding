package site.paulo.pathfinding.algorithm

import site.paulo.pathfinding.data.model.MatrixGraph
import site.paulo.pathfinding.data.model.Node
import kotlin.math.abs

class AStar(matrixGraph: MatrixGraph, startNode: Node, endNode: Node) :
    Djikstra(matrixGraph, startNode, endNode) {

    fun updateHeuristicDistance() {
        val endNodePositionInMatrix = getEndNodePositionInMatrix()
        if (endNodePositionInMatrix == Pair(-1,-1)) return
        (graph as MatrixGraph).table.forEachIndexed { i, row ->
            row.forEachIndexed { j, node ->
                if (node != null) {
                    node.heuristicDistance = getManhattanDistance(Pair(i,j), endNodePositionInMatrix)
                    remaining.add(node)
                }
            }
        }
    }

    fun getManhattanDistance(pointA: Pair<Int,Int>, pointB: Pair<Int,Int>): Int {
        return (abs(pointA.first - pointB.first) + abs(pointA.second - pointB.second))
    }

    override fun prepare() {
        super.prepare()
        remaining.clear()
        updateHeuristicDistance()
    }

    private fun getEndNodePositionInMatrix(): Pair<Int,Int> {
        (graph as MatrixGraph).table.forEachIndexed { i, row ->
            row.forEachIndexed { j, node ->
                if (node == endNode) return Pair(i,j)
            }
        }
        return Pair(-1,-1)
    }

}