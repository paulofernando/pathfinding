package site.paulo.pathfinding.manager.actions

import site.paulo.pathfinding.manager.Action
import site.paulo.pathfinding.manager.HistoryAction
import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableNode


class ActionConnect(val nodeA: DrawableNode, val nodeB: DrawableNode): Action {
    private val type = HistoryAction.CONNECT

    override fun getType(): HistoryAction {
        return type
    }
}