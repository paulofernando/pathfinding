package site.paulo.pathfinding.ui.console

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import site.paulo.pathfinding.R

class ConsoleAdapter(private val rows: Array<String>) :
    RecyclerView.Adapter<ConsoleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsoleAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.console_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("ConsoleAdapter", "Element $position set.")
        holder.textView.text = rows[position]
    }

    override fun getItemCount() = rows.size

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textView: TextView = v.findViewById(R.id.consoleRow)
    }

}