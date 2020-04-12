package site.paulo.pathfinding.ui.component.graphview.drawable

import android.graphics.Paint
import android.graphics.RectF
import site.paulo.pathfinding.data.model.Edge
import site.paulo.pathfinding.data.model.Node


class DrawableEdge (val id: Int, override val nodeA: DrawableNode) : Edge (nodeA, null) {

    override var nodeB: Node? = null
    val weightBox: RectF = RectF(0f, 0f, 0f, 0f)

    fun connectTo(drawableNode: DrawableNode, paint: Paint, weight: Double = DEFAULT_WEIGHT) {
        nodeB = drawableNode
        nodeA.connectByEdge(drawableNode, this, weight)

        updateWeightBox(paint)
    }

    fun updateWeightBox(paint: Paint) {
        val textCenterX = (nodeA.centerX + (nodeB as DrawableNode).centerX) / 2
        val textCenterY = (nodeA.centerY + (nodeB as DrawableNode).centerY) / 2

        weightBox.left = textCenterX - (paint.measureText(weight.toInt().toString()) / 2) - 20
        weightBox.top = textCenterY - (paint.descent() + paint.ascent())
        weightBox.right = textCenterX + (paint.measureText(weight.toInt().toString()) / 2) + 20
        weightBox.bottom = textCenterY + (paint.descent() + paint.ascent())
    }
}