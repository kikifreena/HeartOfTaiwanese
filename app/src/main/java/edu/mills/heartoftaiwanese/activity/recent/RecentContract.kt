package edu.mills.heartoftaiwanese.activity.recent

import android.content.Context
import edu.mills.heartoftaiwanese.data.DatabaseWord

interface RecentContract {
    interface RecentView {
        fun onConfigurationSuccess()
        fun onWordListChanged(newWordList: List<DatabaseWord>)
    }

    interface RecentViewModel {
        fun configure(view: RecentView, context: Context)
        fun favoriteWord(word: DatabaseWord)
        fun getUpdatedWordList(): Boolean
    }
}