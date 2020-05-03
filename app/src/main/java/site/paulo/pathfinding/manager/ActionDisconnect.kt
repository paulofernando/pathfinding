package site.paulo.pathfinding.manager

import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableNode

class ActionDisconnect(private val connectedTo: HashMap<String, DrawableNode>): Action {
    private val type = HistoryAction.DISCONNECT

    override fun getType(): HistoryAction {
        return type
    }

    fun getConnections(): HashMap<String, DrawableNode> {
        return connectedTo
    }
}