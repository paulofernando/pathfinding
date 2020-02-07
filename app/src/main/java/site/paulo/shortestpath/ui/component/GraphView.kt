package site.paulo.shortestpath.ui.component

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class GraphView : View {

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    private val rows: Int = 5
    private val cols: Int = 5
    private var squareSide: Float = 100f

    private val squares: ArrayList<RectF> = ArrayList()

    private val paint = Paint()

    init {
        configurePaint()
        visitPosition(0,0)
        visitPosition(0,1)
    }

    fun visitPosition(row: Int, col: Int) {
        if ((row > rows) || (col > cols)) return
        val topX = squareSide * row
        val topY = squareSide * col
        squares.add(RectF(topX, topY, topX + squareSide, topY + squareSide))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawHorizontalLines(canvas, rows)
        drawVerticalLines(canvas, cols)
        drawNodes(canvas)
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
        for (node in squares) {
            canvas.drawRect(node, paint)
        }
        paint.style = Paint.Style.STROKE
    }

    private fun configurePaint() {
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = resources.displayMetrics.density * 2
    }


}