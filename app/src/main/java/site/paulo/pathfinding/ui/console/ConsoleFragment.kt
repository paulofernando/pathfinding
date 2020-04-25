package site.paulo.pathfinding.ui.console

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import site.paulo.pathfinding.R

class ConsoleFragment(private val rows: Array<String>) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_console, container, false)

        val console = root.findViewById(R.id.consoleRecyclerView) as RecyclerView
        console.layoutManager = LinearLayoutManager(activity)
        console.adapter = ConsoleAdapter(rows)

        return root
    }

}
