package edu.mills.heartoftaiwanese.activity.favorites

import edu.mills.heartoftaiwanese.data.DatabaseWord

interface FavoritesContract {
    interface FavoritesView {
        fun onConfigurationSuccess()
        fun onWordListChanged(newWordList: List<DatabaseWord>)
    }

    interface FavoritesViewModel {
        fun favoriteWord(word: DatabaseWord)

        /**
         * Get the updated word list. Return true if there are more than 0 words, false if there are none.
         */
        fun getUpdatedWordList(): Boolean
    }
}