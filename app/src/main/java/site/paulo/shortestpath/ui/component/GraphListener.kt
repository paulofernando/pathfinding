package site.paulo.shortestpath.ui.component

interface GraphListener {
    fun onGraphReady()
    fun onGraphNotReady()
    fun onGraphCleanable()
    fun onGraphNotCleanable()
}