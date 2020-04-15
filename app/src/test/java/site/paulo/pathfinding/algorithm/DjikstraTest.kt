package test.site.paulo.pathfinding.algorithm

import org.junit.jupiter.api.Test
import site.paulo.pathfinding.algorithm.Djikstra
import site.paulo.pathfinding.data.model.Node
import java.util.*

internal class DjikstraTest {

    @Test
    fun `test Djikstra algorithm - shortest`() {
        val nodes = LinkedList<Node>()
        nodes.add(Node("1"))
        nodes.add(Node("2"))
        nodes.add(Node("3"))

        nodes[0].connect(nodes[1])
        nodes[1].connect(nodes[2])

        val djikstra = Djikstra(nodes, nodes[0], nodes[2])

        djikstra.run()

        assert(djikstra.getPath().size == 3)
        assert(djikstra.getPath()[0] == nodes[2])
        assert(djikstra.getPath()[1] == nodes[1])
        assert(djikstra.getPath()[2] == nodes[0])
    }

    @Test
    fun `test Djikstra algorithm - reverse`() {
        val nodes = LinkedList<Node>()
        nodes.add(Node("1"))
        nodes.add(Node("2"))
        nodes.add(Node("3"))

        nodes[0].connect(nodes[1])
        nodes[1].connect(nodes[2])

        val djikstra = Djikstra(nodes, nodes[2], nodes[0])

        djikstra.run()

        assert(djikstra.getPath().size == 3)
        assert(djikstra.getPath()[0] == nodes[0])
        assert(djikstra.getPath()[1] == nodes[1])
        assert(djikstra.getPath()[2] == nodes[2])
    }

    @Test
    fun `test Djikstra algorithm - circle`() {
        val nodes = LinkedList<Node>()
        nodes.add(Node("1"))
        nodes.add(Node("2"))
        nodes.add(Node("3"))

        nodes[0].connect(nodes[1], 10.0)
        nodes[1].connect(nodes[2])
        nodes[2].connect(nodes[0])

        val djikstra = Djikstra(nodes, nodes[0], nodes[2])

        djikstra.run()

        assert(djikstra.getPath().size == 2)
        assert(djikstra.getPath()[0] == nodes[2])
        assert(djikstra.getPath()[1] == nodes[0])
    }

    @Test
    fun `test Djikstra algorithm - circle 2`() {
        val nodes = LinkedList<Node>()
        nodes.add(Node("1"))
        nodes.add(Node("2"))
        nodes.add(Node("3"))

        nodes[0].connect(nodes[1])
        nodes[1].connect(nodes[2])
        nodes[2].connect(nodes[0], 10.0)

        val djikstra = Djikstra(nodes, nodes[0], nodes[2])

        djikstra.run()

        assert(djikstra.getPath().size == 3)
        assert(djikstra.getPath()[0] == nodes[2])
        assert(djikstra.getPath()[1] == nodes[1])
        assert(djikstra.getPath()[2] == nodes[0])
    }

}