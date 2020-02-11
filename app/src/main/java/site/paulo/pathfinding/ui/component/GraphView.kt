package site.paulo.pathfinding.ui.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import site.paulo.pathfinding.algorithm.Algorithm
import site.paulo.pathfinding.algorithm.Djikstra
import site.paulo.pathfinding.data.model.MatrixGraph
import site.paulo.pathfinding.data.model.Node
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import site.paulo.pathfinding.R
import java.util.concurrent.atomic.AtomicBoolean

class GraphView : View {

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    private var rows: Int = 10
    private var cols: Int = 10
    private var squareSide: Float = 0f
    private val uninitialized: Pair<Int, Int> = Pair(-1, -1)
    private var lastVisitedNode: Pair<Int, Int> = uninitialized
    var startPoint: Pair<Int, Int> = uninitialized
    var endPoint: Pair<Int, Int> = uninitialized

    private var readyToRemoveNodes: Boolean = false
    private var readyToReaddNodes: Boolean = false
    private var animating: AtomicBoolean = AtomicBoolean(false)

    private val pathPositions: HashMap<Pair<Int, Int>, RectF> = HashMap()
    private val visitedNodesPositions: HashMap<Pair<Int, Int>, RectF> = HashMap()
    private val removedNodes: HashMap<Pair<Int, Int>, RectF> = HashMap()
    private var graph: MatrixGraph = MatrixGraph(rows, cols)
    private lateinit var algorithm: Algorithm

    private val paint = Paint()
    private val colorHorizontalLine: Int = ContextCompat.getColor(context, R.color.colorTableHorizontalLines)
    private val colorVerticalLine: Int = ContextCompat.getColor(context, R.color.colorTableVerticalLines)
    private val colorPath: Int = ContextCompat.getColor(context, R.color.colorPath)
    private val colorVisited: Int = ContextCompat.getColor(context, R.color.colorVisited)
    private val colorStartPoint: Int = ContextCompat.getColor(context, R.color.colorStartPoint)
    private val colorEndPoint: Int = ContextCompat.getColor(context, R.color.colorEndPoint)
    private val colorRemovedNode: Int = ContextCompat.getColor(context, R.color.colorRemovedCell)
    private val colorRemovedNodeX: Int = ContextCompat.getColor(context, R.color.colorRemovedCellX)

    private var listeners: ArrayList<GraphListener> = ArrayList()

    enum class SupportedAlgorithms {
        DJIKSTRA
    }

    init {
        configurePaint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawHorizontalLines(canvas, rows)
        drawVerticalLines(canvas, cols)
        drawVisitedNodes(canvas)
        drawPathNodes(canvas)
        drawRemovedCellsNodes(canvas)
        drawPoints(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> markPoint(getRectOnPosition(x, y))
            MotionEvent.ACTION_MOVE -> {
                if ((readyToRemoveNodes) && lastVisitedNode != getRectOnPosition(x, y)) {
                    lastVisitedNode = getRectOnPosition(x, y)
                    removeNode(lastVisitedNode)
                } else if (readyToReaddNodes)
                    readdNode(getRowAndColAtPosition(x, y))
            }
            MotionEvent.ACTION_UP -> {
                readyToRemoveNodes = (startPoint != uninitialized && endPoint != uninitialized)
            }
            MotionEvent.ACTION_CANCEL -> {
            }
        }
        return true
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        squareSide = (width / cols).toFloat()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = if (measuredWidth > measuredHeight) measuredHeight else measuredWidth
        setMeasuredDimension(size, size)
    }

    fun runAlgorithm(alg: SupportedAlgorithms) {
        if (startPoint == uninitialized || endPoint == uninitialized) return
        if (animating.get()) return
        pathPositions.clear()
        visitedNodesPositions.clear()

        when (alg) {
            SupportedAlgorithms.DJIKSTRA -> algorithm = Djikstra(graph, startPoint, endPoint)
        }

        algorithm.run()
        scheduleDraw(algorithm.getVisitedOrder(), algorithm.getShortestPath(), 30)
    }

    fun reset() {
        graph = MatrixGraph(rows, cols)
        startPoint = uninitialized
        endPoint = uninitialized
        pathPositions.clear()
        visitedNodesPositions.clear()
        removedNodes.clear()
        readyToRemoveNodes = false
        invalidate()

        listeners.forEach { it.onGraphNotReady() }
        listeners.forEach { it.onGraphNotCleanable() }
    }

    private fun markPoint(position: Pair<Int, Int>) {
        if ((position.first >= cols) || (position.second >= rows)) return
        when {
            this.startPoint == uninitialized -> {
                this.startPoint = position
                if (endPoint != uninitialized) listeners.forEach { it.onGraphReady() }
            }
            this.startPoint == position -> {
                startPoint = uninitialized
                listeners.forEach { it.onGraphNotReady() }
            }
            this.endPoint == uninitialized -> {
                this.endPoint = position
                if (startPoint != uninitialized) listeners.forEach { it.onGraphReady() }
            }
            this.endPoint == position -> {
                endPoint = uninitialized
                listeners.forEach { it.onGraphNotReady() }
            }
            else -> {
                if (removedNodes[position] != null) {
                    readyToReaddNodes = true
                    readyToRemoveNodes = false
                    this.readdNode(position)
                } else {
                    readyToReaddNodes = false
                    readyToRemoveNodes = true
                    this.removeNode(position)
                }
            }
        }

        if (startPoint != uninitialized || endPoint != uninitialized || pathPositions.isNotEmpty()) {
            listeners.forEach { it.onGraphCleanable() }
        }

        invalidate()
    }

    private fun addPositionToPath(position: Pair<Int, Int>) {
        pathPositions[position] = getRectInsideTablePositionCell(position)
        invalidate()
    }

    private fun addPositionToVisitedNodes(position: Pair<Int, Int>) {
        visitedNodesPositions[position] = getRectInsideTablePositionCell(position)
        invalidate()
    }

    private fun removeNode(position: Pair<Int, Int>) {
        if ((position.first >= cols) || (position.second >= rows)) return
        removedNodes[position] = getRectOnTableCell(position)
        graph.removeNode(position)
        invalidate()
    }

    private fun readdNode(position: Pair<Int, Int>) {
        if ((position.first >= cols) || (position.second >= rows)) return
        removedNodes.remove(position)
        graph.readdNode(position)
        invalidate()
    }

    private fun drawHorizontalLines(canvas: Canvas, rows: Int) {
        paint.color = colorHorizontalLine
        paint.style = Paint.Style.STROKE
        for (i in 0..rows) {
            canvas.drawLine(0f, squareSide * i, squareSide * cols, squareSide * i, paint)
        }
    }

    private fun drawVerticalLines(canvas: Canvas, cols: Int) {
        paint.color = colorVerticalLine
        paint.style = Paint.Style.STROKE
        for (i in 0..cols) {
            canvas.drawLine(squareSide * i, 0f, squareSide * i, squareSide * rows, paint)
        }
    }

    private fun drawPathNodes(canvas: Canvas) {
        paint.color = colorPath
        paint.style = Paint.Style.FILL
        for (node in pathPositions.values) {
            canvas.drawRect(node, paint)
        }
    }

    private fun drawVisitedNodes(canvas: Canvas) {
        paint.color = colorVisited
        paint.style = Paint.Style.FILL
        for (node in visitedNodesPositions.values) {
            canvas.drawRect(node, paint)
        }
    }

    /**
     * Draw start and end points
     */
    private fun drawPoints(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        if (startPoint != uninitialized) {
            paint.color = colorStartPoint
            canvas.drawRect(getRectOnTableCell(startPoint), paint)
        }
        if (endPoint != uninitialized) {
            paint.color = colorEndPoint
            canvas.drawRect(getRectOnTableCell(endPoint), paint)
        }
    }

    private fun drawRemovedCellsNodes(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = colorRemovedNode
        removedNodes.values.forEach {
            canvas.drawRect(it, paint)
        }

        paint.style = Paint.Style.STROKE
        paint.color = colorRemovedNodeX
        removedNodes.values.forEach {
            canvas.drawLine(it.left, it.top, it.right, it.bottom, paint)
            canvas.drawLine(it.left, it.bottom, it.right, it.top, paint)
        }
    }

    private fun scheduleDraw(visitedNodes: LinkedList<Node>, path: Stack<Node>, nodesPerSec: Int) {
        animating.set(true)
        listeners.forEach { it.onGraphNotReady() }
        listeners.forEach { it.onGraphNotCleanable() }

        Handler().post(object : Runnable {
            override fun run() {
                if (visitedNodes.isNotEmpty()) {
                    addPositionToVisitedNodes(visitedNodes.removeFirst().position)
                    handler.postDelayed(this, (1000 / (nodesPerSec * 5)).toLong())
                } else if (path.isNotEmpty()) {
                    addPositionToPath(path.pop().position)
                    handler.postDelayed(this, (1000 / nodesPerSec).toLong())
                } else {
                    handler.removeCallbacks(this)
                    animating.set(false)
                    listeners.forEach { it.onGraphReady() }
                    listeners.forEach { it.onGraphCleanable() }
                }
            }
        })
    }

    private fun getRectInsideTablePositionCell(position: Pair<Int, Int>): RectF {
        val topX = squareSide * position.first
        val topY = squareSide * position.second
        val offset = paint.strokeWidth / 2
        return RectF(
            topX + offset, topY + offset,
            topX + squareSide - offset, topY + squareSide - offset
        )
    }

    private fun getRectOnTableCell(position: Pair<Int, Int>): RectF {
        val topX = squareSide * position.first
        val topY = squareSide * position.second
        return RectF(topX, topY, topX + squareSide, topY + squareSide)
    }

    private var cachedPosition: Pair<Int, Int> = uninitialized
    private fun getRectOnPosition(x: Float, y: Float): Pair<Int, Int> {
        return if ((cachedPosition.first == (x / squareSide).toInt()) &&
            (cachedPosition.second == (y / squareSide).toInt())
        ) cachedPosition
        else getRowAndColAtPosition(x, y)
    }

    private fun getRowAndColAtPosition(x: Float, y: Float): Pair<Int, Int> {
        return Pair((x / squareSide).toInt(), (y / squareSide).toInt())
    }

    private fun configurePaint() {
        paint.isAntiAlias = true
        paint.strokeWidth = resources.displayMetrics.density
    }

    fun registerListener(listener: GraphListener) {
        listeners.add(listener)
    }

    fun configureSides(amount: Int) {
        rows = amount
        cols = amount
        squareSide = (width / cols).toFloat()
        reset()
    }

}