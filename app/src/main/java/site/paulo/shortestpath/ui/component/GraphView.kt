package site.paulo.shortestpath.ui.component

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import site.paulo.shortestpath.R
import site.paulo.shortestpath.algorithm.Algorithm
import site.paulo.shortestpath.algorithm.Djikstra
import site.paulo.shortestpath.data.model.MatrixGraph
import site.paulo.shortestpath.data.model.Node
import java.util.*
import kotlin.collections.HashMap

class GraphView : View {

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    private val rows: Int = 10
    private val cols: Int = 10
    private var squareSide: Float = 100f
    private val uninitialized: Pair<Int,Int> = Pair(-1,-1)
    private var lastVisitedNode: Pair<Int,Int> = uninitialized
    var startPoint: Pair<Int,Int> = uninitialized
    var endPoint: Pair<Int,Int> = uninitialized
    var readyToRemoveNodes: Boolean = false

    private val visitedPosition: HashMap<Pair<Int,Int>, RectF> = HashMap()
    private val removedNodes: HashMap<Pair<Int,Int>, RectF> = HashMap()
    private var graph: MatrixGraph = MatrixGraph(rows,cols)
    private lateinit var algorithm: Algorithm

    private val paint = Paint()
    private val colorHorizontalLine: Int = ContextCompat.getColor(context, R.color.colorTableHorizontalLines)
    private val colorVerticalLine: Int = ContextCompat.getColor(context, R.color.colorTableVerticalLines)
    private val colorPath: Int = ContextCompat.getColor(context, R.color.colorPath)
    private val colorStartPoint: Int = ContextCompat.getColor(context, R.color.colorStartPoint)
    private val colorEndPoint: Int = ContextCompat.getColor(context, R.color.colorEndPoint)
    private val colorRemovedNode: Int = ContextCompat.getColor(context, R.color.colorRemovedCell)

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
        drawNodes(canvas)
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
            MotionEvent.ACTION_DOWN -> {
                markPoint(getSquareOnPosition(x,y))
            }
            MotionEvent.ACTION_MOVE -> {
                if (readyToRemoveNodes) {
                    if (lastVisitedNode != getSquareOnPosition(x, y)) {
                        lastVisitedNode = getSquareOnPosition(x, y)
                        removeNode(lastVisitedNode)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                readyToRemoveNodes = (startPoint != uninitialized && endPoint != uninitialized)
            }
            MotionEvent.ACTION_CANCEL -> {

            }
        }
        return true
    }

    fun runAlgorithm(alg: SupportedAlgorithms) {
        if (startPoint == uninitialized || endPoint == uninitialized) return

        when (alg) {
            SupportedAlgorithms.DJIKSTRA -> algorithm = Djikstra(graph, startPoint, endPoint)
        }

        algorithm.run()
        val path: Stack<Node> = algorithm.getShortestPath()
        while (path.isNotEmpty()) {
            visitPosition(path.pop().position)
        }
    }

    fun reset() {
        graph = MatrixGraph(rows,cols)
        startPoint = uninitialized
        endPoint = uninitialized
        visitedPosition.clear()
        removedNodes.clear()
        readyToRemoveNodes = false
        invalidate()
    }

    private fun visitPosition(position: Pair<Int, Int>) {
        visitedPosition[position] = getRectInPosition(position)
        invalidate()
    }

    private fun getRectInPosition(position: Pair<Int, Int>): RectF {
        val topX = squareSide * position.first
        val topY = squareSide * position.second
        return RectF(topX, topY, topX + squareSide, topY + squareSide)
    }

    private fun markPoint(position: Pair<Int, Int>) {
        if ((position.first > cols) || (position.second > rows)) return
        when {
            this.startPoint.first == -1 -> this.startPoint = position
            this.startPoint == position -> startPoint = Pair(-1,-1)
            this.endPoint.first == -1 -> this.endPoint = position
            this.endPoint == position -> endPoint = Pair(-1,-1)
            else -> this.removeNode(position)
        }
        invalidate()
    }

    private fun removeNode(position: Pair<Int, Int>) {
        removedNodes[position] = getRectInPosition(position)
        graph.removeNode(position)
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

    private fun drawNodes(canvas: Canvas) {
        paint.color = colorPath
        paint.style = Paint.Style.FILL
        for (node in visitedPosition.values) {
            canvas.drawRect(node, paint)
        }
        paint.style = Paint.Style.STROKE
    }

    /**
     * Draw start and end points
     */
    private fun drawPoints(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        if (startPoint != uninitialized) {
            paint.color = colorStartPoint
            canvas.drawRect(getRectInPosition(startPoint), paint)
        }
        if (endPoint != uninitialized) {
            paint.color = colorEndPoint
            canvas.drawRect(getRectInPosition(endPoint), paint)
        }
    }

    private fun drawRemovedCellsNodes(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = colorRemovedNode
        removedNodes.values.forEach {
            canvas.drawRect(it, paint)
        }
    }

    var cachedPosition: Pair<Int,Int> = uninitialized
    private fun getSquareOnPosition(x: Float, y: Float): Pair<Int, Int> {
        return if ((cachedPosition.first == (x / squareSide).toInt()) &&
            (cachedPosition.second == (y / squareSide).toInt())) cachedPosition
        else Pair((x / squareSide).toInt(), (y / squareSide).toInt())
    }

    private fun configurePaint() {
        paint.isAntiAlias = true
        paint.strokeWidth = resources.displayMetrics.density * 2
    }


}