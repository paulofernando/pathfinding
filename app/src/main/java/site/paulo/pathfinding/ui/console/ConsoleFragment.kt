package site.paulo.pathfinding.ui.console

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_console.*

import site.paulo.pathfinding.R

class ConsoleFragment(private val rows: ArrayList<String>) : BottomSheetDialogFragment() {

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

        consoleCopyButton.setOnClickListener {
            copyGraphInformation()
        }
    }

    private fun copyGraphInformation() {
        val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText(getString(R.string.graph_information),
            rows.joinToString { it -> "${it}\n" })
        clipboard.setPrimaryClip(clip)
        Toast.makeText(activity, getString(R.string.text_copied), Toast.LENGTH_SHORT).show()
    }

}
