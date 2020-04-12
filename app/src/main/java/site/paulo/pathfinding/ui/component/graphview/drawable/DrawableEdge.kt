package site.paulo.pathfinding.ui.component.graphview.drawable

import android.graphics.RectF
import site.paulo.pathfinding.data.model.Edge
import site.paulo.pathfinding.data.model.Node


class DrawableEdge (val id: Int, override val nodeA: DrawableNode) : Edge (nodeA, null) {

    override var nodeB: Node? = null
    val weightBox: RectF = RectF(0f, 0f, 0f, 0f)

    fun connectTo(drawableNode: DrawableNode, weight: Double = Edge.DEFAULT_WEIGHT) {
        nodeB = drawableNode
        nodeA.connectByEdge(drawableNode, this, weight)
    }
}