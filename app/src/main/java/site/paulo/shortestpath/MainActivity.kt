package site.paulo.shortestpath

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import site.paulo.shortestpath.data.model.MatrixGraph
import site.paulo.shortestpath.data.model.Node

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val matrix = MatrixGraph(3, 3)
        matrix.print()
    }
}
