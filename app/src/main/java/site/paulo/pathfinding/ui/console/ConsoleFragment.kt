package site.paulo.pathfinding.ui.console

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).setBackgroundColor(Color.TRANSPARENT)
        val resources = resources

        assert(view != null)
        val parent = view?.parent as View
        val layoutParams = parent.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.setMargins(
            resources.getDimensionPixelSize(R.dimen.console_margin),
            0,
            resources.getDimensionPixelSize(R.dimen.console_margin),
            0
        )
        parent.layoutParams = layoutParams

    }
}
