package site.paulo.pathfinding.ui.component.graphview.drawable

import android.graphics.RectF

class DrawableNode (val id: Int, var centerX: Float, var centerY: Float) {

    var rect: RectF

    companion object {
        const val DIAMETER = 100f
        const val RADIUS = DIAMETER / 2
    }

    init {
        rect = RectF(
            centerX - RADIUS,
            centerY - RADIUS,
            centerX + RADIUS,
            centerY + RADIUS
        )
    }
}