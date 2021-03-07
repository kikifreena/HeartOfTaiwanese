package edu.mills.heartoftaiwanese.activity.recent

import android.content.Context
import edu.mills.heartoftaiwanese.data.DatabaseWord
import edu.mills.heartoftaiwanese.data.Word
import java.util.Date

class RecentViewModel : RecentContract.RecentViewModel {
    private lateinit var view: RecentContract.RecentView

    override fun configure(view: RecentContract.RecentView, context: Context) {
        this.view = view
        view.onConfigurationSuccess()
    }

    /**
     * Favorite a word, or unfavorite it if it's already favorited
     */
    override fun favoriteWord(word: DatabaseWord) {

    }

    override fun getUpdatedWordList(): Boolean {
        val testList = listOf(
            DatabaseWord(Word("aa", "bb", "FAKE data"), 1, false, Date()),
            DatabaseWord(Word("a", "b", "Fake fake fake"), 2, false, Date())
        )
        view.onWordListChanged(testList)
        return true
    }
}