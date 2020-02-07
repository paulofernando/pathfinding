package site.paulo.shortestpath.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import site.paulo.shortestpath.R
import site.paulo.shortestpath.algorithm.Djikstra
import site.paulo.shortestpath.data.model.MatrixGraph
import site.paulo.shortestpath.data.model.Point
import site.paulo.shortestpath.ui.component.GraphView

class MainActivity : AppCompatActivity() {

    private lateinit var graphView: GraphView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        graphView = GraphView(this)

        /*val matrix = MatrixGraph(5, 5)
        matrix.print()
        Djikstra(matrix, Point(0,0), Point(3,3)).run()*/
    }
}
