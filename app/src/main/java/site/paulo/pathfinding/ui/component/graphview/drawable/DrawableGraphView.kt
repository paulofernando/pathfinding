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
import site.paulo.pathfinding.data.model.PathFindingAlgorithms
import site.paulo.pathfinding.data.model.PathFindingAlgorithms.*
import site.paulo.pathfinding.ui.component.graphview.GraphListener
import java.util.*

class DrawableGraphView : View {

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    private val touchableSpace: Float = 10f
    private var selectedOption: PathFindingAlgorithms = DJIKSTRA
    private var listeners: ArrayList<GraphListener> = ArrayList()

    private val drawableEdges: ArrayList<DrawableEdge> = ArrayList()
    private var startPoint: DrawableNode? = null
    private var endPoint: DrawableNode? = null
    private var selectedNode: DrawableNode? = null
    private var readyToAddEdges: Boolean = false
    private var readyToAddStartAndEndNodes: Boolean = false
    private var readyToRunAgain: Boolean = false

    private var graph: DrawableGraph = DrawableGraph()
    private lateinit var algorithm: PathFindingAlgorithm
    private val pathPositions: ArrayList<Node> = ArrayList()
    private val visitedNodesOrder: Stack<Node> = Stack()
    private var selectedAlgorithm: PathFindingAlgorithms = DJIKSTRA

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
    private val colorSelectedNode: Int = ContextCompat.getColor(context, R.color.colorSelectedNode)
    private val colorBoundaries: Int = ContextCompat.getColor(context, R.color.colorBoundaries)
    // --------------------------

    init {
        configurePaint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBoundaries(canvas)
        drawEdges(canvas)
        drawNodes(canvas)
        if (selectedAlgorithm == DJIKSTRA) {
            drawWeights(canvas)
        }
        if (visitedNodesOrder.isNotEmpty()) {
            drawVisitedNodes(canvas)
            drawVisitedEdges(canvas)
            if (selectedAlgorithm == DJIKSTRA) {
                drawVisitedWeights(canvas)
            }
        }
        drawStartAndEndPoints(canvas)
        drawTextNodes(canvas)
        drawSelectedNode(canvas)
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
                if (selectedNode == null) {
                    selectedNode = getDrawableNodeAtPoint(x, y)
                } else {
                    val nodeB = getDrawableNodeAtPoint(x, y)

                    if(nodeB == null) { //user clicks on an empty area after choose first node
                        selectedNode = null
                        return true
                    }

                    if (selectedNode != nodeB) { //user connect nodes
                        addDrawableEdge(selectedNode!!, nodeB)
                        selectedNode = null
                        return true
                    }

                    if (selectedNode == nodeB) { //user clicks on the same selected node
                        selectDrawableNode(x, y)
                        selectedNode = null
                        return true
                    }
                }

                if (selectedNode == null) {
                    val edge = getEdgeBoxAtPoint(x, y)
                    if (edge != null) {
                        increaseEdgeWeight(edge)
                    } else {
                        addDrawableNode(x, y)
                        readyToAddEdges = false
                    }
                } else {
                    readyToAddEdges = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val node = selectedNode ?: return false
                moveNode(node, x, y)
                readyToAddEdges = false
            }
            MotionEvent.ACTION_UP -> {
                if (!readyToAddEdges && !readyToAddStartAndEndNodes) {
                    selectedNode = null
                }
                invalidate()
            }
            MotionEvent.ACTION_CANCEL -> { }
        }
        return true
    }

    fun setAlgorithm(alg: PathFindingAlgorithms) {
        selectedAlgorithm = alg
        invalidate()
        if(readyToRunAgain) {
            runAlgorithm()
        }
    }

    fun runAlgorithm() {
        if (startPoint == null || endPoint == null) return

        pathPositions.clear()
        visitedNodesOrder.clear()

        val nodes = graph.getNodes() as LinkedList<Node>
        val nodeA = startPoint as Node
        val nodeB = endPoint as Node
        algorithm = when (selectedAlgorithm) {
            DJIKSTRA -> Djikstra(nodes, nodeA, nodeB)
            BREADTH_FIRST -> BreadthFirst(nodeA, nodeB)
            else -> DepthFirst(nodeA, nodeB)
        }

        algorithm.run()
        readyToRunAgain = true

        val path = algorithm.getPath()
        while (path.isNotEmpty())
            visitedNodesOrder.add(path.pop())

        invalidate()
    }

    fun isReadyToRun(): Boolean {
        return (startPoint != null && endPoint != null)
    }

    private fun increaseEdgeWeight(drawableEdge: DrawableEdge) {
        if (selectedAlgorithm == DJIKSTRA) {
            drawableEdge.increaseWeight(1.0)
            invalidate()
            if(readyToRunAgain) {
                runAlgorithm()
            }
        }
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

    private fun addDrawableEdge(nodeA: DrawableNode, nodeB: DrawableNode) {
        if (nodeA.connectedTo.size < graph.getNodes().size - 1) {
            drawableEdges.add(DrawableEdge(drawableEdges.size + 1, nodeA, nodeB))
            drawableEdges.last().connectTo(nodeB, paint)
            invalidate()
            if(readyToRunAgain) {
                runAlgorithm()
            }
        }
    }

    private fun selectDrawableNode(x: Float, y: Float) {
        val node = getDrawableNodeAtPoint(x, y) ?: return

        if (startPoint == node) { //deselect start point
            startPoint = null
            if(endPoint != null) {
                listeners.forEach { it.onGraphNotReady() }
                pathPositions.clear()
                visitedNodesOrder.clear()
            }
            invalidate()
            return
        } else if (endPoint == node) { //deselect end point
            endPoint = null
            if(startPoint != null) {
                listeners.forEach { it.onGraphNotReady() }
                pathPositions.clear()
                visitedNodesOrder.clear()
            }
            invalidate()
            return
        }

        if (startPoint == null) {
            startPoint = node
            if(endPoint != null) listeners.forEach { it.onGraphReady() }
            invalidate()
        } else if (endPoint == null) {
            endPoint = node
            if(startPoint != null) listeners.forEach { it.onGraphReady() }
            invalidate()
        }

    }

    private fun moveNode(selectedNode: DrawableNode, x: Float, y: Float) {
        val tempX = selectedNode.centerX
        val tempY = selectedNode.centerY
        selectedNode.updatePosition(x, y)
        if (hasCollision(selectedNode)) {
            selectedNode.updatePosition(tempX, tempY)
        } else {
            for(edge in selectedNode.connectedByEdge.values) {
                edge.updateWeightBox(paint)
            }
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
        val touchedPoint = RectF(x - touchableSpace, y - touchableSpace,
            x + touchableSpace, y + touchableSpace)
        for (n in graph.getNodes()) {
            if (touchedPoint.intersect(n.rect))
                return n
        }
        return null
    }

    private fun getEdgeBoxAtPoint(x: Float, y: Float): DrawableEdge? {
        val touchedPoint = RectF(x - touchableSpace, y - touchableSpace,
            x + touchableSpace, y + touchableSpace)
        for (e in drawableEdges) {
            if (touchedPoint.intersect(e.touchableArea))
                return e
        }
        return null
    }

    private fun drawBoundaries(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.color = colorBoundaries

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

    private fun drawSelectedNode(canvas: Canvas) {
        val node = selectedNode ?: return
        paint.style = Paint.Style.STROKE
        paint.color = colorSelectedNode

        drawNode(node, canvas)
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
        val startNode = startPoint
        if (startNode != null) {
            paint.style = Paint.Style.FILL
            paint.color = colorStartNode
            canvas.drawCircle(startNode.centerX, startNode.centerY, DrawableNode.RADIUS, paint)
        }

        val endNode = endPoint ?: return
        paint.color = colorEndNode
        canvas.drawCircle(endNode.centerX, endNode.centerY, DrawableNode.RADIUS, paint)
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
        paint.color = colorEdge
        paint.strokeWidth = resources.displayMetrics.density * 2

        for (edge in drawableEdges) {
            drawEdge(edge.nodeA, edge.nodeB, canvas)
        }
        paint.strokeWidth = resources.displayMetrics.density
    }

    private fun drawEdge(nodeA: DrawableNode, nodeB: DrawableNode, canvas: Canvas) {
        canvas.drawLine(nodeA.centerX, nodeA.centerY, nodeB.centerX, nodeB.centerY, paint)
    }

    private fun drawVisitedEdges(canvas: Canvas) {
        var currentNode = visitedNodesOrder.get(index = 0) as DrawableNode
        paint.color = colorDrawablePath
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = resources.displayMetrics.density * 2
        for (i in 1 until visitedNodesOrder.size) {
            val nodeB = visitedNodesOrder.get(index = i) as DrawableNode
            drawEdge(currentNode, nodeB, canvas)
            currentNode = visitedNodesOrder.get(index = i) as DrawableNode
        }
        invalidate()
    }

    private fun drawVisitedWeights(canvas: Canvas) {
        var currentNode = visitedNodesOrder.get(index = 0) as DrawableNode
        paint.textSize /= 1.5f
        paint.style = Paint.Style.FILL
        paint.strokeWidth = resources.displayMetrics.density
        for (i in 1 until visitedNodesOrder.size) {
            val nodeB = visitedNodesOrder.get(index = i) as DrawableNode
            drawWeight(currentNode, nodeB, currentNode.edges[nodeB.id]!!.weight.toInt().toString(),
                colorDrawablePath, canvas)
            currentNode = visitedNodesOrder.get(index = i) as DrawableNode
        }
        paint.textSize *= 1.5f
        invalidate()
    }

    private fun drawWeights(canvas: Canvas) {
        paint.style = Paint.Style.FILL

        paint.textSize /= 1.5f
        for (drawableEdge in drawableEdges) {
            val edge = drawableEdge.edge ?: continue
            val nodeA = drawableEdge.nodeA
            val nodeB = drawableEdge.nodeB
            drawWeight(nodeA, nodeB, edge.weight.toInt().toString(),
                colorBoxWeight, canvas)
        }
        paint.textSize *= 1.5f
    }

    private fun drawWeight(nodeA: DrawableNode, nodeB: DrawableNode, weight: String,
                           boxColor: Int, canvas: Canvas) {
        val edge = nodeA.connectedByEdge[nodeB.id] ?: return
        val textCenterX = (nodeA.centerX + nodeB.centerX) / 2
        val textCenterY = (nodeA.centerY + nodeB.centerY) / 2

        paint.color = boxColor
        canvas.drawRoundRect(edge.weightBox, 15f, 15f, paint)

        paint.color = colorTextWeight
        canvas.drawText(weight, textCenterX - (paint.measureText(weight) / 2),
            textCenterY - ((paint.descent() + paint.ascent()) / 2), paint)
    }

    private fun configurePaint() {
        paint.isAntiAlias = true
        paint.strokeWidth = resources.displayMetrics.density
        paint.textSize = 48f
    }

    fun registerListener(listener: GraphListener) {
        listeners.add(listener)
    }

    fun reset() {
        graph = DrawableGraph()
        startPoint = null
        endPoint = null
        pathPositions.clear()
        drawableEdges.clear()
        visitedNodesOrder.clear()
        selectedOption = DJIKSTRA
        invalidate()

        listeners.forEach { it.onGraphNotReady() }
        listeners.forEach { it.onGraphNotCleanable() }
    }


}