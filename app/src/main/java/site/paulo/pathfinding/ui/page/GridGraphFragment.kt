package site.paulo.pathfinding.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_grid_graph.*
import site.paulo.pathfinding.R
import site.paulo.pathfinding.ui.MainActivity
import site.paulo.pathfinding.ui.TabReadyListener
import site.paulo.pathfinding.ui.component.graphview.HRadio
import site.paulo.pathfinding.ui.component.graphview.grid.GridGraphView
import site.paulo.pathfinding.data.model.PathFindingAlgorithms

/**
 * A placeholder fragment containing a simple view.
 */
class GridGraphFragment : Fragment(), GraphFragment, HRadio.HRadioListener<PathFindingAlgorithms> {

    private val defaultMatrixRows = 10
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
        gridGraphView.configureSides(defaultMatrixRows)
        configureSeekbar()
        horizontalRadioGroup.registerListener(this)
        gridGraphView.registerListener(activity as MainActivity)
        (activity as TabReadyListener).tabReady(gridGraphView)
    }

    private fun configureSeekbar() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                gridGraphView.configureSides(i + minMatrixGraphRows)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) { }
            override fun onStopTrackingTouch(seekBar: SeekBar) { }
        })
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

    override fun getGraph(): GridGraphView {
        return gridGraphView
    }

    override fun onChangeOption(newOption: PathFindingAlgorithms) {
        gridGraphView.setAlgorithm(newOption)
    }
}