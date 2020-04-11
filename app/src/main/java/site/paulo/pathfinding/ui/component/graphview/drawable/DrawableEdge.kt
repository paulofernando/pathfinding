package site.paulo.pathfinding.ui.component.graphview.drawable

import site.paulo.pathfinding.data.model.Edge


class DrawableEdge (val id: Int, var startNode: DrawableNode) {

    var endNode: DrawableNode? = null
    var weight: Double = .0

    fun connectTo(drawableNode: DrawableNode, weight: Double = Edge.DEFAULT_WEIGHT) {
        endNode = drawableNode
        this.weight = weight

        startNode.connect(drawableNode, weight)
        startNode.connectedTo[drawableNode.id] = drawableNode
        drawableNode.connectedTo[startNode.id] = startNode
    }
}