package site.paulo.pathfinding.ui.page

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import site.paulo.pathfinding.R
import site.paulo.pathfinding.ui.component.graphview.grid.GridGraphView

private val TAB_TITLES = arrayOf(
        R.string.tab_1,
        R.string.tab_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    private lateinit var gridGraphFragment: GridGraphFragment
    private lateinit var drawableGraphFragment: DrawableGraphFragment

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                drawableGraphFragment = DrawableGraphFragment.newInstance(position + 1)
                drawableGraphFragment
            }
            else -> {
                gridGraphFragment = GridGraphFragment.newInstance(position + 1)
                gridGraphFragment
            }
        }
    }

    fun getGraph(position: Int): GridGraphView {
        return when (position) {
            0 -> gridGraphFragment.getGraph()
            else -> drawableGraphFragment.getGraph()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}