package site.paulo.pathfinding.manager

import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableNode

class ActionEndPoint(val node: DrawableNode): Action {
    private val type = HistoryAction.END_POINT

    override fun getType(): HistoryAction {
        return type
    }
}