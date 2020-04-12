package site.paulo.pathfinding.data.model

open class Edge(open val nodeA: Node, open var nodeB: Node?, var weight: Double = DEFAULT_WEIGHT) {

    companion object {
        const val DEFAULT_WEIGHT = 1.0
    }

    var visited: Boolean = false
    /**
     * Connection between nodes is valid or not
     */
    var connected: Boolean = true

    /**
     * Given one node, return the other one connect to it
     */
    fun getOpposite(node: Node): Node? {
        if(node == nodeA)
            return nodeB

        return nodeA
    }

}