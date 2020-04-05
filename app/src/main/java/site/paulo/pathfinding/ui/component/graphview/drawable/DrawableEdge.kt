package site.paulo.pathfinding.ui.component.graphview.drawable


class DrawableEdge (val id: Int, var startNode: DrawableNode) {

    var endNode: DrawableNode? = null
    var weight: Int  = 1

    init {
        startNode.connectedAmount++
    }

    fun connectTo(node: DrawableNode, weight: Int = 1) {
        node.connectedAmount++
        endNode = node
    }
}