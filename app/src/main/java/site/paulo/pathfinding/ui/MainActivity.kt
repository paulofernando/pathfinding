package site.paulo.pathfinding.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_grid_graph.*
import kotlinx.android.synthetic.main.fragment_grid_graph.view.*
import site.paulo.pathfinding.ui.component.graphview.GraphListener
import site.paulo.pathfinding.ui.component.graphview.HRadioGroup
import site.paulo.pathfinding.ui.component.graphview.PathFindingAlgorithms
import site.paulo.pathfinding.R
import site.paulo.pathfinding.ui.component.graphview.GraphView
import site.paulo.pathfinding.ui.page.GridGraphFragment
import site.paulo.pathfinding.ui.page.SectionsPagerAdapter


class MainActivity : AppCompatActivity(),
    GraphListener, HRadioGroup.HRadioListener, TabReadyListener {

    private var selectedAlgorithm = PathFindingAlgorithms.DJIKSTRA
    private lateinit var gridGraph: GraphView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        horizontalRadioGroup.registerListener(this)
        runImageView.isEnabled = false
        clearImageView.isEnabled = false


        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun runAlgorithm(view: View) {
        gridGraph.runAlgorithm(selectedAlgorithm)
    }

    fun reset(view: View) {
        gridGraph.reset()
        clearImageView.isEnabled = false
    }

    override fun onGraphReady() {
        runImageView.isEnabled = true
    }

    override fun onGraphNotReady() {
        runImageView.isEnabled = false
    }

    override fun onGraphCleanable() {
        clearImageView.isEnabled = true
    }

    override fun onGraphNotCleanable() {
        clearImageView.isEnabled = false
    }

    override fun onChangeOption(newOption: PathFindingAlgorithms) {
        selectedAlgorithm = newOption
        when(newOption) {
            PathFindingAlgorithms.DJIKSTRA -> gridGraph.enableWeightIncrease(true)
            PathFindingAlgorithms.ASTAR -> gridGraph.enableWeightIncrease(true)
            PathFindingAlgorithms.BREADTH_FIRST -> gridGraph.enableWeightIncrease(false)
            PathFindingAlgorithms.DEPTH_FIRST -> gridGraph.enableWeightIncrease(false)
        }
    }

    override fun tabReady(graphView: GraphView) {
        gridGraph = graphView
    }
}
