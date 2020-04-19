package site.paulo.pathfinding.ui.intro.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.fragment_slide.view.*
import site.paulo.pathfinding.R

open class IntroGraphAdapter (private val context: Context): PagerAdapter() {

    lateinit var slideHeaders: Array<String>
    lateinit var slideImages: IntArray
    lateinit var slideDescriptions: Array<String>

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
