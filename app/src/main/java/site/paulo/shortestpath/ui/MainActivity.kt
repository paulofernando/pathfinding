package site.paulo.shortestpath.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import site.paulo.shortestpath.R
import site.paulo.shortestpath.algorithm.Djikstra
import site.paulo.shortestpath.data.model.MatrixGraph
import kotlinx.android.synthetic.main.activity_main.*
import site.paulo.shortestpath.ui.component.GraphView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun runAlgorithm(view: View) {
        /*val matrix = MatrixGraph(10, 10)
        matrix.removeNode(Pair(2,0))
        matrix.print()*/
        graphView.runAlgorithm(GraphView.SupportedAlgorithms.DJIKSTRA)
    }
}
