package site.paulo.pathfinding.ui.component.graphview.intro

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import site.paulo.pathfinding.R
import kotlinx.android.synthetic.main.fragment_slide.view.*

class IntroAdapter (private val context: Context): PagerAdapter() {

    companion object {
        const val SLIDES_COUNT = 5
    }

    private val slideHeaders = arrayOf("Add", "Connect", "Weight", "Choose", "Run")
    private val slideImages = intArrayOf(
        R.drawable.graph_add,
        R.drawable.graph_connect,
        R.drawable.graph_weight,
        R.drawable.graph_choose,
        R.drawable.graph_run
    )
    private val slideDescriptions = arrayOf("Description 1", "Description 2", "Description 3",
        "Description 4", "Description 5")

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return slideHeaders.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.fragment_slide, container, false)

        view.headerTextView.text = slideHeaders[position].toUpperCase()
        view.stepImage.setImageResource(slideImages[position])
        view.descriptionTextView.text = slideDescriptions[position]

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}
