package site.paulo.pathfinding.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import site.paulo.pathfinding.R
import kotlinx.android.synthetic.main.activity_main.*
import site.paulo.pathfinding.ui.component.GraphListener
import site.paulo.pathfinding.ui.component.GraphView

class MainActivity : AppCompatActivity(), GraphListener {

    private val minMatrixGraphRows: Int = 5
    private val defaultMatrixRows = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        graphView.registerListener(this)
        runImageView.isEnabled = false
        clearImageView.isEnabled = false

        configureSeekbar()
        graphView.configureSides(defaultMatrixRows)
    }

    fun runAlgorithm(view: View) {
        graphView.runAlgorithm(GraphView.SupportedAlgorithms.DJIKSTRA)
    }

    fun reset(view: View) {
        graphView.reset()
        clearImageView.isEnabled = false
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
}
