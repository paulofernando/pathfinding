package site.paulo.pathfinding.ui.component.graphview.grid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.content.ContextCompat
import kotlin.collections.HashMap
import site.paulo.pathfinding.R


class GridGraphViewPaint(val context: Context, private val paint: Paint,
                         private var rows: Int = 10, private var cols: Int = 10) {

    init {
        configurePaint()
    }

    private val uninitialized = Pair(-1, -1)
    private var squareSide: Float = 0f

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

    fun drawHorizontalLines(rows: Int, canvas: Canvas) {
        paint.color = colorHorizontalLine
        paint.style = Paint.Style.STROKE
        for (i in 0..rows) {
            canvas.drawLine(0f, squareSide * i, squareSide * cols, squareSide * i, paint)
        }
    }

    fun drawVerticalLines(cols: Int, canvas: Canvas) {
        paint.color = colorVerticalLine
        paint.style = Paint.Style.STROKE
        for (i in 0..cols) {
            canvas.drawLine(squareSide * i, 0f, squareSide * i, squareSide * rows, paint)
        }
    }

    fun drawPathNodes(pathPositions: HashMap<Pair<Int, Int>, RectF>, canvas: Canvas) {
        paint.color = colorPath
        paint.style = Paint.Style.FILL
        for (node in pathPositions.values) {
            canvas.drawRect(node, paint)
        }
    }

    fun drawVisitedNodes(visitedNodesPositions: HashMap<Pair<Int, Int>, RectF>, canvas: Canvas) {
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
    fun drawPoints(startPoint: Pair<Int, Int>, endPoint: Pair<Int, Int>,
                           canvas: Canvas) {
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

    fun drawWeightIncreasedPoints(increasedWeightNodes: HashMap<Pair<Int, Int>, Int>,
                                          canvas: Canvas) {
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

    fun drawRemovedCellsNodes(removedNodes: HashMap<Pair<Int, Int>, RectF>, canvas: Canvas) {
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

    private fun configurePaint() {
        paint.isAntiAlias = true
        paint.strokeWidth = context.resources.displayMetrics.density
        paint.textSize = 48f
    }

    private fun getCenterOfCell(position: Pair<Int, Int>): Pair<Float, Float> {
        val topX = squareSide * position.first
        val topY = squareSide * position.second
        return Pair(topX + (squareSide / 2), topY + (squareSide / 2))
    }

    private fun getRectOnTableCell(position: Pair<Int, Int>): RectF {
        val topX = squareSide * position.first
        val topY = squareSide * position.second
        return RectF(topX, topY, topX + squareSide, topY + squareSide)
    }

}