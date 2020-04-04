package site.paulo.pathfinding.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import site.paulo.pathfinding.ui.component.graphview.GraphListener
import site.paulo.pathfinding.R
import site.paulo.pathfinding.ui.component.graphview.grid.GraphView
import site.paulo.pathfinding.ui.page.SectionsPagerAdapter


class MainActivity : AppCompatActivity(),
    GraphListener, TabReadyListener {

    private lateinit var gridGraph: GraphView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        gridGraph.runAlgorithm()
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

    override fun tabReady(graphView: GraphView) {
        gridGraph = graphView
    }
}
