package site.paulo.shortestpath.data.model

class Edge(val nodeA: Node, val nodeB: Node, val weight: Double) {

    var visited: Boolean = false

    /**
     * Given one node, return the other one connect to it
     */
    fun getOpposite(node: Node): Node {
        if(node == nodeA)
            return nodeB

        return nodeA
    }

}