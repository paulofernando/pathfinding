package site.paulo.pathfinding.ui.component.graphview.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import site.paulo.pathfinding.data.model.DrawableGraph
import site.paulo.pathfinding.data.model.Graph
import site.paulo.pathfinding.data.model.Node
import java.util.*


class DrawableGraphViewPaint(val context: Context, val paint: Paint) {

    init {
        configurePaint()
    }

    // --------- colors ---------
    private val colorStartNode: Int = androidx.core.content.ContextCompat.getColor(context, site.paulo.pathfinding.R.color.colorStartPoint)
    private val colorEndNode: Int = androidx.core.content.ContextCompat.getColor(context, site.paulo.pathfinding.R.color.colorEndPoint)
    private val colorNode: Int = androidx.core.content.ContextCompat.getColor(context, site.paulo.pathfinding.R.color.colorNode)
    private val colorDrawablePath: Int = androidx.core.content.ContextCompat.getColor(context, site.paulo.pathfinding.R.color.colorDrawablePath)
    private val colorNodeText: Int = androidx.core.content.ContextCompat.getColor(context, site.paulo.pathfinding.R.color.colorNodeText)
    private val colorEdge: Int = androidx.core.content.ContextCompat.getColor(context, site.paulo.pathfinding.R.color.colorEdge)
    private val colorTextWeight: Int = androidx.core.content.ContextCompat.getColor(context, site.paulo.pathfinding.R.color.colorTextWeight)
    private val colorBoxWeight: Int = androidx.core.content.ContextCompat.getColor(context, site.paulo.pathfinding.R.color.colorBoxWeight)
    private val colorSelectedNode: Int = androidx.core.content.ContextCompat.getColor(context, site.paulo.pathfinding.R.color.colorSelectedNode)
    private val colorBoundaries: Int = androidx.core.content.ContextCompat.getColor(context, site.paulo.pathfinding.R.color.colorBoundaries)
    // --------------------------

    fun drawBoundaries(width: Int, height: Int, canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.color = colorBoundaries

        canvas.drawRect(1f, 1f, width - 1f, height - 1f, paint)
    }

    fun drawNodes(nodes: List<DrawableNode>, canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = colorNode

        for (node in nodes)
            drawNode(node, canvas)
    }

    fun drawNode(node: DrawableNode, canvas: Canvas) {
        canvas.drawCircle(node.centerX, node.centerY, DrawableNode.RADIUS, paint)
    }

    fun drawSelectedNode(selectedNode: DrawableNode?, canvas: Canvas) {
        if (selectedNode == null) return
        paint.style = Paint.Style.STROKE
        paint.color = colorSelectedNode
        drawNode(selectedNode, canvas)
    }

    fun drawPathNodes(graph: DrawableGraph, pathNodesOrder: List<Node>, canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = colorDrawablePath

        for (node in pathNodesOrder) {
            val drawableNode = graph.getNode(node.name)
            if (drawableNode != null)
                drawNode(drawableNode, canvas)
        }
    }

    fun drawStartAndEndPoints(startNode: DrawableNode?, endNode: DrawableNode?, canvas: Canvas) {
        if (startNode != null) {
            paint.style = Paint.Style.FILL
            paint.color = colorStartNode
            canvas.drawCircle(startNode.centerX, startNode.centerY, DrawableNode.RADIUS, paint)
        }

        if (endNode == null) return
        paint.color = colorEndNode
        canvas.drawCircle(endNode.centerX, endNode.centerY, DrawableNode.RADIUS, paint)
    }

    fun drawTextNodes(nodes: List<DrawableNode>, canvas: Canvas) {
        paint.color = colorNodeText
        for (node in nodes) {
            drawTextNode(node, canvas)
        }
    }

    fun drawTextNode(node: DrawableNode, canvas: Canvas) {
        canvas.drawText(
            node.id,
            node.centerX - paint.measureText(node.id) / 2,
            node.centerY - ((paint.descent() + paint.ascent()) / 2), paint
        )
    }

    fun drawEdges(drawableEdges: List<DrawableEdge>, canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.color = colorEdge
        paint.strokeWidth = context.resources.displayMetrics.density * 2

        for (edge in drawableEdges) {
            drawEdge(edge.nodeA, edge.nodeB, canvas)
        }
        paint.strokeWidth = context.resources.displayMetrics.density
    }

    fun drawEdge(nodeA: DrawableNode, nodeB: DrawableNode, canvas: Canvas) {
        canvas.drawLine(nodeA.centerX, nodeA.centerY, nodeB.centerX, nodeB.centerY, paint)
    }

    fun drawPathEdges(pathNodesOrder: List<Node>, canvas: Canvas) {
        var currentNode = pathNodesOrder.get(index = 0) as DrawableNode
        paint.color = colorDrawablePath
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = context.resources.displayMetrics.density * 2
        for (i in 1 until pathNodesOrder.size) {
            val nodeB = pathNodesOrder.get(index = i) as DrawableNode
            drawEdge(currentNode, nodeB, canvas)
            currentNode = pathNodesOrder.get(index = i) as DrawableNode
        }
    }

    fun drawPathWeights(pathNodesOrder: List<Node>, canvas: Canvas) {
        var currentNode = pathNodesOrder.get(index = 0) as DrawableNode
        paint.textSize /= 1.5f
        paint.style = Paint.Style.FILL
        paint.strokeWidth = context.resources.displayMetrics.density
        for (i in 1 until pathNodesOrder.size) {
            val nodeB = pathNodesOrder.get(index = i) as DrawableNode
            drawWeight(
                currentNode, nodeB, currentNode.edges[nodeB.id]!!.weight.toInt().toString(),
                colorDrawablePath, canvas
            )
            currentNode = pathNodesOrder.get(index = i) as DrawableNode
        }
        paint.textSize *= 1.5f
    }

    fun drawWeights(drawableEdges: List<DrawableEdge>, canvas: Canvas) {
        paint.style = Paint.Style.FILL

        paint.textSize /= 1.5f
        for (drawableEdge in drawableEdges) {
            val edge = drawableEdge.edge ?: continue
            val nodeA = drawableEdge.nodeA
            val nodeB = drawableEdge.nodeB
            drawWeight(
                nodeA, nodeB, edge.weight.toInt().toString(),
                colorBoxWeight, canvas
            )
        }
        paint.textSize *= 1.5f
    }

    fun drawWeight(
        nodeA: DrawableNode, nodeB: DrawableNode, weight: String,
        boxColor: Int, canvas: Canvas
    ) {
        val edge = nodeA.connectedByEdge[nodeB.id] ?: return
        val textCenterX = (nodeA.centerX + nodeB.centerX) / 2
        val textCenterY = (nodeA.centerY + nodeB.centerY) / 2

        paint.color = boxColor
        canvas.drawRoundRect(edge.weightBox, 15f, 15f, paint)

        paint.color = colorTextWeight
        canvas.drawText(
            weight, textCenterX - (paint.measureText(weight) / 2),
            textCenterY - ((paint.descent() + paint.ascent()) / 2), paint
        )
    }

    private fun configurePaint() {
        paint.isAntiAlias = true
        paint.strokeWidth = context.resources.displayMetrics.density
        paint.textSize = 48f
    }

}