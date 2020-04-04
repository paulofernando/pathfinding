package site.paulo.pathfinding.ui

import site.paulo.pathfinding.ui.component.graphview.grid.GraphView

interface TabReadyListener {
    fun tabReady(graphView: GraphView)
}