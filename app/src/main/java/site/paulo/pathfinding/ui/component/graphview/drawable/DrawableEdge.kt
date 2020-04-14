package site.paulo.pathfinding.ui.component.graphview.drawable

import android.graphics.Paint
import android.graphics.RectF
import site.paulo.pathfinding.data.model.Edge.Companion.DEFAULT_WEIGHT


class DrawableEdge (val id: Int, val nodeA: DrawableNode,
    val nodeB: DrawableNode) {

    val weightBox: RectF = RectF(0f, 0f, 0f, 0f)
    val touchableArea: RectF = RectF(0f, 0f, 0f, 0f)
    var weight: Double = .0
    private val spacing: Int = 20

    fun connectTo(drawableNode: DrawableNode, paint: Paint, weight: Double = DEFAULT_WEIGHT) {
        nodeA.connectByEdge(drawableNode, this, weight)
        updateWeightBox(paint)
    }

    fun updateWeightBox(paint: Paint) {
        val textCenterX = (nodeA.centerX + nodeB.centerX) / 2
        val textCenterY = (nodeA.centerY + nodeB.centerY) / 2

        weightBox.left = textCenterX - (paint.measureText(weight.toInt().toString()) / 2) - spacing
        weightBox.top = textCenterY - (paint.descent() + paint.ascent())
        weightBox.right = textCenterX + (paint.measureText(weight.toInt().toString()) / 2) + spacing
        weightBox.bottom = textCenterY + (paint.descent() + paint.ascent())

        touchableArea.left = textCenterX - spacing
        touchableArea.top = textCenterY - spacing
        touchableArea.right = textCenterX + spacing
        touchableArea.bottom = textCenterY + spacing
    }
}