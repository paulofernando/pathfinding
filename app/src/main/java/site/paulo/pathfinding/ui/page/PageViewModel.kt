package site.paulo.pathfinding.ui.page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = Transformations.map(_index) {
        "He\n\n\n\n\n\n\n\n\nllo world from section: $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}