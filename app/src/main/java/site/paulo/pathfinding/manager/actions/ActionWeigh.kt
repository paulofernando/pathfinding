package site.paulo.pathfinding.manager.actions

import site.paulo.pathfinding.manager.Action
import site.paulo.pathfinding.manager.HistoryAction
import site.paulo.pathfinding.ui.component.graphview.drawable.WeighBox

class ActionWeigh(val weighBox: WeighBox, val weight: Double): Action {
    private val type = HistoryAction.WEIGH

    override fun getType(): HistoryAction {
        return type
    }

}