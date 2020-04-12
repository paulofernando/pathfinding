package site.paulo.pathfinding.ui.component.graphview.drawable

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import kotlinx.android.synthetic.main.h_radio_drawable_items.view.*
import site.paulo.pathfinding.R
import site.paulo.pathfinding.ui.component.graphview.HRadio
import site.paulo.pathfinding.ui.component.graphview.drawable.DrawableItems.*

class HRadioDrawableItem(context: Context, attrs: AttributeSet) :
    HRadio<DrawableItems>(context, attrs) {

    init {
        inflate(context, R.layout.h_radio_drawable_items, this)
        selectRadio.setOnClickListener { checkRadio(SELECT, false) }
        nodeRadio.setOnClickListener { checkRadio(NODE, false) }
        currentRadio = nodeRadio
    }

    override fun getRadio(option: DrawableItems): Button {
        return when (option) {
            NODE -> nodeRadio
            SELECT -> selectRadio
        }
    }

}