package site.paulo.shortestpath.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import site.paulo.shortestpath.R
import kotlinx.android.synthetic.main.activity_main.*
import site.paulo.shortestpath.ui.component.GraphListener
import site.paulo.shortestpath.ui.component.GraphView

class MainActivity : AppCompatActivity(), GraphListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        graphView.registerListener(this)
        runImageView.isEnabled = false
        clearImageView.isEnabled = false
    }

    fun runAlgorithm(view: View) {
        graphView.runAlgorithm(GraphView.SupportedAlgorithms.DJIKSTRA)
    }

    fun reset(view: View) {
        graphView.reset()
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

    }
}
