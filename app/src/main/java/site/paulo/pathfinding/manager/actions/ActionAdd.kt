package site.paulo.pathfinding.manager.actions

import site.paulo.pathfinding.manager.Action
import site.paulo.pathfinding.manager.HistoryAction
import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableNode

class ActionAdd(val drawableNode: DrawableNode):
    Action {
    private val type = HistoryAction.ADD

    override fun getType(): HistoryAction {
        return type
    }

    fun undo() {

    }

}