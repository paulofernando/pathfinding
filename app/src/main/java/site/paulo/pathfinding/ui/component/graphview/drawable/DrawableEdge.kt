package site.paulo.pathfinding.ui.component.graphview.drawable


class DrawableEdge (val id: Int, var startNode: DrawableNode) {

    var endNode: DrawableNode? = null

    fun connectTo(node: DrawableNode) {
        endNode = node
    }
}