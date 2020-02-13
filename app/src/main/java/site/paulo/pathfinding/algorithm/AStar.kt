package site.paulo.pathfinding.algorithm

import site.paulo.pathfinding.data.model.MatrixGraph
import kotlin.math.abs

class AStar(matrixGraph: MatrixGraph, startPoint: Pair<Int, Int>, endPoint: Pair<Int, Int>) :
    Djikstra(matrixGraph, startPoint, endPoint) {

    init {
        println(matrixGraph)
    }

    fun updateHeuristicDistance() {
        matrixGraph.table.forEachIndexed { i, row ->
            row.forEachIndexed { j, node ->
                if (node != null) {
                    node.heuristicDistance = getManhattanDistance(Pair(i,j), endPoint)
                    remaining.add(node)
                }
            }
        }
    }

    fun getManhattanDistance(pointA: Pair<Int,Int>, pointB: Pair<Int,Int>): Int {
        return (abs(pointA.first - pointB.first) + abs(pointA.second - pointB.second))
    }

    override fun initShortestPaths() {
        super.initShortestPaths()
        remaining.clear()
        updateHeuristicDistance()
    }

}