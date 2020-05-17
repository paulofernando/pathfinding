package site.paulo.pathfinding.ui.component.graphview

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import kotlinx.android.synthetic.main.h_radio_algorithms.view.*
import site.paulo.pathfinding.R
import site.paulo.pathfinding.data.model.PathFindingAlgorithms
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

    fun hideOption(option: PathFindingAlgorithms) {
        when (option) {
            DJIKSTRA -> djikstraRadio.visibility = GONE
            ASTAR -> aStarRadio.visibility = GONE
            BREADTH_FIRST -> breadthFirstRadio.visibility = GONE
            DEPTH_FIRST -> depthFirstRadio.visibility = GONE
        }
    }

}