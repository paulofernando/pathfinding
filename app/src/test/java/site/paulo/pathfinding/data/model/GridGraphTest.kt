package site.paulo.pathfinding.data.model

import io.mockk.clearAllMocks
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GridGraphTest {

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `test grid graph creation and getNode`() {
        val gridGraph = GridGraph(3,3)

        assert(gridGraph.getNode(0,0)?.name.equals("0,0"))
        assert(gridGraph.getNode(0,1)?.name.equals("0,1"))
        assert(gridGraph.getNode(0,2)?.name.equals("0,2"))
        assert(gridGraph.getNode(1,0)?.name.equals("1,0"))
        assert(gridGraph.getNode(1,1)?.name.equals("1,1"))
        assert(gridGraph.getNode(1,2)?.name.equals("1,2"))
        assert(gridGraph.getNode(2,0)?.name.equals("2,0"))
        assert(gridGraph.getNode(2,1)?.name.equals("2,1"))
        assert(gridGraph.getNode(2,2)?.name.equals("2,2"))
    }

    @Test
    fun `test grid graph connections`() {
        val gridGraph = GridGraph(3,3)

        assert(gridGraph.getNode(0,0)?.edges?.size == 2)
        assert(gridGraph.getNode(0,1)?.edges?.size == 3)
        assert(gridGraph.getNode(0,2)?.edges?.size == 2)
        assert(gridGraph.getNode(1,0)?.edges?.size == 3)
        assert(gridGraph.getNode(1,1)?.edges?.size == 4)
        assert(gridGraph.getNode(1,2)?.edges?.size == 3)
        assert(gridGraph.getNode(2,0)?.edges?.size == 2)
        assert(gridGraph.getNode(2,1)?.edges?.size == 3)
        assert(gridGraph.getNode(2,2)?.edges?.size == 2)
    }

    @Test
    fun `test grid graph remove node - corner`() {
        val gridGraph = GridGraph(3,3)
        val node = gridGraph.getNode(0,0)!!
        val connectedTo1 = gridGraph.getNode(0,1)!!
        val connectedTo2 = gridGraph.getNode(1,0)!!

        assert(gridGraph.getNode(0,0)?.edges?.size == 2)
        assert(gridGraph.getNode(0,1)?.edges?.size == 3)
        assert(gridGraph.getNode(1,0)?.edges?.size == 3)
        assert(node.edges[connectedTo1.name]!!.connected)
        assert(node.edges[connectedTo2.name]!!.connected)

        gridGraph.removeNode(Pair(0,0))

        assert(gridGraph.getNode(0,0)?.edges?.size == 2)
        assert(gridGraph.getNode(0,1)?.edges?.size == 3)
        assert(gridGraph.getNode(1,0)?.edges?.size == 3)
        assert(!node.edges[connectedTo1.name]!!.connected)
        assert(!node.edges[connectedTo2.name]!!.connected)
    }

    @Test
    fun `test grid graph remove node - middle`() {
        val gridGraph = GridGraph(3,3)
        val node = gridGraph.getNode(1,1)!!
        val connectedTo1 = gridGraph.getNode(1,0)!!
        val connectedTo2 = gridGraph.getNode(1,2)!!
        val connectedTo3 = gridGraph.getNode(0,1)!!
        val connectedTo4 = gridGraph.getNode(2,1)!!

        assert(gridGraph.getNode(1,1)?.edges?.size == 4)
        assert(gridGraph.getNode(0,1)?.edges?.size == 3)
        assert(gridGraph.getNode(1,0)?.edges?.size == 3)
        assert(gridGraph.getNode(1,2)?.edges?.size == 3)
        assert(gridGraph.getNode(2,1)?.edges?.size == 3)
        assert(node.edges[connectedTo1.name]!!.connected)
        assert(node.edges[connectedTo2.name]!!.connected)
        assert(node.edges[connectedTo3.name]!!.connected)
        assert(node.edges[connectedTo4.name]!!.connected)

        gridGraph.removeNode(Pair(1,1))

        assert(gridGraph.getNode(1,1)?.edges?.size == 4)
        assert(gridGraph.getNode(0,1)?.edges?.size == 3)
        assert(gridGraph.getNode(1,0)?.edges?.size == 3)
        assert(gridGraph.getNode(1,2)?.edges?.size == 3)
        assert(gridGraph.getNode(2,1)?.edges?.size == 3)
        assert(!node.edges[connectedTo1.name]!!.connected)
        assert(!node.edges[connectedTo2.name]!!.connected)
        assert(!node.edges[connectedTo3.name]!!.connected)
        assert(!node.edges[connectedTo4.name]!!.connected)
    }

    @Test
    fun `test grid graph readd node - corner`() {
        val gridGraph = GridGraph(3,3)
        val node = gridGraph.getNode(0,0)!!
        val connectedTo1 = gridGraph.getNode(0,1)!!
        val connectedTo2 = gridGraph.getNode(1,0)!!

        assert(gridGraph.getNode(0,0)?.edges?.size == 2)
        assert(gridGraph.getNode(0,1)?.edges?.size == 3)
        assert(gridGraph.getNode(1,0)?.edges?.size == 3)
        assert(node.edges[connectedTo1.name]!!.connected)
        assert(node.edges[connectedTo2.name]!!.connected)

        gridGraph.removeNode(Pair(0,0))

        assert(gridGraph.getNode(0,0)?.edges?.size == 2)
        assert(gridGraph.getNode(0,1)?.edges?.size == 3)
        assert(gridGraph.getNode(1,0)?.edges?.size == 3)
        assert(!node.edges[connectedTo1.name]!!.connected)
        assert(!node.edges[connectedTo2.name]!!.connected)

        gridGraph.readdNode(Pair(0,0))

        assert(gridGraph.getNode(0,0)?.edges?.size == 2)
        assert(gridGraph.getNode(0,1)?.edges?.size == 3)
        assert(gridGraph.getNode(1,0)?.edges?.size == 3)
        assert(node.edges[connectedTo1.name]!!.connected)
        assert(node.edges[connectedTo2.name]!!.connected)
    }

    @Test
    fun `test grid graph readd node - middle`() {
        val gridGraph = GridGraph(3,3)
        val node = gridGraph.getNode(1,1)!!
        val connectedTo1 = gridGraph.getNode(1,0)!!
        val connectedTo2 = gridGraph.getNode(1,2)!!
        val connectedTo3 = gridGraph.getNode(0,1)!!
        val connectedTo4 = gridGraph.getNode(2,1)!!

        assert(gridGraph.getNode(1,1)?.edges?.size == 4)
        assert(gridGraph.getNode(0,1)?.edges?.size == 3)
        assert(gridGraph.getNode(1,0)?.edges?.size == 3)
        assert(gridGraph.getNode(1,2)?.edges?.size == 3)
        assert(gridGraph.getNode(2,1)?.edges?.size == 3)
        assert(node.edges[connectedTo1.name]!!.connected)
        assert(node.edges[connectedTo2.name]!!.connected)
        assert(node.edges[connectedTo3.name]!!.connected)
        assert(node.edges[connectedTo4.name]!!.connected)

        gridGraph.removeNode(Pair(1,1))

        assert(gridGraph.getNode(1,1)?.edges?.size == 4)
        assert(gridGraph.getNode(0,1)?.edges?.size == 3)
        assert(gridGraph.getNode(1,0)?.edges?.size == 3)
        assert(gridGraph.getNode(1,2)?.edges?.size == 3)
        assert(gridGraph.getNode(2,1)?.edges?.size == 3)
        assert(!node.edges[connectedTo1.name]!!.connected)
        assert(!node.edges[connectedTo2.name]!!.connected)
        assert(!node.edges[connectedTo3.name]!!.connected)
        assert(!node.edges[connectedTo4.name]!!.connected)

        gridGraph.readdNode(Pair(1,1))

        assert(gridGraph.getNode(1,1)?.edges?.size == 4)
        assert(gridGraph.getNode(0,1)?.edges?.size == 3)
        assert(gridGraph.getNode(1,0)?.edges?.size == 3)
        assert(gridGraph.getNode(1,2)?.edges?.size == 3)
        assert(gridGraph.getNode(2,1)?.edges?.size == 3)
        assert(node.edges[connectedTo1.name]!!.connected)
        assert(node.edges[connectedTo2.name]!!.connected)
        assert(node.edges[connectedTo3.name]!!.connected)
        assert(node.edges[connectedTo4.name]!!.connected)
    }
}