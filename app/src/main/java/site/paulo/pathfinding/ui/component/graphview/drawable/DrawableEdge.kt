package site.paulo.pathfinding.ui.component.graphview.drawable

import site.paulo.pathfinding.data.model.Edge


class DrawableEdge (val id: Int, var startNode: DrawableNode) {

    var endNode: DrawableNode? = null
    var edge: Edge? = null

    init {
        startNode.connectedAmount++
    }

    fun connectTo(drawableNode: DrawableNode, weight: Double = 1.0) {
        drawableNode.connectedAmount++
        endNode = drawableNode
        edge = Edge(startNode.node, drawableNode.node, weight)
    }
}