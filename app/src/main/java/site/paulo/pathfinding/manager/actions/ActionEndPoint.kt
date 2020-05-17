package site.paulo.pathfinding.manager.actions

import site.paulo.pathfinding.manager.Action
import site.paulo.pathfinding.manager.HistoryAction
import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableNode


class ActionEndPoint(val node: DrawableNode): Action {
    private val type = HistoryAction.END_POINT

    override fun getType(): HistoryAction {
        return type
    }
}