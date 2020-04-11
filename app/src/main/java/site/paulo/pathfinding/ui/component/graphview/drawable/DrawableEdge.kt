package site.paulo.pathfinding.ui.component.graphview.drawable

import site.paulo.pathfinding.data.model.Edge


class DrawableEdge (val id: Int, var startNode: DrawableNode) {

    var endNode: DrawableNode? = null
    var edge: Edge? = null

    fun connectTo(drawableNode: DrawableNode, weight: Double = Edge.DEFAULT_WEIGHT) {
        endNode = drawableNode
        edge = Edge(startNode, drawableNode, weight)

        startNode.connectedTo[drawableNode.id] = drawableNode
        drawableNode.connectedTo[startNode.id] = startNode
    }
}