package site.paulo.shortestpath

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import site.paulo.shortestpath.algorithm.Djikstra
import site.paulo.shortestpath.data.model.MatrixGraph
import site.paulo.shortestpath.data.model.Node
import site.paulo.shortestpath.data.model.Point

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val matrix = MatrixGraph(5, 5)
        Djikstra(matrix, Point(0,0), Point(3,3)).run()
        matrix.print()
    }
}
