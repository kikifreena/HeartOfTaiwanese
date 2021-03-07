package edu.mills.heartoftaiwanese.activity.recent

import android.content.Context
import edu.mills.heartoftaiwanese.data.DatabaseWord

class RecentViewModel : RecentContract.RecentViewModel {
    private lateinit var view: RecentContract.RecentView

    override fun configure(view: RecentContract.RecentView, context: Context) {
        this.view = view
        getUpdatedWordList()
        TODO("Not yet implemented")
    }

    /**
     * Favorite a word, or unfavorite it if it's already favorited
     */
    override fun favoriteWord(word: DatabaseWord) {
        TODO("Not yet implemented")
    }

    override fun getUpdatedWordList(): Boolean {
        view.onWordListChanged(listOf())
        TODO("Not yet implemented")
    }
}