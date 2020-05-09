package site.paulo.pathfinding.ui.component.graphview

interface GraphListener {
    fun onGraphReady()
    fun onGraphNotReady()
    fun onGraphCleanable()
    fun onGraphNotCleanable()
    fun onGraphNodeRemovable()
    fun onGraphNodeNotRemovable()
    fun onUndoEnabled()
    fun onUndoDisabled()
    fun onRedoEnabled()
    fun onRedoDisabled()
}