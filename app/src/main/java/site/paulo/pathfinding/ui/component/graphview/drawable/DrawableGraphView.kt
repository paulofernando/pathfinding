package site.paulo.pathfinding.ui.component.graphview.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.collections.ArrayList
import site.paulo.pathfinding.R
import site.paulo.pathfinding.ui.component.graphview.GraphListener

class DrawableGraphView : View {

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    private var listeners: ArrayList<GraphListener> = ArrayList()

    private val drawableNodes: ArrayList<DrawableNode> = ArrayList()

    private val paint = Paint()
    // --------- colors ---------
    private val colorStartNode: Int = ContextCompat.getColor(context, R.color.colorStartPoint)
    private val colorEndNode: Int = ContextCompat.getColor(context, R.color.colorEndPoint)
    private val colorNode: Int = ContextCompat.getColor(context, R.color.colorNode)
    private val colorNodeText: Int = ContextCompat.getColor(context, R.color.colorNodeText)
    // --------------------------

    init {
        configurePaint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawNodes(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return false

        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                addDrawableNode(x ,y)
            }
            MotionEvent.ACTION_MOVE -> {

            }
            MotionEvent.ACTION_UP -> {

            }
            MotionEvent.ACTION_CANCEL -> { }
        }
        return true
    }

    fun reset() {
        listeners.forEach { it.onGraphNotReady() }
        listeners.forEach { it.onGraphNotCleanable() }
    }

    private fun addDrawableNode(x: Float, y: Float) {
        val node = DrawableNode(drawableNodes.size + 1, x, y)
        if (!hasCollision(node)) {
            drawableNodes.add(node)
            invalidate()
        }
    }

    private fun hasCollision(node: DrawableNode): Boolean {
        for (n in drawableNodes) {
            if (node.rect.intersect(n.rect))
                return true
        }
        return false
    }

    private fun drawNodes(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        for (node in drawableNodes) {
            paint.color = colorNode
            canvas.drawCircle(node.centerX, node.centerY, DrawableNode.RADIUS, paint)
            paint.color = colorNodeText
            canvas.drawText(
                node.id.toString(), node.centerX - paint.measureText(node.id.toString()) / 2,
                    node.centerY - ((paint.descent() + paint.ascent()) / 2), paint
            )
        }
    }

    private fun configurePaint() {
        paint.isAntiAlias = true
        paint.strokeWidth = resources.displayMetrics.density
        paint.textSize = 48f
    }

    fun registerListener(listener: GraphListener) {
        listeners.add(listener)
    }

}