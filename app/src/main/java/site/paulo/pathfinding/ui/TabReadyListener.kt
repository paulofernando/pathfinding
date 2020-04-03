package site.paulo.pathfinding.ui

import site.paulo.pathfinding.ui.component.graphview.GraphView

interface TabReadyListener {
    fun tabReady(graphView: GraphView)
}