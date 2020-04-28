package site.paulo.pathfinding.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.preference.PreferenceManager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_drawable_graph.*
import site.paulo.pathfinding.R
import site.paulo.pathfinding.ui.component.graphview.GraphListener
import site.paulo.pathfinding.ui.component.graphview.grid.GridGraphView
import site.paulo.pathfinding.ui.intro.ui.IntroDrawableGraphActivity
import site.paulo.pathfinding.ui.intro.ui.IntroGridGraphActivity
import site.paulo.pathfinding.ui.console.ConsoleFragment
import site.paulo.pathfinding.ui.page.SectionsPagerAdapter


class MainActivity : AppCompatActivity(),
    GraphListener, TabReadyListener {

    private lateinit var gridGridGraph: GridGraphView
    private var nodeRemovable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runImageView.isEnabled = false
        removeNodeImageView.isEnabled = false

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)

        configureTabs()
    }

    private fun configureTabs() {
        tabs.getTabAt(0)?.setIcon(R.drawable.ic_graph)
        tabs.getTabAt(1)?.setIcon(R.drawable.ic_grid)

        tabs.getTabAt(0)?.icon?.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(ContextCompat
            .getColor(tabs.context, R.color.colorSelectedTabIcon), BlendModeCompat.SRC_ATOP)
        tabs.getTabAt(1)?.icon?.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(ContextCompat
            .getColor(tabs.context, R.color.colorTabIcon), BlendModeCompat.SRC_ATOP)

        tabs.addOnTabSelectedListener(
            object : TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    super.onTabSelected(tab)
                    tab.icon?.colorFilter =
                        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                            ContextCompat.getColor(tabs.context, R.color.colorSelectedTabIcon),
                            BlendModeCompat.SRC_ATOP)

                    if (viewPager.currentItem == 0) {
                        runImageView.isEnabled = drawableGraphView.isReadyToRun()
                        removeNodeImageView.isEnabled = nodeRemovable
                        removeNodeImageView.visibility = View.VISIBLE
                        consoleImageView.visibility = View.VISIBLE
                    } else if (viewPager.currentItem == 1) {
                        runImageView.isEnabled = gridGridGraph.isReadyToRun()
                        removeNodeImageView.isEnabled = false
                        removeNodeImageView.visibility = View.GONE
                        consoleImageView.visibility = View.GONE
                    }

                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    super.onTabUnselected(tab)
                    tab.icon?.colorFilter =
                        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                            ContextCompat.getColor(tabs.context, R.color.colorTabIcon),
                            BlendModeCompat.SRC_ATOP)
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(baseContext)
        val previouslyStarted =
            prefs.getBoolean(getString(R.string.pref_previously_started), false)
        if (!previouslyStarted) {
            val edit = prefs.edit()
            edit.putBoolean(getString(R.string.pref_previously_started), true)
            edit.apply()
            showIntro()
        }
    }

    private fun showIntro() {
        startActivity(Intent(this, IntroGridGraphActivity::class.java))
        startActivity(Intent(this, IntroDrawableGraphActivity::class.java))
    }

    fun runAlgorithm(view: View) {
        if (viewPager.currentItem == 0)
            drawableGraphView.runAlgorithm()
        else
            gridGridGraph.runAlgorithm()
    }

    fun removeNode(view: View) {
        drawableGraphView.removeSelectedNode()
    }

    fun openConsole(view: View) {
        if (viewPager.currentItem == 0) {
            val rows = ArrayList<String>()
            rows.add(drawableGraphView.graphDescription())

            val visited = drawableGraphView.printableVisitedOrder()
            if (visited.isNotEmpty()) {
                rows.add("\nPath:")
                rows.add(drawableGraphView.printableVisitedOrder())
            }
            val consoleFragment = ConsoleFragment(rows)
            consoleFragment.show(supportFragmentManager, "add_console_dialog_fragment")
        }
    }

    fun callMenuAbout(view: View) {
        startActivity(Intent(this, AboutActivity::class.java))
    }

    fun reset(view: View) {
        if(tabs.selectedTabPosition == 0)
            drawableGraphView.reset()
        if(tabs.selectedTabPosition == 1)
            gridGridGraph.reset()
    }

    override fun onGraphReady() {
         runImageView.isEnabled = true
    }

    override fun onGraphNotReady() {
        runImageView.isEnabled = false
    }

    override fun onGraphCleanable() { }

    override fun onGraphNotCleanable() { }

    override fun onGraphNodeRemovable() {
        nodeRemovable = true
        removeNodeImageView.isEnabled = nodeRemovable
    }

    override fun onGraphNodeNotRemovable() {
        nodeRemovable = false
        removeNodeImageView.isEnabled = nodeRemovable
    }

    override fun tabReady(gridGraphView: GridGraphView) {
        gridGridGraph = gridGraphView
    }

}
