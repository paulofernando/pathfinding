package site.paulo.pathfinding.ui.component.graphview

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.h_radio_group.view.*
import site.paulo.pathfinding.R
import site.paulo.pathfinding.ui.component.graphview.SupportedAlgorithms.*

class HRadioGroup(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var currentOption: SupportedAlgorithms = DJIKSTRA
    private var currentRadio: Button

    init {
        inflate(context, R.layout.h_radio_group, this)
        djikstraRadio.setOnClickListener { checkRadio(DJIKSTRA) }
        aStarRadio.setOnClickListener { checkRadio(ASTAR) }
        breadthFirstRadio.setOnClickListener { checkRadio(BREADTH_FIRST) }
        depthFirstRadio.setOnClickListener { checkRadio(DEPTH_FIRST) }
        currentRadio = djikstraRadio
    }


    private fun checkRadio(option: SupportedAlgorithms) {
        currentRadio.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        currentRadio.setBackgroundResource(R.drawable.radio)

        currentRadio = getRadio(option)
        currentRadio.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_mark, 0)
        currentRadio.setBackgroundResource(R.drawable.radio_checked)

        currentOption = option
    }

    private fun getRadio(option: SupportedAlgorithms): Button {
        return when (option) {
            DJIKSTRA -> djikstraRadio
            ASTAR -> aStarRadio
            BREADTH_FIRST -> breadthFirstRadio
            DEPTH_FIRST -> depthFirstRadio
        }
    }

}