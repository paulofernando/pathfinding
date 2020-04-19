package site.paulo.pathfinding.ui.intro

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.fragment_slide.view.*
import site.paulo.pathfinding.R
import site.paulo.pathfinding.ui.MainActivity

class IntroAdapter (private val context: Context): PagerAdapter() {

    companion object {
        const val SLIDES_COUNT = 5
    }

    private val slideHeaders = arrayOf(
        context.getString(R.string.slide_header_add),
        context.getString(R.string.slide_header_connect),
        context.getString(R.string.slide_header_weigh),
        context.getString(R.string.slide_header_choose),
        context.getString(R.string.slide_header_run))

    private val slideImages = intArrayOf(
        R.drawable.graph_add,
        R.drawable.graph_connect,
        R.drawable.graph_weight,
        R.drawable.graph_choose,
        R.drawable.graph_run
    )
    private val slideDescriptions = arrayOf(
        context.getString(R.string.slide_description_add),
        context.getString(R.string.slide_description_connect),
        context.getString(R.string.slide_description_weigh),
        context.getString(R.string.slide_description_choose),
        context.getString(R.string.slide_description_run))

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
