package site.paulo.pathfinding.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_drawable_graph.*
import site.paulo.pathfinding.R
import site.paulo.pathfinding.data.model.PathFindingAlgorithms
import site.paulo.pathfinding.ui.MainActivity
import site.paulo.pathfinding.ui.component.graphview.HRadio
import site.paulo.pathfinding.ui.component.graphview.grid.GridGraphView


/**
 * A placeholder fragment containing a simple view.
 */
class DrawableGraphFragment : Fragment(), GraphFragment, HRadio.HRadioListener<PathFindingAlgorithms> {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drawable_graph, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        horizontalDrawableRadioGroup.registerListener(this)
        horizontalDrawableRadioGroup.hideOption(PathFindingAlgorithms.ASTAR)
        drawableGraphView.registerListener(activity as MainActivity)
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

    override fun getGraph(): GridGraphView {
        TODO("Not yet implemented")
    }

    override fun onChangeOption(newOption: PathFindingAlgorithms) {
        drawableGraphView.setAlgorithm(newOption)
    }

}