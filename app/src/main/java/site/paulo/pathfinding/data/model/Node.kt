package site.paulo.pathfinding.data.model

open class Node(val name: String) : Comparable<Node> {

    /**
     * LinkedHashMap (because the ordering matters when drawing) with
     * connected node name as key, and edge as value
     */
    open val edges: LinkedHashMap<String, Edge> = LinkedHashMap()
    /**
     * Shortest distance from S to V
     */
    var shortestPath = Double.POSITIVE_INFINITY
    var heuristicDistance = 0
    /**
     * Connection to previous node of the shortest distance
     */
    var previous: Edge? = null

    var position: Pair<Int, Int> = Pair(-1, -1)


    override fun compareTo(other: Node): Int {
        val currentValue = this.shortestPath + this.heuristicDistance
        val valueToCompare = other.shortestPath + other.heuristicDistance

        if (currentValue > valueToCompare) {
            return 1
        } else if (currentValue < valueToCompare) {
            return -1
        }

        return 0
    }

    open fun connect(nodeToConnect: Node, weight: Double = Edge.DEFAULT_WEIGHT) : Edge {
        val edge = Edge(this, nodeToConnect, weight)
        this.edges[nodeToConnect.name] = edge
        nodeToConnect.edges[this.name] = edge
        return edge
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

    fun reconnectAll() {
        edges.values.forEach {
            it.connected = true
        }
    }

    fun getAdjacentNodes(): ArrayList<Node> {
        val nodes = ArrayList<Node>()
        for (edge in edges.values)
            nodes.add(edge.getOpposite(this))

        return nodes
    }

    fun reset() {
        previous = null
        shortestPath = Double.POSITIVE_INFINITY
        heuristicDistance = 0
    }

    /**
     * Sets the weight of all edges
     */
    fun setAllWeights(amount: Double) {
        edges.values.forEach {
            it.weight = amount
        }
    }

}