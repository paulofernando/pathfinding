package site.paulo.pathfinding.ui.console

import android.content.Context
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import site.paulo.pathfinding.R


class ConsoleAdapter(val context: Context, private val rows: ArrayList<SpannableString>) :
    RecyclerView.Adapter<ConsoleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsoleAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.console_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = rows[position]
    }

    override fun getItemCount() = rows.size

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textView: TextView = v.findViewById(R.id.consoleRow)
    }

}