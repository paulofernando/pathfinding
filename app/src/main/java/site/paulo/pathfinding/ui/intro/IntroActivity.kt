package site.paulo.pathfinding.ui.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_intro.*
import site.paulo.pathfinding.R
import site.paulo.pathfinding.ui.intro.IntroAdapter.Companion.SLIDES_COUNT

class IntroActivity : AppCompatActivity() {

    private var currentPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        slideViewPage.adapter = IntroAdapter(this)

        val spannable = SpannableString("\u2022".repeat(SLIDES_COUNT))
        val color = ForegroundColorSpan(ContextCompat.getColor(applicationContext, R.color.colorSelectedDot))
        spannable.setSpan(color, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        dots.text = spannable

        slideViewPage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) { }
            override fun onPageScrolled(position: Int,
                positionOffset: Float, positionOffsetPixels: Int) { }

            override fun onPageSelected(position: Int) {
                currentPage = position
                spannable.setSpan(color, position, position + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                dots.text = spannable

                introNextButton.visibility = View.VISIBLE
                introBackButton.visibility = View.VISIBLE
                introNextButton.text = getString(R.string.next)

                if (position == 0) {
                    introBackButton.visibility = View.INVISIBLE
                } else if (position == (SLIDES_COUNT - 1)) {
                    introNextButton.text = getString(R.string.finish)
                }
            }
        })
    }

    fun previousPage(view: View) {
        slideViewPage.currentItem = currentPage - 1
    }

    fun nextPage(view: View) {
        if (currentPage < (SLIDES_COUNT - 1))
            slideViewPage.currentItem = currentPage + 1
        else
            finish()
    }

    fun closePage(view: View) {
        finish()
    }
}
