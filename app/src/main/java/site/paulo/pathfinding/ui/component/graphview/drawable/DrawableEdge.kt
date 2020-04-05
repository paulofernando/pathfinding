package site.paulo.pathfinding.ui.component.graphview.drawable


class DrawableEdge (val id: Int, var startNode: DrawableNode) {

    var endNode: DrawableNode? = null

    init {
        startNode.connectedAmount++
    }

    fun connectTo(node: DrawableNode) {
        node.connectedAmount++
        endNode = node
    }
}