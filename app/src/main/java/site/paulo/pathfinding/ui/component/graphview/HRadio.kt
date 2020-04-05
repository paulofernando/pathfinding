package site.paulo.pathfinding.ui.component.graphview

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.h_radio_drawable_items.view.*
import site.paulo.pathfinding.R
import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableItems.*

abstract class HRadio<T>(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var currentOption: T? = null
    var currentRadio: Button? = null
    private var listener: HRadioListener<T>? = null

    protected fun checkRadio(option: T) {
        currentRadio?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        currentRadio?.setBackgroundResource(R.drawable.radio)

        currentOption = option
        currentRadio = getRadio(option)
        currentRadio?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_mark, 0)
        currentRadio?.setBackgroundResource(R.drawable.radio_checked)

        listener?.onChangeOption(option)
    }

    abstract fun getRadio(option: T): Button

    fun registerListener(hRadioListener: HRadioListener<T>) {
        listener = hRadioListener
    }


    interface HRadioListener<T> {
        fun onChangeOption(newOption: T)
    }

}