package site.paulo.shortestpath.ui.component

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import site.paulo.shortestpath.R
import site.paulo.shortestpath.algorithm.Algorithm
import site.paulo.shortestpath.data.model.Node
import java.util.*
import kotlin.collections.HashMap

class GraphView : View {

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    private val rows: Int = 10
    private val cols: Int = 10
    private var squareSide: Float = 100f

    private var startPoint: Pair<Int,Int> = Pair(-1,-1)
    private var endPoint: Pair<Int,Int> = Pair(-1,-1)
    private val squares: HashMap<Pair<Int,Int>, RectF> = HashMap()
    private val paint = Paint()

    private val colorHorizontalLine: Int = ContextCompat.getColor(context, R.color.colorTableHorizontalLines)
    private val colorVerticalLine: Int = ContextCompat.getColor(context, R.color.colorTableVerticalLines)
    private val colorPath: Int = ContextCompat.getColor(context, R.color.colorPath)
    private val colorStartPoint: Int = ContextCompat.getColor(context, R.color.colorStartPoint)
    private val colorEndPoint: Int = ContextCompat.getColor(context, R.color.colorEndPoint)

    init {
        configurePaint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawHorizontalLines(canvas, rows)
        drawVerticalLines(canvas, cols)
        drawNodes(canvas)
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

            }
            MotionEvent.ACTION_UP -> {

            }
            MotionEvent.ACTION_CANCEL -> {

            }
        }
        return true
    }

    fun visitPosition(position: Pair<Int, Int>) {
        squares[position] = getRectInPosition(position)
        invalidate()
    }

    fun getStartPoint(): Pair<Int,Int> {
        return this.startPoint
    }

    fun getEndPoint(): Pair<Int,Int> {
        return this.endPoint
    }

    fun runAlgorithm(alg: Algorithm) {
        alg.run()
        val path: Stack<Node> = alg.getShortestPath()
        while (path.isNotEmpty()) {
            visitPosition(path.pop().position)
        }
    }

    private fun getRectInPosition(position: Pair<Int, Int>): RectF {
        val topX = squareSide * position.first
        val topY = squareSide * position.second
        return RectF(topX, topY, topX + squareSide, topY + squareSide)
    }

    private fun markPoint(position: Pair<Int, Int>) {
        if (this.startPoint.first == -1) {
            this.startPoint = position
        } else {
            if (this.startPoint == position) {
                startPoint = Pair(-1,-1)
            } else {
                this.endPoint = position
            }
        }
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
        for (node in squares.values) {
            canvas.drawRect(node, paint)
        }
        paint.style = Paint.Style.STROKE
    }

    private fun drawPoints(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        if (startPoint.first != -1) {
            paint.color = colorStartPoint
            canvas.drawRect(getRectInPosition(startPoint), paint)
        }
        if (endPoint.first != -1) {
            paint.color = colorEndPoint
            canvas.drawRect(getRectInPosition(endPoint), paint)
        }
    }

    private fun getSquareOnPosition(x: Float, y: Float): Pair<Int, Int> {
        return Pair((x/squareSide).toInt(), (y/squareSide).toInt())
    }

    private fun configurePaint() {
        paint.isAntiAlias = true
        paint.strokeWidth = resources.displayMetrics.density * 2
    }


}