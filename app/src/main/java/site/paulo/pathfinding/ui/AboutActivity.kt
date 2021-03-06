package site.paulo.pathfinding.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_about.*
import site.paulo.pathfinding.R
import site.paulo.pathfinding.ui.intro.ui.IntroDrawableGraphActivity
import site.paulo.pathfinding.ui.intro.ui.IntroGridGraphActivity


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setSupportActionBar(aboutToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        populateAboutList()
    }


    private fun populateAboutList() {
        val generalList = buildGeneralItems()
        val from = arrayOf("name", "description")
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)

        aboutGeneralListView.adapter = SimpleAdapter(this, generalList,
            android.R.layout.simple_list_item_2, from, to)
    }

    private fun buildGeneralItems(): ArrayList<Map<String, String>>? {
        val list = ArrayList<Map<String, String>>()

        val mapAppVersion = hashMapOf<String, String>()
        mapAppVersion["name"] = getString(R.string.about_app_version)
        mapAppVersion["description"] = packageManager.getPackageInfo(packageName, 0).versionName
        list.add(mapAppVersion)

        val mapDevelopedBy = hashMapOf<String, String>()
        mapDevelopedBy["name"] = getString(R.string.about_developed_by)
        mapDevelopedBy["description"] = "github.com/paulofernando/pathfinding"
        list.add(mapDevelopedBy)

        return list
    }

    fun openDrawableTutorial(view: View) {
        startActivity(Intent(this, IntroDrawableGraphActivity::class.java))
    }

    fun openGridTutorial(view: View) {
        startActivity(Intent(this, IntroGridGraphActivity::class.java))
    }

}