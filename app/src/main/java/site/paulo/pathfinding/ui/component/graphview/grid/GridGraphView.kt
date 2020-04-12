package site.paulo.pathfinding.ui.component.graphview.grid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import site.paulo.pathfinding.data.model.GridGraph
import site.paulo.pathfinding.data.model.Node
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import site.paulo.pathfinding.R
import site.paulo.pathfinding.algorithm.*
import site.paulo.pathfinding.data.model.Edge
import site.paulo.pathfinding.data.model.PathFindingAlgorithms
import site.paulo.pathfinding.ui.component.graphview.GraphListener
import java.util.concurrent.atomic.AtomicBoolean
import site.paulo.pathfinding.data.model.PathFindingAlgorithms.*

class GridGraphView : View {

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    private var rows: Int = 10
    private var cols: Int = 10
    private var squareSide: Float = 0f
    private val uninitialized = Pair(-1, -1)
    private val maxWeight: Double = 3.0

    private var readyToIncreaseWeightNodes: Boolean = false
    private var readyToReaddNodes: Boolean = false
    private var hasWeight: Boolean = true
    private var animating: AtomicBoolean = AtomicBoolean(false)
    private val defaultPathNodePerSec = 50
    private val defaultVisitedNodePerSec = defaultPathNodePerSec * 5
    private var selectedAlgorithm: PathFindingAlgorithms = DJIKSTRA

    private var startPoint = uninitialized
    private var endPoint = uninitialized
    private var lastVisitedNode = uninitialized
    private var graph: GridGraph = GridGraph(rows, cols)
    private lateinit var algorithm: PathFindingAlgorithm
    private val pathPositions: HashMap<Pair<Int, Int>, RectF> = HashMap()
    private val visitedNodesPositions: HashMap<Pair<Int, Int>, RectF> = HashMap()
    private val removedNodes: HashMap<Pair<Int, Int>, RectF> = HashMap()
    private val increasedWeightNodes: HashMap<Pair<Int, Int>, Int> = HashMap()
    private var listeners: ArrayList<GraphListener> = ArrayList()

    private val paint = Paint()
    // --------- colors ---------
    private val colorHorizontalLine: Int = ContextCompat.getColor(context, R.color.colorTableHorizontalLines)
    private val colorVerticalLine: Int = ContextCompat.getColor(context, R.color.colorTableVerticalLines)
    private val colorPath: Int = ContextCompat.getColor(context, R.color.colorPath)
    private val colorVisited: Int = ContextCompat.getColor(context, R.color.colorVisited)
    private val colorStartPoint: Int = ContextCompat.getColor(context, R.color.colorStartPoint)
    private val colorEndPoint: Int = ContextCompat.getColor(context, R.color.colorEndPoint)
    private val colorRemovedNode: Int = ContextCompat.getColor(context, R.color.colorRemovedCell)
    private val colorRemovedNodeX: Int = ContextCompat.getColor(context, R.color.colorRemovedCellX)
    private val colorIncreasedWeightNode: Int = ContextCompat.getColor(context, R.color.colorIncreasedWeightText)
    // --------------------------

    init {
        configurePaint()
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawHorizontalLines(canvas, rows)
        drawVerticalLines(canvas, cols)
        drawVisitedNodes(canvas)
        drawPathNodes(canvas)
        drawRemovedCellsNodes(canvas)
        if (hasWeight) {
            drawWeightIncreasedPoints(canvas)
        }
        drawPoints(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return false

        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastVisitedNode = getRectOnPosition(x, y)
                markPoint(lastVisitedNode)
            }
            MotionEvent.ACTION_MOVE -> {
                if (lastVisitedNode != getRectOnPosition(x, y)) {
                    lastVisitedNode = getRectOnPosition(x, y)
                    if (readyToIncreaseWeightNodes) {
                        increaseWeight(lastVisitedNode)
                    } else if (readyToReaddNodes) {
                        readdNode(getRowAndColAtPosition(x, y))
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                readyToIncreaseWeightNodes =
                    (startPoint != uninitialized && endPoint != uninitialized)
            }
            MotionEvent.ACTION_CANCEL -> { }
        }
        return true
    }

    fun setAlgorithm(alg: PathFindingAlgorithms) {
        selectedAlgorithm = alg
        when(selectedAlgorithm) {
            DJIKSTRA -> enableWeightIncrease(true)
            ASTAR -> enableWeightIncrease(true)
            BREADTH_FIRST -> enableWeightIncrease(false)
            DEPTH_FIRST -> enableWeightIncrease(false)
        }
    }

    fun runAlgorithm() {
        if (startPoint == uninitialized || endPoint == uninitialized) return
        if (animating.get()) return
        pathPositions.clear()
        visitedNodesPositions.clear()

        val nodeA = graph.getNode(startPoint) ?: return
        val nodeB = graph.getNode(endPoint) ?: return
        algorithm = when (selectedAlgorithm) {
            DJIKSTRA -> Djikstra(graph.getNodes(), nodeA, nodeB)
            ASTAR -> AStar(graph, nodeA, nodeB)
            BREADTH_FIRST -> BreadthFirst(nodeA, nodeB)
            DEPTH_FIRST -> DepthFirst(nodeA, nodeB)
        }

        algorithm.run()
        scheduleDraw(algorithm.getVisitedOrder(), algorithm.getPath(),
            defaultPathNodePerSec, defaultVisitedNodePerSec)
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
                    readyToIncreaseWeightNodes = false
                    this.readdNode(position)
                } else {
                    readyToReaddNodes = false
                    readyToIncreaseWeightNodes = true
                    if (hasWeight) {
                        this.increaseWeight(position)
                    } else {
                        this.removeNode(position)
                    }
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

    private fun increaseWeight(position: Pair<Int, Int>, amountToIncrease: Double = 2.0) {
        if ((position.first >= cols) || (position.second >= rows)) return

        if (removedNodes.containsKey(position)) {
            readdNode(position)
            return
        }

        var newWeight = amountToIncrease
        if (increasedWeightNodes.containsKey(position)) {
            newWeight += increasedWeightNodes[position]!!
        } else {
            newWeight += Edge.DEFAULT_WEIGHT
        }

        if (newWeight > maxWeight) {
            removeNode(position)
            increasedWeightNodes.remove(position)
            graph.getNode(position)?.setAllWeights(Edge.DEFAULT_WEIGHT)
        } else {
            increasedWeightNodes[position] = newWeight.toInt()
            graph.getNode(position)?.setAllWeights(newWeight)
        }

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
        paint.alpha = 200
        for (node in visitedNodesPositions.values) {
            canvas.drawRect(node, paint)
        }
        paint.alpha = 255
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

    private fun drawWeightIncreasedPoints(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = colorIncreasedWeightNode
        increasedWeightNodes.entries.forEach {
            val center = getCenterOfCell(it.key)
            canvas.drawText(
                it.value.toString(), center.first - paint.measureText(it.value.toString()) / 2,
                center.second - ((paint.descent() + paint.ascent()) / 2), paint
            )
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

    private fun scheduleDraw(visitedNodes: LinkedList<Node>, path: Stack<Node>,
                             pathNodesPerSec: Int, visitedNodesPerSec: Int) {
        animating.set(true)
        listeners.forEach { it.onGraphNotReady() }
        listeners.forEach { it.onGraphNotCleanable() }

        Handler().post(object : Runnable {
            override fun run() {
                when {
                    visitedNodes.isNotEmpty() -> {
                        addPositionToVisitedNodes(visitedNodes.removeFirst().position)
                        handler.postDelayed(this, (1000 / visitedNodesPerSec).toLong())
                    }
                    path.isNotEmpty() -> {
                        addPositionToPath(path.pop().position)
                        handler.postDelayed(this, (1000 / pathNodesPerSec).toLong())
                    }
                    else -> {
                        handler.removeCallbacks(this)
                        animating.set(false)
                        listeners.forEach { it.onGraphReady() }
                        listeners.forEach { it.onGraphCleanable() }
                    }
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

    private fun getCenterOfCell(position: Pair<Int, Int>): Pair<Float, Float> {
        val topX = squareSide * position.first
        val topY = squareSide * position.second
        return Pair(topX + (squareSide / 2), topY + (squareSide / 2))
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
        paint.textSize = 48f
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

    fun enableWeightIncrease(enable: Boolean) {
        hasWeight = enable
        invalidate()
    }

    fun reset() {
        graph = GridGraph(rows, cols)
        startPoint = uninitialized
        endPoint = uninitialized
        pathPositions.clear()
        visitedNodesPositions.clear()
        increasedWeightNodes.clear()
        removedNodes.clear()
        readyToIncreaseWeightNodes = false
        invalidate()

        listeners.forEach { it.onGraphNotReady() }
        listeners.forEach { it.onGraphNotCleanable() }
    }

}