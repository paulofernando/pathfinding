package site.paulo.pathfinding.ui

import android.os.Bundle
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_about.*
import site.paulo.pathfinding.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setSupportActionBar(aboutToolbar)
        populateAboutList()
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun populateAboutList() {
        val list = buildItems()
        val from = arrayOf("name", "description")
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)

        aboutListView.adapter = SimpleAdapter(this, list,
            android.R.layout.simple_list_item_2, from, to)
    }

    private fun buildItems(): ArrayList<Map<String, String>>? {
        val list = ArrayList<Map<String, String>>()

        val mapAppVersion = hashMapOf<String, String>()
        mapAppVersion["name"] = "App version"
        mapAppVersion["description"] = packageManager.getPackageInfo(packageName, 0).versionName
        list.add(mapAppVersion)

        val mapDevelopedBy = hashMapOf<String, String>()
        mapDevelopedBy["name"] = "Developed by"
        mapDevelopedBy["description"] = "Paulo Fernando - github.com/paulofernando/pathfinding"
        list.add(mapDevelopedBy)

        return list
    }

}