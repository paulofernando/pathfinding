package site.paulo.pathfinding.manager

import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableNode

class ActionConnect(private val nodeA: DrawableNode, private val nodeB: DrawableNode): Action {
    private val type = HistoryAction.CONNECT

    override fun getType(): HistoryAction {
        return type
    }

    fun getNodeA(): DrawableNode {
        return nodeA
    }

    fun getNodeB(): DrawableNode {
        return nodeB
    }
}