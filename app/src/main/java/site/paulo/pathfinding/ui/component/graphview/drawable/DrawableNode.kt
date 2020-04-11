package site.paulo.pathfinding.ui.component.graphview.drawable

import android.graphics.RectF
import site.paulo.pathfinding.data.model.Node

class DrawableNode (val id: String, var centerX: Float, var centerY: Float):
    Node(id, Pair(centerX.toInt(), centerY.toInt())) {

    var rect: RectF
    var connectedTo: HashMap<String, DrawableNode> = HashMap()

    companion object {
        const val DIAMETER = 100f
        const val RADIUS = DIAMETER / 2
    }

    init {
        rect = RectF(
            centerX - RADIUS,
            centerY - RADIUS,
            centerX + RADIUS,
            centerY + RADIUS)
    }

    fun updatePosition(x: Float, y: Float) {
        centerX = x;
        centerY = y
        rect.left = centerX - RADIUS
        rect.top = centerY - RADIUS
        rect.right = centerX + RADIUS
        rect.bottom = centerY + RADIUS
    }
}