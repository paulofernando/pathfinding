package site.paulo.pathfinding.ui.intro.adapter

import android.content.Context
import site.paulo.pathfinding.R


class IntroDrawableGraphAdapter (context: Context): IntroGraphAdapter(context) {

    init {
        slideHeaders = arrayOf(
            context.getString(R.string.slide_header_add),
            context.getString(R.string.slide_header_connect),
            context.getString(R.string.slide_header_weigh),
            context.getString(R.string.slide_header_choose),
            context.getString(R.string.slide_header_run))

        slideImages = intArrayOf(
            R.drawable.graph_add,
            R.drawable.graph_connect,
            R.drawable.graph_weight,
            R.drawable.graph_choose,
            R.drawable.graph_run
        )
        slideDescriptions = arrayOf(
            context.getString(R.string.slide_desc_drawable_add),
            context.getString(R.string.slide_desc_drawable_connect),
            context.getString(R.string.slide_desc_drawable_weigh),
            context.getString(R.string.slide_desc_drawable_choose),
            context.getString(R.string.slide_desc_drawable_run))
    }

}
