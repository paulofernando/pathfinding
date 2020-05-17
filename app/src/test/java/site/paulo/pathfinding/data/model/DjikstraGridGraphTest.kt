package test.site.paulo.pathfinding.data.model

import org.junit.jupiter.api.Test
import site.paulo.pathfinding.algorithm.Djikstra
import site.paulo.pathfinding.data.model.graph.GraphTypes
import site.paulo.pathfinding.data.model.graph.GridGraph

internal class DjikstraGridGraphTest {

    @Test
    fun `test Djikstra algorithm in grid graph`() {
        val gridGraph = GridGraph(3, 3)
        val djikstra = Djikstra(
            gridGraph.getNodes(),
            gridGraph.getNode(Pair(0, 0))!!,
            gridGraph.getNode(Pair(2, 2))!!
        )
        djikstra.run(GraphTypes.GRID)
        assert(djikstra.getPath().size == 5)
    }

    @Test
    fun `test Djikstra algorithm in grid graph - reverse`() {
        val gridGraph = GridGraph(3, 3)
        val djikstra = Djikstra(
            gridGraph.getNodes(),
            gridGraph.getNode(Pair(2, 2))!!,
            gridGraph.getNode(Pair(0, 0))!!
        )
        djikstra.run(GraphTypes.GRID)

        assert(djikstra.getPath().size == 5)
    }

    @Test
    fun `test Djikstra algorithm in grid graph - no path start at corner - remove diagonal`() {
        val gridGraph = GridGraph(3, 3)
        val djikstra = Djikstra(
            gridGraph.getNodes(),
            gridGraph.getNode(Pair(0, 0))!!,
            gridGraph.getNode(Pair(2, 2))!!
        )

        gridGraph.removeNode(Pair(0,2))
        gridGraph.removeNode(Pair(2,0))
        gridGraph.removeNode(Pair(1,1))
        djikstra.run(GraphTypes.GRID)

        assert(djikstra.getPath().size == 0)
    }

    @Test
    fun `test Djikstra algorithm grid graph - no path start at corner - lock around start point`() {
        val gridGraph = GridGraph(3, 3)
        val djikstra = Djikstra(
            gridGraph.getNodes(),
            gridGraph.getNode(Pair(0, 0))!!,
            gridGraph.getNode(Pair(2, 2))!!
        )

        gridGraph.removeNode(Pair(0,1))
        gridGraph.removeNode(Pair(1,0))
        gridGraph.removeNode(Pair(1,1))
        djikstra.run(GraphTypes.GRID)

        assert(djikstra.getPath().size == 0)
    }

    @Test
    fun `test Djikstra algorithm in grid graph - no path - start at middle`() {
        val gridGraph = GridGraph(10, 10)
        val djikstra = Djikstra(
            gridGraph.getNodes(),
            gridGraph.getNode(Pair(0, 0))!!,
            gridGraph.getNode(Pair(5, 5))!!
        )

        gridGraph.removeNode(Pair(5,4))
        gridGraph.removeNode(Pair(5,6))
        gridGraph.removeNode(Pair(4,5))
        gridGraph.removeNode(Pair(6,5))
        djikstra.run(GraphTypes.GRID)

        assert(djikstra.getPath().size == 0)
    }

    @Test
    fun `test Djikstra algorithm in grid graph - short grid graph`() {
        val gridGraph = GridGraph(2, 2)
        val djikstra = Djikstra(
            gridGraph.getNodes(),
            gridGraph.getNode(Pair(0, 0))!!,
            gridGraph.getNode(Pair(1, 1))!!
        )

        djikstra.run(GraphTypes.GRID)

        assert(djikstra.getPath().size == 3)
    }

    @Test
    fun `test Djikstra algorithm in grid graph - shortest grid graph`() {
        val gridGraph = GridGraph(1, 1)
        val djikstra = Djikstra(
            gridGraph.getNodes(),
            gridGraph.getNode(Pair(0, 0))!!,
            gridGraph.getNode(Pair(0, 0))!!
        )

        djikstra.run(GraphTypes.GRID)

        assert(djikstra.getPath().size == 1)
    }

}