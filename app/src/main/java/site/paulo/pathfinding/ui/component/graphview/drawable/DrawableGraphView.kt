package site.paulo.pathfinding.ui.component.graphview.drawable

import android.app.RemoteAction
import android.content.Context
import android.graphics.*
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
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
import site.paulo.pathfinding.manager.*
import site.paulo.pathfinding.ui.component.graphview.GraphListener
import java.util.*

class DrawableGraphView : View {

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    private val touchableSpace: Float = 10f
    private var selectedOption: PathFindingAlgorithms = DJIKSTRA
    private var listeners: ArrayList<GraphListener> = ArrayList()
    private lateinit var actionsManager: ActionsManager

    private val drawableEdges: ArrayList<DrawableEdge> = ArrayList()
    private var startPoint: DrawableNode? = null
    private var endPoint: DrawableNode? = null
    private var selectedNode: DrawableNode? = null
    private var readyToAddEdges: Boolean = false
    private var readyToAddStartAndEndNodes: Boolean = false
    private var readyToRunAgain: Boolean = false

    private var graph: DrawableGraph = DrawableGraph()
    private lateinit var algorithm: PathFindingAlgorithm
    private val pathNodesOrder: Stack<Node> = Stack()
    private var selectedAlgorithm: PathFindingAlgorithms = DJIKSTRA

    private val paint = Paint()
    private val paintManager = DrawableGraphViewPaint(context, paint)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paintManager.drawBoundaries(width, height, canvas)
        paintManager.drawEdges(drawableEdges, canvas)
        paintManager.drawNodes(graph.getNodes(), canvas)
        if (selectedAlgorithm == DJIKSTRA) {
            paintManager.drawWeights(drawableEdges, canvas)
        }
        if (pathNodesOrder.isNotEmpty()) {
            paintManager.drawPathNodes(graph, pathNodesOrder, canvas)
            paintManager.drawPathEdges(pathNodesOrder, this, canvas)
            if (selectedAlgorithm == DJIKSTRA) {
                paintManager.drawPathWeights(pathNodesOrder, this, canvas)
            }
        }
        paintManager.drawStartAndEndPoints(startPoint, endPoint, canvas)
        paintManager.drawTextNodes(graph.getNodes(), canvas)
        paintManager.drawSelectedNode(selectedNode, canvas)
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
                    if ((selectedNode != null) && (pathNodesOrder.isEmpty())) {
                        listeners.forEach { it.onGraphNodeRemovable() }
                    }
                } else {
                    val nodeB = getDrawableNodeAtPoint(x, y)

                    if (nodeB == null) { //user clicks on an empty area after choose first node
                        deselectNode()
                        return true
                    }

                    if (selectedNode != nodeB) { //user connect nodes
                        addDrawableEdge(selectedNode!!, nodeB)
                        deselectNode()
                        return true
                    }

                    if (selectedNode == nodeB) { //user clicks on the same selected node
                        selectInitialFinalNode(x, y)
                        deselectNode()
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
                if (y > 0 && y < this.height) {
                    val node = selectedNode ?: return false
                    moveNode(node, x, y)
                    listeners.forEach { it.onGraphNodeNotRemovable() }
                    readyToAddEdges = false
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!readyToAddEdges && !readyToAddStartAndEndNodes) {
                    deselectNode()
                }
                invalidate()
            }
            MotionEvent.ACTION_CANCEL -> {
            }
        }
        return true
    }

    private fun deselectNode() {
        selectedNode = null
        listeners.forEach { it.onGraphNodeNotRemovable() }
    }

    fun setAlgorithm(alg: PathFindingAlgorithms) {
        selectedAlgorithm = alg
        invalidate()
        if (readyToRunAgain) {
            runAlgorithm()
        }
    }

    fun runAlgorithm() {
        if (startPoint == null || endPoint == null) return

        listeners.forEach { it.onGraphNodeNotRemovable() }
        pathNodesOrder.clear()

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
            pathNodesOrder.add(path.pop())

        invalidate()
    }

    fun isReadyToRun(): Boolean {
        return (startPoint != null && endPoint != null)
    }

    private fun increaseEdgeWeight(drawableEdge: DrawableEdge) {
        if (selectedAlgorithm == DJIKSTRA) {
            drawableEdge.increaseWeight(1.0)
            invalidate()
            if (readyToRunAgain) {
                runAlgorithm()
            }
        }
    }

    private fun addDrawableNode(x: Float, y: Float) {
        val id = if (graph.getNodes().isNotEmpty())
            (graph.getNodes().last.id.toInt() + 1).toString()
        else "1"
        val node = DrawableNode(id, x, y)
        if (!hasCollision(node)) {
            graph.addNode(node)
            selectedNode = node
            invalidate()
        }
        actionsManager.addHistory(ActionAdd(node))
        listeners.forEach { it. onGraphCleanable() }
    }

    private fun addDrawableNode(drawableNode: DrawableNode, history: Boolean = true) {
        graph.addNode(drawableNode)
        invalidate()
        if (history) {
            actionsManager.addHistory(ActionAdd(drawableNode))
        }
        listeners.forEach { it. onGraphCleanable() }
    }

    fun removeSelectedNode() {
        val selected = selectedNode ?: return
        graph.removeNode(selected)
        drawableEdges.removeAll(
            drawableEdges.filter { edge -> edge.nodeA == selectedNode || edge.nodeB == selectedNode }
        )
        deselectNode()
        actionsManager.addHistory(ActionRemove(selected))
        invalidate()
    }

    fun removeNode(drawableNode: DrawableNode, history: Boolean = true) {
        graph.removeNode(drawableNode)
        drawableEdges.removeAll(
            drawableEdges.filter { edge -> edge.nodeA == drawableNode || edge.nodeB == drawableNode }
        )
        deselectNode()
        if (history) {
            actionsManager.addHistory(ActionRemove(drawableNode))
        }
        invalidate()
    }

    private fun addDrawableEdge(nodeA: DrawableNode, nodeB: DrawableNode, history: Boolean = true) {
        if (nodeA.connectedTo.size < graph.getNodes().size - 1) {
            drawableEdges.add(DrawableEdge(drawableEdges.size + 1, nodeA, nodeB))
            drawableEdges.last().connectTo(nodeB, paint)
            if (history) {
                actionsManager.addHistory(ActionConnect(nodeA, nodeB))
            }
            invalidate()
            if (readyToRunAgain) {
                runAlgorithm()
            }
        }
    }

    private fun disconnect(nodeA: DrawableNode, nodeB: DrawableNode) {
        nodeA.disconnect(nodeB)
        invalidate()
        if (readyToRunAgain) {
            runAlgorithm()
        }
    }


    private fun reconnect(nodeA: DrawableNode, nodeB: DrawableNode) {
        nodeA.reconnect(nodeB)
        invalidate()
        if (readyToRunAgain) {
            runAlgorithm()
        }
    }

    private fun selectInitialFinalNode(x: Float, y: Float) {
        val node = getDrawableNodeAtPoint(x, y) ?: return

        if (startPoint == node) { //deselect start point
            startPoint = null
            if (endPoint != null) {
                listeners.forEach { it.onGraphNotReady() }
                pathNodesOrder.clear()
            }
            invalidate()
            return
        } else if (endPoint == node) { //deselect end point
            endPoint = null
            if (startPoint != null) {
                listeners.forEach { it.onGraphNotReady() }
                pathNodesOrder.clear()
            }
            invalidate()
            return
        }

        if (startPoint == null) {
            startPoint = node
            if (endPoint != null) listeners.forEach { it.onGraphReady() }
            invalidate()
        } else if (endPoint == null) {
            endPoint = node
            if (startPoint != null) listeners.forEach { it.onGraphReady() }
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
            for (drawableEdge in selectedNode.connectedByEdge.values) {
                val edge = drawableEdge.edge ?: continue
                if (edge.connected)
                    drawableEdge.updateWeightBox(paint)
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
        val touchedPoint = RectF(
            x - touchableSpace, y - touchableSpace,
            x + touchableSpace, y + touchableSpace
        )
        for (n in graph.getNodes()) {
            if (touchedPoint.intersect(n.rect))
                return n
        }
        return null
    }

    private fun getEdgeBoxAtPoint(x: Float, y: Float): DrawableEdge? {
        val touchedPoint = RectF(
            x - touchableSpace, y - touchableSpace,
            x + touchableSpace, y + touchableSpace
        )
        for (e in drawableEdges) {
            if (touchedPoint.intersect(e.touchableArea))
                return e
        }
        return null
    }



    fun printablePath(): String {
        if (pathNodesOrder.isEmpty()) return ""

        val stringPath: StringBuffer = StringBuffer(pathNodesOrder[0].name)
        for (i in 1 until pathNodesOrder.size) {
            stringPath.append(" -> ${pathNodesOrder[i].name}")
        }

        return stringPath.toString()
    }

    fun printableVisitedOrder(): SpannableStringBuilder {
        if (algorithm.getVisitedOrder().isEmpty()) return SpannableStringBuilder("")

        val stringPath = SpannableStringBuilder(algorithm.getVisitedOrder()[0].name)
        val color = ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent))
        stringPath.setSpan(color, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        for (i in 1 until algorithm.getVisitedOrder().size) {
            stringPath.append(" -> ${algorithm.getVisitedOrder()[i].name}")
        }

        return stringPath
    }

    fun graphDescription(): String {
        val total = context.getString(R.string.graph_information_total_of)
        val isNotConnected = context.getString(R.string.graph_information_is_not_connected)
        val isConnectedTo = context.getString(R.string.graph_information_is_connected_to)
        val nodes = context.getString(R.string.nodes)
        val node = context.getString(R.string.node)

        val stringPath: StringBuffer = StringBuffer("$total: ${graph.getNodes().size} ${
            if (graph.getNodes().size > 1) nodes else node
        }")

        for (drawableNode in graph.getNodes()) {
            if (drawableNode.edges.keys.isEmpty()) {
                stringPath.append("\n${drawableNode.name} $isNotConnected")
            } else {
                stringPath.append("\n${drawableNode.name} $isConnectedTo ${drawableNode.edges.keys}")
            }
        }

        return stringPath.toString()
    }

    fun registerListener(listener: GraphListener) {
        listeners.add(listener)
    }

    fun reset() {
        graph = DrawableGraph()
        startPoint = null
        endPoint = null
        selectedNode = null
        drawableEdges.clear()
        pathNodesOrder.clear()
        selectedOption = DJIKSTRA
        invalidate()

        listeners.forEach { it.onGraphNotReady() }
        listeners.forEach { it.onGraphNotCleanable() }
        listeners.forEach { it.onGraphNodeNotRemovable() }
    }

    fun setActionsManager(actionsManager: ActionsManager) {
        this.actionsManager = actionsManager
    }

    fun undo() {
        val action = this.actionsManager.undo() ?: return
        when(action.getType()) {
            HistoryAction.ADD -> removeNode((action as ActionAdd).getNode(), false)
            HistoryAction.REMOVE -> addDrawableNode((action as ActionRemove).getNode(), false)
            HistoryAction.CONNECT -> disconnect((action as ActionConnect).getNodeA(),
                action.getNodeB())
        }
    }

    fun redo() {
        val action = this.actionsManager.redo() ?: return
        when(action.getType()) {
            HistoryAction.ADD -> addDrawableNode((action as ActionAdd).getNode(), false)
            HistoryAction.REMOVE -> removeNode((action as ActionRemove).getNode(), false)
            HistoryAction.CONNECT -> reconnect((action as ActionConnect).getNodeA(),
                action.getNodeB())
        }
    }

}