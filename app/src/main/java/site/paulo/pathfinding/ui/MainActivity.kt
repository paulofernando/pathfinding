package site.paulo.pathfinding.ui

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_drawable_graph.*
import site.paulo.pathfinding.ui.component.graphview.GraphListener
import site.paulo.pathfinding.ui.component.graphview.grid.GridGraphView
import site.paulo.pathfinding.ui.page.SectionsPagerAdapter
import site.paulo.pathfinding.R


class MainActivity : AppCompatActivity(),
    GraphListener, TabReadyListener {

    private lateinit var gridGridGraph: GridGraphView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runImageView.isEnabled = false
        clearImageView.isEnabled = false

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(0)?.setIcon(R.drawable.ic_graph)
        tabs.getTabAt(1)?.setIcon(R.drawable.ic_grid)

        tabs.getTabAt(0)?.icon?.setColorFilter(ContextCompat
            .getColor(tabs.context, R.color.colorSelectedTabIcon), PorterDuff.Mode.SRC_IN)
        tabs.getTabAt(1)?.icon?.setColorFilter(ContextCompat
            .getColor(tabs.context, R.color.colorTabIcon), PorterDuff.Mode.SRC_IN)

        tabs.addOnTabSelectedListener(
            object : TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    super.onTabSelected(tab)
                    tab.icon?.setColorFilter(ContextCompat
                        .getColor(tabs.context, R.color.colorSelectedTabIcon),
                        PorterDuff.Mode.SRC_IN)

                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    super.onTabUnselected(tab)
                    tab.icon?.setColorFilter(ContextCompat
                        .getColor(tabs.context, R.color.colorTabIcon),
                        PorterDuff.Mode.SRC_IN)
                }
            }
        )

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
        if (viewPager.currentItem == 0)
            drawableGraphView.runAlgorithm()
        else
            gridGridGraph.runAlgorithm()

    }

    fun reset(view: View) {
        gridGridGraph.reset()
        drawableGraphView.reset()
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

    override fun tabReady(gridGraphView: GridGraphView) {
        gridGridGraph = gridGraphView
    }
}
