package site.paulo.pathfinding.manager

import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableEdge
import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableNode

interface Action {
    fun getType(): HistoryAction
}