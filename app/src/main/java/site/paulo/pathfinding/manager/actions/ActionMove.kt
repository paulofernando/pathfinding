package site.paulo.pathfinding.manager.actions

import site.paulo.pathfinding.manager.Action
import site.paulo.pathfinding.manager.HistoryAction
import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableNode


class ActionMove(val node: DrawableNode, val initialPosition: Pair<Float, Float>,
                 val finalPosition: Pair<Float, Float>): Action {
    private val type = HistoryAction.MOVE

    override fun getType(): HistoryAction {
        return type
    }
}