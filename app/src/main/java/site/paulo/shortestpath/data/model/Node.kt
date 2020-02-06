package site.paulo.shortestpath.data.model

class Node(val name: String) {
    var edges: ArrayList<Edge> = arrayListOf()

    fun connect(node: Node, weight: Int) {
        val edge = Edge(this, node, weight)
        edges.add(edge)
        node.add(edge)
    }

    fun add(edge: Edge) {
        edges.add(edge)
    }
}