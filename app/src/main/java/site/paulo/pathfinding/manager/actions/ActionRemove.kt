package site.paulo.pathfinding.manager.actions

import site.paulo.pathfinding.data.model.Edge
import site.paulo.pathfinding.manager.Action
import site.paulo.pathfinding.manager.HistoryAction
import site.paulo.pathfinding.ui.component.graphview.drawable.WeighBox
import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableNode

class ActionRemove(private val drawableNode: DrawableNode,
                   private val weighBoxes: List<WeighBox>,
                   private val edgesConnections: List<Boolean>
): Action {
    private val type = HistoryAction.REMOVE

    override fun getType(): HistoryAction {
        return type
    }

    fun getNode(): DrawableNode {
        return drawableNode
    }

    fun getDrawableEdges(): List<WeighBox> {
        return weighBoxes
    }

    fun getEdges(): List<Edge?> {
        return weighBoxes.map { drawableEdge -> drawableEdge.edge }
    }

    fun getConnections(): List<Boolean> {
        return edgesConnections
    }
}