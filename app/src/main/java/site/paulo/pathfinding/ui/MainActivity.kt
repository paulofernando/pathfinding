package site.paulo.pathfinding.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.preference.PreferenceManager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_drawable_graph.*
import site.paulo.pathfinding.R
import site.paulo.pathfinding.manager.ActionsManager
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
    private val actionsManager = ActionsManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runImageView.isEnabled = false
        removeNodeImageView.isEnabled = false
        undoImageView.isEnabled = false
        redoImageView.isEnabled = false

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
                        undoImageView.visibility = View.VISIBLE
                        redoImageView.visibility = View.VISIBLE
                    } else if (viewPager.currentItem == 1) {
                        runImageView.isEnabled = gridGridGraph.isReadyToRun()
                        removeNodeImageView.isEnabled = false
                        removeNodeImageView.visibility = View.GONE
                        consoleImageView.visibility = View.GONE
                        undoImageView.visibility = View.GONE
                        redoImageView.visibility = View.GONE
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
        vibratePhone()
        if (viewPager.currentItem == 0)
            drawableGraphView.runAlgorithm()
        else
            gridGridGraph.runAlgorithm()
    }

    fun removeNode(view: View) {
        vibratePhone()
        drawableGraphView.removeSelectedNode()
    }

    fun openConsole(view: View) {
        vibratePhone()
        if (viewPager.currentItem == 0) {
            val consoleFragment = ConsoleFragment(getConsoleContent())
            consoleFragment.show(supportFragmentManager, "add_console_dialog_fragment")
        }
    }

    private fun getConsoleContent(): ArrayList<SpannableString> {
        val rows = ArrayList<SpannableString>()
        rows.add(SpannableString(drawableGraphView.graphDescription()))

        val visited = drawableGraphView.printablePath()
        val visitOrderText = "\n${getString(R.string.graph_information_visit_order)}: "
        val pathText = "\n${getString(R.string.graph_information_path)}: "
        if (visited.isNotEmpty()) {
            val printableVisitOrder = SpannableString("$visitOrderText${drawableGraphView.printableVisitedOrder()}")
            val printablePath = SpannableString("$pathText${drawableGraphView.printablePath()}")

            var color = ForegroundColorSpan(ContextCompat.getColor(applicationContext, R.color.colorStartPoint))
            printableVisitOrder.setSpan(color, 0, visitOrderText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            printablePath.setSpan(color, 0, pathText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            color = ForegroundColorSpan(ContextCompat.getColor(applicationContext, R.color.colorAccent))
            printableVisitOrder.setSpan(color, visitOrderText.length, printableVisitOrder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            printablePath.setSpan(color, pathText.length, printablePath.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            rows.add(printableVisitOrder)
            rows.add(printablePath)
        }

        return rows
    }

    fun callMenuAbout(view: View) {
        startActivity(Intent(this, AboutActivity::class.java))
    }

    fun reset(view: View) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle(getString(R.string.delete_graph))
        builder.setMessage(getString(R.string.confirm_delete))
        builder.setPositiveButton(getString(R.string.yes)){_, _ ->
            if(tabs.selectedTabPosition == 0)
                drawableGraphView.reset()
            if(tabs.selectedTabPosition == 1)
                gridGridGraph.reset()
        }
        builder.setNeutralButton(getString(R.string.cancel)){_,_ -> }
        builder.create().show()
    }

    fun undo(view: View) {
        vibratePhone()
        drawableGraphView.undo()
    }

    fun redo(view: View) {
        vibratePhone()
        drawableGraphView.redo()
    }

    private fun vibratePhone() {
        val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(10)
        }
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

    override fun onUndoEnabled() {
        undoImageView.isEnabled = true
    }

    override fun onUndoDisabled() {
        undoImageView.isEnabled = false
    }

    override fun onRedoEnabled() {
        redoImageView.isEnabled = true
    }

    override fun onRedoDisabled() {
        redoImageView.isEnabled = false
    }

    override fun tabReady(gridGraphView: GridGraphView) {
        gridGridGraph = gridGraphView
        drawableGraphView.setActionsManager(actionsManager)
    }

}
