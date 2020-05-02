package site.paulo.pathfinding.manager

import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableNode

class ActionRemove(val drawableNode: DrawableNode): Action {
    private val type = HistoryAction.REMOVE

    override fun getType(): HistoryAction {
        return type
    }

    override fun getNode(): DrawableNode {
        return drawableNode
    }
}