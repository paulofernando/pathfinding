package site.paulo.pathfinding.algorithm

import site.paulo.pathfinding.data.model.GridGraph
import site.paulo.pathfinding.data.model.Node
import site.paulo.pathfinding.data.model.PathFindingAlgorithms
import kotlin.math.abs

class AStar(private val gridGraph: GridGraph, startNode: Node, endNode: Node) :
    Djikstra(gridGraph.getNodes(), startNode, endNode) {

    private fun updateHeuristicDistance() {
        val endNodePositionInMatrix = getEndNodePositionInMatrix()
        if (endNodePositionInMatrix == Pair(-1,-1)) return
        gridGraph.table.forEachIndexed { i, row ->
            row.forEachIndexed { j, node ->
                if (node != null) {
                    node.heuristicDistance = getManhattanDistance(Pair(i,j), endNodePositionInMatrix)
                    remaining.add(node)
                }
            }
        }
    }

    private fun getManhattanDistance(pointA: Pair<Int,Int>, pointB: Pair<Int,Int>): Int {
        return (abs(pointA.first - pointB.first) + abs(pointA.second - pointB.second))
    }

    override fun prepare() {
        super.prepare()
        remaining.clear()
        updateHeuristicDistance()
    }

    override fun getType(): PathFindingAlgorithms {
        return PathFindingAlgorithms.ASTAR
    }

    private fun getEndNodePositionInMatrix(): Pair<Int,Int> {
        gridGraph.table.forEachIndexed { i, row ->
            row.forEachIndexed { j, node ->
                if (node == endNode) return Pair(i,j)
            }
        }
        return Pair(-1,-1)
    }

}