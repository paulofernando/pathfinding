package site.paulo.pathfinding.ui.component.graphview.grid

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import kotlinx.android.synthetic.main.h_radio_algorithms.view.*
import site.paulo.pathfinding.R
import site.paulo.pathfinding.data.model.PathFindingAlgorithms
import site.paulo.pathfinding.ui.component.graphview.HRadio
import site.paulo.pathfinding.data.model.PathFindingAlgorithms.*

class HRadioAlgorithms(context: Context, attrs: AttributeSet) :
    HRadio<PathFindingAlgorithms>(context, attrs) {

    init {
        inflate(context, R.layout.h_radio_algorithms, this)
        djikstraRadio.setOnClickListener { checkRadio(DJIKSTRA) }
        aStarRadio.setOnClickListener { checkRadio(ASTAR) }
        breadthFirstRadio.setOnClickListener { checkRadio(BREADTH_FIRST) }
        depthFirstRadio.setOnClickListener { checkRadio(DEPTH_FIRST) }
        currentRadio = djikstraRadio
    }

    override fun getRadio(option: PathFindingAlgorithms): Button {
        return when (option) {
            DJIKSTRA -> djikstraRadio
            ASTAR -> aStarRadio
            BREADTH_FIRST -> breadthFirstRadio
            DEPTH_FIRST -> depthFirstRadio
        }
    }

}