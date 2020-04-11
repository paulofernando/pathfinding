package site.paulo.pathfinding.ui.component.graphview.drawable

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.collections.ArrayList
import site.paulo.pathfinding.R
import site.paulo.pathfinding.algorithm.*
import site.paulo.pathfinding.data.model.DrawableGraph
import site.paulo.pathfinding.data.model.Node
import site.paulo.pathfinding.ui.component.graphview.GraphListener
import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableItems.*
import site.paulo.pathfinding.data.model.PathFindingAlgorithms.*
import java.util.*

class DrawableGraphView : View {

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    private var selectedOption: DrawableItems = DrawableItems.NODE
    private var listeners: ArrayList<GraphListener> = ArrayList()

    private val drawableEdges: ArrayList<DrawableEdge> = ArrayList()
    private var startPoint: DrawableNode? = null
    private var endPoint: DrawableNode? = null
    private lateinit var selectedNode: DrawableNode

    private var graph: DrawableGraph = DrawableGraph()
    private lateinit var algorithm: PathFindingAlgorithm
    private val pathPositions: ArrayList<Node> = ArrayList()
    private val visitedNodesOrder: Stack<Node> = Stack()

    private val paint = Paint()
    // --------- colors ---------
    private val colorStartNode: Int = ContextCompat.getColor(context, R.color.colorStartPoint)
    private val colorEndNode: Int = ContextCompat.getColor(context, R.color.colorEndPoint)
    private val colorNode: Int = ContextCompat.getColor(context, R.color.colorNode)
    private val colorDrawablePath: Int = ContextCompat.getColor(context, R.color.colorDrawablePath)
    private val colorNodeText: Int = ContextCompat.getColor(context, R.color.colorNodeText)
    private val colorEdge: Int = ContextCompat.getColor(context, R.color.colorEdge)
    private val colorTextWeight: Int = ContextCompat.getColor(context, R.color.colorTextWeight)
    private val colorBoxWeight: Int = ContextCompat.getColor(context, R.color.colorBoxWeight)
    // --------------------------

    init {
        configurePaint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBoundaries(canvas)
        drawEdges(canvas)
        drawWeights(canvas)
        drawNodes(canvas)
        if (visitedNodesOrder.isNotEmpty())
            drawVisitedNodes(canvas)
        drawStartAndEndPoints(canvas)
        drawTextNodes(canvas)
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
                    NODE -> {
                        val node = getDrawableNodeAtPoint(x, y)
                        if (node == null) {
                            addDrawableNode(x, y)
                        } else {
                            selectedNode = node
                        }
                    }
                    EDGE -> addDrawableEdge(x, y)
                    SELECT -> selectDrawableNode(x, y)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                when (selectedOption) {
                    NODE -> {
                        moveNode(selectedNode, x, y)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {

            }
            MotionEvent.ACTION_CANCEL -> { }
        }
        return true
    }

    fun runAlgorithm() {
        if (startPoint == null || endPoint == null) return

        pathPositions.clear()
        visitedNodesOrder.clear()

        algorithm = Djikstra(graph.getNodes() as LinkedList<Node>,
            startPoint as Node,
            endPoint as Node)
        algorithm.run()

        val path = algorithm.getPath()
        while (path.isNotEmpty())
            visitedNodesOrder.add(path.pop())

        invalidate()
    }

    private fun addDrawableNode(x: Float, y: Float) {
        val node = DrawableNode((graph.getNodes().size + 1).toString(), x, y)
        if (!hasCollision(node)) {
            graph.addNode(node)
            selectedNode = node
            invalidate()
        }
        listeners.forEach { it.onGraphCleanable() }
    }

    private fun addDrawableEdge(x: Float, y: Float) {
        val node = getDrawableNodeAtPoint(x, y)
        if (node != null) {
            if (drawableEdges.isNotEmpty() && drawableEdges.last().endNode == null) { //
                if (drawableEdges.last().startNode == node)
                    return
            }

            if (node.connectedTo.size < graph.getNodes().size - 1) {
                if (drawableEdges.isEmpty() || drawableEdges.last().endNode != null) {
                    drawableEdges.add(DrawableEdge(drawableEdges.size + 1, node))
                } else {
                    if (drawableEdges.last().startNode != node)
                        drawableEdges.last().connectTo(node)
                }
                invalidate()
            }
        }
    }

    private fun selectDrawableNode(x: Float, y: Float) {
        val node = getDrawableNodeAtPoint(x, y)
        if (node != null) {
            if (startPoint == null) {
                startPoint = node
                invalidate()
            } else if (endPoint == null) {
                endPoint = node
                listeners.forEach { it.onGraphReady() }
                invalidate()
            }
        }
    }

    private fun moveNode(selectedNode: DrawableNode, x: Float, y: Float) {
        val tempX = selectedNode.centerX
        val tempY = selectedNode.centerY
        selectedNode.updatePosition(x, y)
        if (hasCollision(selectedNode)) {
            selectedNode.updatePosition(tempX, tempY)
        } else {
            invalidate()
        }
    }

    private fun hasCollision(node: DrawableNode): Boolean {
        for (n in graph.getNodes()) {
            if (n == node) continue
            if (node.rect.intersect(n.rect))
                return true
        }
        return false
    }

    private fun getDrawableNodeAtPoint(x: Float, y: Float): DrawableNode? {
        val touchedPoint = RectF(x - DrawableNode.RADIUS, y - DrawableNode.RADIUS,
            x + DrawableNode.RADIUS, y + DrawableNode.RADIUS)
        for (n in graph.getNodes()) {
            if (touchedPoint.intersect(n.rect))
                return n
        }
        return null
    }

    private fun drawBoundaries(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        canvas.drawRect(1f, 1f, width - 1f, height - 1f, paint)
    }

    private fun drawNodes(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = colorNode
        for (node in graph.getNodes())
            drawNode(node, canvas)
    }

    private fun drawNode(node: DrawableNode, canvas: Canvas) {
        canvas.drawCircle(node.centerX, node.centerY, DrawableNode.RADIUS, paint)
    }

    private fun drawVisitedNodes(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = colorDrawablePath
        for (node in visitedNodesOrder) {
            val drawableNode = graph.getNode(node.name)
            if (drawableNode != null)
                drawNode(drawableNode, canvas)
        }
    }

    private fun drawStartAndEndPoints(canvas: Canvas) {
        if (startPoint != null) {
            paint.style = Paint.Style.FILL
            paint.color = colorStartNode
            canvas.drawCircle(startPoint!!.centerX, startPoint!!.centerY, DrawableNode.RADIUS, paint)
            if (endPoint != null) {
                paint.color = colorEndNode
                canvas.drawCircle(endPoint!!.centerX, endPoint!!.centerY, DrawableNode.RADIUS, paint)
            }
        }
    }

    private fun drawTextNodes(canvas: Canvas) {
        paint.color = colorNodeText
        for (node in graph.getNodes()) {
            drawTextNode(node, canvas)
        }
    }

    private fun drawTextNode(node: DrawableNode, canvas: Canvas) {
        canvas.drawText(
            node.id,
            node.centerX - paint.measureText(node.id) / 2,
            node.centerY - ((paint.descent() + paint.ascent()) / 2), paint
        )
    }

    private fun drawEdges(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = resources.displayMetrics.density * 2
        for (edge in drawableEdges) {
            val firstNode = edge.startNode
            val secondNode = edge.endNode
            if (secondNode != null) {
                paint.color = colorEdge
                canvas.drawLine(edge.startNode.centerX, edge.startNode.centerY,
                    secondNode.centerX, secondNode.centerY, paint)
            } else {
                paint.color = Color.RED
                canvas.drawCircle(firstNode.centerX, firstNode.centerY, DrawableNode.RADIUS + 4, paint)
            }
        }

        paint.strokeWidth = resources.displayMetrics.density
    }

    private fun drawWeights(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        for (drawableEdge in drawableEdges) {
            val startNode = drawableEdge.startNode
            val endNode = drawableEdge.endNode
            if (endNode != null) {
                val textCenterX = (startNode.centerX + endNode.centerX) / 2
                val textCenterY = (startNode.centerY + endNode.centerY) / 2
                val textWeight = drawableEdge.weight.toInt().toString()
                paint.color = colorBoxWeight
                canvas.drawRoundRect(
                    RectF(textCenterX - (paint.measureText(textWeight) / 2) - 20,
                    textCenterY - (paint.descent() + paint.ascent()),
                    textCenterX + (paint.measureText(textWeight) / 2) + 20,
                    textCenterY + (paint.descent() + paint.ascent())),
                    15f,
                    15f,
                    paint
                )

                paint.color = colorTextWeight
                canvas.drawText(textWeight, textCenterX - (paint.measureText(textWeight) / 2),
                    textCenterY - ((paint.descent() + paint.ascent()) / 2), paint)
            }
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

    fun setDrawableType(newOption: DrawableItems) {
        selectedOption = newOption
    }

    fun reset() {
        graph = DrawableGraph()
        startPoint = null
        endPoint = null
        pathPositions.clear()
        drawableEdges.clear()
        invalidate()

        listeners.forEach { it.onGraphNotReady() }
        listeners.forEach { it.onGraphNotCleanable() }
    }


}