package site.paulo.pathfinding.data.model

class Node(val name: String, val position: Pair<Int, Int>) : Comparable<Node> {
    var edges: LinkedHashMap<String, Edge> = LinkedHashMap() //LinkedHashMap because the ordering matters when drawing
    /**
     * Shortest distance from S to V
     */
    var shortestPath = Double.POSITIVE_INFINITY
    var heuristicDistance = 0
    /**
     * Connection to previous node of the shortest distance
     */
    var previous: Edge? = null


    override fun compareTo(other: Node): Int {
        val currentValue = this.shortestPath + this.heuristicDistance
        val valueToCompare = other.shortestPath + other.heuristicDistance

        if (currentValue > valueToCompare) {
            return 1
        } else if (currentValue < valueToCompare) {
            return -1
        } else {
            if (this.heuristicDistance < other.heuristicDistance) return -1
        }

        return 0
    }

    fun connect(nodeToConnect: Node, weight: Double = 1.0) {
        val edge = Edge(this, nodeToConnect, weight)
        this.edges[nodeToConnect.name] = edge
        nodeToConnect.edges[this.name] = edge
    }

    fun reconnect(nodeToReconnect: Node) {
        this.edges[nodeToReconnect.name]?.connected = true
    }

    fun disconnect(nodeToDisconnect: Node) {
        this.edges[nodeToDisconnect.name]?.connected = false
    }

    fun disconnectAll() {
        edges.values.forEach {
            it.connected = false
        }
    }

    fun getAdjacentNodes(): ArrayList<Node> {
        val nodes = ArrayList<Node>()
        for (edge in edges.values) {
            nodes.add(edge.getOpposite(this))
        }

        return nodes
    }

    fun reset() {
        previous = null
        edges.values.forEach{ it.visited = false}
        shortestPath = Double.POSITIVE_INFINITY
        heuristicDistance = 0
    }
}