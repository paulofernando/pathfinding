package site.paulo.pathfinding.manager.actions

import site.paulo.pathfinding.manager.Action
import site.paulo.pathfinding.manager.HistoryAction
import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableEdge

class ActionWeigh(val drawableEdge: DrawableEdge, val weight: Double):
    Action {
    private val type = HistoryAction.WEIGH

    override fun getType(): HistoryAction {
        return type
    }

}