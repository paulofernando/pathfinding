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
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import site.paulo.pathfinding.ui.component.graphview.GraphListener
import site.paulo.pathfinding.ui.component.graphview.HRadioGroup
import site.paulo.pathfinding.ui.component.graphview.PathFindingAlgorithms
import site.paulo.pathfinding.R
import site.paulo.pathfinding.ui.page.SectionsPagerAdapter


class MainActivity : AppCompatActivity(),
    GraphListener, HRadioGroup.HRadioListener {

    private val minMatrixGraphRows: Int = 5
    private val defaultMatrixRows = 10
    private var selectedAlgorithm = PathFindingAlgorithms.DJIKSTRA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        horizontalRadioGroup.registerListener(this)
        //graphView.registerListener(this)
        runImageView.isEnabled = false
        clearImageView.isEnabled = false

        configureSeekbar()
        //graphView.configureSides(defaultMatrixRows)

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
        //graphView.runAlgorithm(selectedAlgorithm)
    }

    fun reset(view: View) {
        //graphView.reset()
        clearImageView.isEnabled = false
    }

    private fun configureSeekbar() {
//        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
//                graphView.configureSides(i + minMatrixGraphRows)
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) { }
//            override fun onStopTrackingTouch(seekBar: SeekBar) { }
//        })
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
//        selectedAlgorithm = newOption
//        when(newOption) {
//            PathFindingAlgorithms.DJIKSTRA -> graphView.enableWeightIncrease(true)
//            PathFindingAlgorithms.ASTAR -> graphView.enableWeightIncrease(true)
//            PathFindingAlgorithms.BREADTH_FIRST -> graphView.enableWeightIncrease(false)
//            PathFindingAlgorithms.DEPTH_FIRST -> graphView.enableWeightIncrease(false)
//        }
    }
}
