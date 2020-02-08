package site.paulo.shortestpath.ui.component

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class GraphView : View {

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    private val rows: Int = 5
    private val cols: Int = 5
    private var squareSide: Float = 100f

    private var startPoint: Pair<Int,Int>? = null
    private var endPoint: Pair<Int,Int>? = null
    private val squares: HashMap<Pair<Int,Int>, RectF> = HashMap()
    private val paint = Paint()

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

    private fun getRectInPosition(position: Pair<Int, Int>): RectF {
        val topX = squareSide * position.first
        val topY = squareSide * position.second
        return RectF(topX, topY, topX + squareSide, topY + squareSide)
    }

    private fun markPoint(position: Pair<Int, Int>) {
        if (startPoint == null) {
            startPoint = position
        } else {
            if (startPoint == position) {
                startPoint = null
            } else {
                endPoint = position
            }
        }
        invalidate()
    }

    private fun drawHorizontalLines(canvas: Canvas, rows: Int) {
        for (i in 0..rows) {
            canvas.drawLine(0f, squareSide * i, squareSide * cols, squareSide * i, paint)
        }
    }

    private fun drawVerticalLines(canvas: Canvas, cols: Int) {
        for (i in 0..cols) {
            canvas.drawLine(squareSide * i, 0f, squareSide * i, squareSide * rows, paint)
        }
    }

    private fun drawNodes(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        for (node in squares.values) {
            canvas.drawRect(node, paint)
        }
        paint.style = Paint.Style.STROKE
    }

    private fun drawPoints(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        if (startPoint != null) {
            paint.color = Color.BLUE
            canvas.drawRect(getRectInPosition(startPoint!!), paint)
        }
        if (endPoint != null) {
            paint.color = Color.RED
            canvas.drawRect(getRectInPosition(endPoint!!), paint)
        }
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
    }

    private fun getSquareOnPosition(x: Float, y: Float): Pair<Int, Int> {
        return Pair((x/squareSide).toInt(), (y/squareSide).toInt())
    }

    private fun configurePaint() {
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = resources.displayMetrics.density * 2
    }


}