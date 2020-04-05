package site.paulo.pathfinding.ui.component.graphview.drawable

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.h_radio_drawable_items.view.*
import site.paulo.pathfinding.R
import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableItems.*

class HRadioDrawableItem(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var currentOption: DrawableItems = NODE
    private var currentRadio: Button
    private var listener: HRadioListener? = null

    init {
        inflate(context, R.layout.h_radio_drawable_items, this)
        nodeRadio.setOnClickListener { checkRadio(NODE) }
        edgeRadio.setOnClickListener { checkRadio(EDGE) }
        currentRadio = nodeRadio
    }


    private fun checkRadio(option: DrawableItems) {
        currentRadio.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        currentRadio.setBackgroundResource(R.drawable.radio)

        currentOption = option
        currentRadio = getRadio(option)
        currentRadio.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_mark, 0)
        currentRadio.setBackgroundResource(R.drawable.radio_checked)

        listener?.onChangeOption(option)
    }

    private fun getRadio(option: DrawableItems): Button {
        return when (option) {
            NODE -> nodeRadio
            EDGE -> edgeRadio
        }
    }

    fun registerListener(hRadioListener: HRadioListener) {
        listener = hRadioListener
    }


    interface HRadioListener {
        fun onChangeOption(newOption: DrawableItems)
    }

}