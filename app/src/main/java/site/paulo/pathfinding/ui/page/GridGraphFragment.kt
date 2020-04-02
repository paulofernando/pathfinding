package site.paulo.pathfinding.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_grid_graph.*
import site.paulo.pathfinding.R
import site.paulo.pathfinding.ui.component.graphview.GraphView

/**
 * A placeholder fragment containing a simple view.
 */
class GridGraphFragment : Fragment() {

    private val minMatrixGraphRows: Int = 5

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_grid_graph, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureSeekbar()
    }

    private fun configureSeekbar() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                graphView.configureSides(i + minMatrixGraphRows)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) { }
            override fun onStopTrackingTouch(seekBar: SeekBar) { }
        })
    }

    fun getGraphView(): GraphView {
        return graphView
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "grid_graph"

        @JvmStatic
        fun newInstance(sectionNumber: Int): GridGraphFragment {
            return GridGraphFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}