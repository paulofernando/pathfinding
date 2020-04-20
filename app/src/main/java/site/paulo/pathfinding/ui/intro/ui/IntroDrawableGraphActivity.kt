package site.paulo.pathfinding.ui.intro.ui

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_intro.*
import site.paulo.pathfinding.ui.intro.adapter.IntroDrawableGraphAdapter

class IntroDrawableGraphActivity : IntroGraphActivity(5) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slideViewPage.adapter = IntroDrawableGraphAdapter(this)
    }
}
