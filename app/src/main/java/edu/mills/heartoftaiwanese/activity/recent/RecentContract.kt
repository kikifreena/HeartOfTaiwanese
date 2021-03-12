package edu.mills.heartoftaiwanese.activity.recent

import edu.mills.heartoftaiwanese.data.DatabaseWord

interface RecentContract {
    interface RecentView {
        fun onConfigurationSuccess()
        fun onWordListChanged(newWordList: List<DatabaseWord>)
    }

    interface RecentViewModel {
        fun favoriteWord(word: DatabaseWord)

        /**
         * Get the updated word list. Return true if there are more than 0 words, false if there are none.
         */
        fun getUpdatedWordList(): Boolean
    }
}