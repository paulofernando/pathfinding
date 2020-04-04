package site.paulo.pathfinding.ui.component.graphview.grid

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.h_radio_algorithms.view.*
import site.paulo.pathfinding.R
import site.paulo.pathfinding.ui.component.graphview.PathFindingAlgorithms
import site.paulo.pathfinding.ui.component.graphview.PathFindingAlgorithms.*

class HRadioAlgorithms(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var currentOption: PathFindingAlgorithms = DJIKSTRA
    private var currentRadio: Button
    private var listener: HRadioListener? = null

    init {
        inflate(context, R.layout.h_radio_algorithms, this)
        djikstraRadio.setOnClickListener { checkRadio(DJIKSTRA) }
        aStarRadio.setOnClickListener { checkRadio(ASTAR) }
        breadthFirstRadio.setOnClickListener { checkRadio(BREADTH_FIRST) }
        depthFirstRadio.setOnClickListener { checkRadio(DEPTH_FIRST) }
        currentRadio = djikstraRadio
    }


    private fun checkRadio(option: PathFindingAlgorithms) {
        currentRadio.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        currentRadio.setBackgroundResource(R.drawable.radio)

        currentOption = option
        currentRadio = getRadio(option)
        currentRadio.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_mark, 0)
        currentRadio.setBackgroundResource(R.drawable.radio_checked)

        listener?.onChangeOption(option)
    }

    private fun getRadio(option: PathFindingAlgorithms): Button {
        return when (option) {
            DJIKSTRA -> djikstraRadio
            ASTAR -> aStarRadio
            BREADTH_FIRST -> breadthFirstRadio
            DEPTH_FIRST -> depthFirstRadio
        }
    }

    fun registerListener(hRadioListener: HRadioListener) {
        listener = hRadioListener
    }


    interface HRadioListener {
        fun onChangeOption(newOption: PathFindingAlgorithms)
    }

}