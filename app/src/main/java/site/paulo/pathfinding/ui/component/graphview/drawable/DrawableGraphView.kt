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
import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableItems.*

class DrawableGraphView : View {

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    private var selectedOption: DrawableItems = DrawableItems.NODE
    private var listeners: ArrayList<GraphListener> = ArrayList()

    private val drawableNodes: ArrayList<DrawableNode> = ArrayList()
    private val drawableEdges: ArrayList<DrawableEdge> = ArrayList()

    private val paint = Paint()
    // --------- colors ---------
    private val colorStartNode: Int = ContextCompat.getColor(context, R.color.colorStartPoint)
    private val colorEndNode: Int = ContextCompat.getColor(context, R.color.colorEndPoint)
    private val colorNode: Int = ContextCompat.getColor(context, R.color.colorNode)
    private val colorNodeText: Int = ContextCompat.getColor(context, R.color.colorNodeText)
    private val colorEdge: Int = ContextCompat.getColor(context, R.color.colorEdge)
    // --------------------------

    init {
        configurePaint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBounderies(canvas)
        drawEdges(canvas)
        drawNodes(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = if (measuredWidth > measuredHeight) measuredHeight else measuredWidth
        setMeasuredDimension(size, size)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return false

        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                when (selectedOption) {
                    NODE -> addDrawableNode(x ,y)
                    EDGE -> addDrawableEdge(x, y)
                }
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

    private fun addDrawableEdge(x: Float, y: Float) {
        val node = getDrawableNodeAtPoint(x, y)
        if (node != null) {
            if (drawableEdges.isEmpty() || drawableEdges.last().endNode != null) {
                drawableEdges.add(DrawableEdge(drawableEdges.size + 1, node))
            } else {
                if (drawableEdges.last().startNode != node)
                    drawableEdges.last().connectTo(node)
            }
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

    private fun getDrawableNodeAtPoint(x: Float, y: Float): DrawableNode? {
        val touchedPoint = RectF(x - DrawableNode.RADIUS, y - DrawableNode.RADIUS,
            x + DrawableNode.RADIUS, y + DrawableNode.RADIUS)
        for (n in drawableNodes) {
            if (touchedPoint.intersect(n.rect))
                return n
        }
        return null
    }

    private fun drawBounderies(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        canvas.drawRect(1f, 1f, width - 1f, height - 1f, paint)
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

    private fun drawEdges(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = resources.displayMetrics.density * 2
        for (edge in drawableEdges) {
            paint.color = colorEdge
            val startNode = edge.startNode
            val endNode = edge.endNode
            if (endNode != null) {
                canvas.drawLine(edge.startNode.centerX, edge.startNode.centerY,
                    endNode.centerX, endNode.centerY, paint)
            } else {
                paint.color = Color.RED
                canvas.drawCircle(startNode.centerX, startNode.centerY, DrawableNode.RADIUS + 4, paint)
            }
        }
        paint.strokeWidth = resources.displayMetrics.density
    }

    private fun configurePaint() {
        paint.isAntiAlias = true
        paint.strokeWidth = resources.displayMetrics.density
        paint.textSize = 48f
    }

    fun registerListener(listener: GraphListener) {
        listeners.add(listener)
    }

    fun setDrawableType(newOption: DrawableItems) {
        selectedOption = newOption
    }

}