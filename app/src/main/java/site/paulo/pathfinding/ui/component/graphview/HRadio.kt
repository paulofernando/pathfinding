package site.paulo.pathfinding.ui.component.graphview

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.FrameLayout
import site.paulo.pathfinding.R


abstract class HRadio<T>(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private var currentOption: T? = null
    var currentRadio: Button? = null
    private var listener: HRadioListener<T>? = null

    fun checkRadio(option: T, showCheckMark: Boolean = true) {
        currentRadio?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        currentRadio?.setBackgroundResource(R.drawable.radio)

        currentOption = option
        currentRadio = getRadio(option)
        if (showCheckMark) {
            currentRadio?.setCompoundDrawablesWithIntrinsicBounds(
                0, 0, R.drawable.ic_check_mark, 0)
        }
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