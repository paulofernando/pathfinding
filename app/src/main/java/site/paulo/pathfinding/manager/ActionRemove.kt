package site.paulo.pathfinding.manager

import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableEdge
import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableNode

class ActionRemove(private val drawableNode: DrawableNode,
                   private val drawableEdges: List<DrawableEdge>): Action {
    private val type = HistoryAction.REMOVE

    override fun getType(): HistoryAction {
        return type
    }

    fun getNode(): DrawableNode {
        return drawableNode
    }

    fun getEdges(): List<DrawableEdge> {
        return drawableEdges
    }
}