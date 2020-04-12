package site.paulo.pathfinding.data.model

import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableNode
import java.util.*

class DrawableGraph: Graph<DrawableNode> {

    private val drawableNodes: LinkedList<DrawableNode> = LinkedList()

    override fun getNodes() : LinkedList<DrawableNode> {
        return drawableNodes
    }

    fun getNode(id: String): DrawableNode? {
        for (node in drawableNodes) {
            if (node.id == id)
                return node
        }
        return null
    }

    fun addNode(node: DrawableNode) {
        drawableNodes.add(node)
    }

}