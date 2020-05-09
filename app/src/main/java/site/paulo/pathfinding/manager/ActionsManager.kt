package site.paulo.pathfinding.manager

import android.util.Log
import site.paulo.pathfinding.manager.actions.ActionAdd
import site.paulo.pathfinding.ui.component.graphview.GraphListener
import java.util.*

class ActionsManager(private val listener: GraphListener) {
    private val actions = Stack<Action>()
    private val redoActions = Stack<Action>()

    fun undo(): Action? {
        if (actions.isEmpty()) return null
        val action = actions.pop()
        if(actions.isEmpty())
            listener.onUndoDisabled()

        redoActions.add(action)
        listener.onRedoEnabled()
        return action
    }

    fun redo(): Action? {
        if (redoActions.isEmpty()) return null
        val action = redoActions.pop()
        if (redoActions.isEmpty())
            listener.onRedoDisabled()

        actions.add(action)
        listener.onUndoEnabled()
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