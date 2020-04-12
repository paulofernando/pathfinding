package site.paulo.pathfinding.ui.page

import site.paulo.pathfinding.ui.component.graphview.grid.GridGraphView

interface GraphFragment {
    fun getGraph(): GridGraphView
}