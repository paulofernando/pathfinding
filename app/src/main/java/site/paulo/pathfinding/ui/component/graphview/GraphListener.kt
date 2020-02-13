package site.paulo.pathfinding.ui.component.graphview

interface GraphListener {
    fun onGraphReady()
    fun onGraphNotReady()
    fun onGraphCleanable()
    fun onGraphNotCleanable()
}