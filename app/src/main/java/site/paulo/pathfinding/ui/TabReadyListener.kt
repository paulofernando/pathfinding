package site.paulo.pathfinding.ui

import site.paulo.pathfinding.ui.component.graphview.grid.GridGraphView

interface TabReadyListener {
    fun tabReady(gridGraphView: GridGraphView)
}