package site.paulo.pathfinding.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import site.paulo.pathfinding.R
import site.paulo.pathfinding.ui.component.graphview.GraphView

/**
 * A placeholder fragment containing a simple view.
 */
class DrawableGraphFragment : Fragment(), GraphFragment {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drawable_graph, container, false)
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "drawable_graph"

        @JvmStatic
        fun newInstance(sectionNumber: Int): DrawableGraphFragment {
            return DrawableGraphFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun getGraph(): GraphView {
        TODO("Not yet implemented")
    }
}