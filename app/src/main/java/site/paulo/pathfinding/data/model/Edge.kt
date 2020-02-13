package site.paulo.pathfinding.data.model

class Edge(val nodeA: Node, val nodeB: Node, val weight: Double) {

    var visited: Boolean = false
    /**
     * Connection between nodes is valid or not
     */
    var connected: Boolean = true

    /**
     * Given one node, return the other one connect to it
     */
    fun getOpposite(node: Node): Node {
        if(node == nodeA)
            return nodeB

        return nodeA
    }

}