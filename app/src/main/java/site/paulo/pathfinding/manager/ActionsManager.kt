package site.paulo.pathfinding.manager

import site.paulo.pathfinding.ui.component.graphview.GraphListener
import java.util.*


class ActionsManager(private val listener: GraphListener) {
    private val actions = Stack<Action>()
    private val redoActions = Stack<Action>()

    fun undo(): Action? {
        if (actions.isEmpty()) return null
        val action = actions.pop()
        redoActions.add(action)
        listener.onRedoEnabled()
        if(actions.isEmpty())
            listener.onUndoDisabled()

        return action
    }

    fun redo(): Action? {
        if (redoActions.isEmpty()) return null
        val action = redoActions.pop()
        actions.add(action)
        listener.onUndoEnabled()
        if (redoActions.isEmpty())
            listener.onRedoDisabled()

        return action
    }

    fun addHistory(action: Action) {
        actions.push(action)
        redoActions.clear()
        listener.onUndoEnabled()
        listener.onRedoDisabled()
    }

    fun clearHistory() {
        actions.clear()
        redoActions.clear()
        listener.onUndoDisabled()
        listener.onRedoDisabled()
    }
}