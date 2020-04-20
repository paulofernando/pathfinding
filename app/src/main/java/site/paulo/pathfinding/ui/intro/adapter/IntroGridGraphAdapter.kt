package site.paulo.pathfinding.ui.intro.adapter

import android.content.Context
import site.paulo.pathfinding.R

class IntroGridGraphAdapter (context: Context): IntroGraphAdapter(context) {

    init {
        slideHeaders = arrayOf(
            context.getString(R.string.slide_header_add),
            context.getString(R.string.slide_wall_weigh),
            context.getString(R.string.slide_header_run))

        slideImages = intArrayOf(
            R.drawable.grid_add,
            R.drawable.grid_wall,
            R.drawable.grid_run
        )

        slideDescriptions = arrayOf(
            context.getString(R.string.slide_desc_grid_add),
            context.getString(R.string.slide_desc_grid_wall),
            context.getString(R.string.slide_desc_grid_run))
    }

}
