package edu.mills.heartoftaiwanese.activity.recent

import android.content.Context
import edu.mills.heartoftaiwanese.activity.BaseFragment
import edu.mills.heartoftaiwanese.activity.BaseViewModel
import edu.mills.heartoftaiwanese.data.DatabaseWord
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecentViewModel : RecentContract.RecentViewModel, BaseViewModel() {
    private lateinit var view: RecentContract.RecentView

    override fun configure(view: BaseFragment, context: Context) {
        super.configure(view, context)
        this.view = view as RecentContract.RecentView
        this.view.onConfigurationSuccess()
    }

    /**
     * Favorite a word, or unfavorite it if it's already favorited
     */
    override fun favoriteWord(word: DatabaseWord) {
        TODO()
    }

    override fun getUpdatedWordList(): Boolean {
        var returnValue = false
        GlobalScope.launch {
            val recentList = repository.getRecent()
            returnValue = recentList.isNotEmpty()
            view.onWordListChanged(recentList)
        }
        return returnValue
    }
}
