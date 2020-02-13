package site.paulo.pathfinding.algorithm

import org.junit.jupiter.api.Test
import site.paulo.pathfinding.data.model.MatrixGraph

internal class DjikstraTest {

    @Test
    fun `test djikstra algorithm in matrix`() {
        val matrix = MatrixGraph(3,3)
        val djikstra = Djikstra(matrix, matrix.getNode(Pair(0,0))!!, matrix.getNode(Pair(2,2))!!)
        djikstra.run()
        assert(djikstra.getPath().size == 5)
    }

    @Test
    fun `test djikstra algorithm in matrix - reverse`() {
        val matrix = MatrixGraph(3,3)
        val djikstra = Djikstra(matrix, matrix.getNode(Pair(2,2))!!, matrix.getNode(Pair(0,0))!!)
        djikstra.run()

        assert(djikstra.getPath().size == 5)
    }

    @Test
    fun `test djikstra algorithm in matrix - no path start at corner - remove diagonal`() {
        val matrix = MatrixGraph(3,3)
        val djikstra = Djikstra(matrix, matrix.getNode(Pair(0,0))!!, matrix.getNode(Pair(2,2))!!)

        matrix.removeNode(Pair(0,2))
        matrix.removeNode(Pair(2,0))
        matrix.removeNode(Pair(1,1))
        djikstra.run()

        assert(djikstra.getPath().size == 0)
    }

    @Test
    fun `test djikstra algorithm in matrix - no path start at corner - lock around start point`() {
        val matrix = MatrixGraph(3,3)
        val djikstra = Djikstra(matrix, matrix.getNode(Pair(0,0))!!, matrix.getNode(Pair(2,2))!!)

        matrix.removeNode(Pair(0,1))
        matrix.removeNode(Pair(1,0))
        matrix.removeNode(Pair(1,1))
        djikstra.run()

        assert(djikstra.getPath().size == 0)
    }

    @Test
    fun `test djikstra algorithm in matrix - no path - start at middle`() {
        val matrix = MatrixGraph(10,10)
        val djikstra = Djikstra(matrix, matrix.getNode(Pair(0,0))!!, matrix.getNode(Pair(5,5))!!)

        matrix.removeNode(Pair(5,4))
        matrix.removeNode(Pair(5,6))
        matrix.removeNode(Pair(4,5))
        matrix.removeNode(Pair(6,5))
        djikstra.run()

        assert(djikstra.getPath().size == 0)
    }

    @Test
    fun `test djikstra algorithm in matrix - short matrix`() {
        val matrix = MatrixGraph(2,2)
        val djikstra = Djikstra(matrix, matrix.getNode(Pair(0,0))!!, matrix.getNode(Pair(1,1))!!)

        djikstra.run()

        assert(djikstra.getPath().size == 3)
    }

    @Test
    fun `test djikstra algorithm in matrix - shortest matrix`() {
        val matrix = MatrixGraph(1,1)
        val djikstra = Djikstra(matrix, matrix.getNode(Pair(0,0))!!, matrix.getNode(Pair(0,0))!!)

        djikstra.run()

        assert(djikstra.getPath().size == 1)
    }
}