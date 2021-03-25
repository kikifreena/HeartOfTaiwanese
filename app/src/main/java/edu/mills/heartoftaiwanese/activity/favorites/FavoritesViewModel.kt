package edu.mills.heartoftaiwanese.activity.favorites

import android.content.Context
import edu.mills.heartoftaiwanese.activity.BaseFragment
import edu.mills.heartoftaiwanese.activity.BaseViewModel
import edu.mills.heartoftaiwanese.data.DatabaseWord
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavoritesViewModel : BaseViewModel(), FavoritesContract.FavoritesViewModel {
    private lateinit var view: FavoritesFragment
    override fun favoriteWord(word: DatabaseWord) {
        GlobalScope.launch {
            repository.favorite(word.id, !word.favorite)
            word.favorite = !word.favorite
        }
    }

    /**
     * Get the updated word list. Return true if there are more than 0 words, false if there are none.
     */
    override fun getUpdatedWordList(): Boolean {
        var returnValue = false
        GlobalScope.launch {
            val favoritesList = repository.getFavorites()
            returnValue = favoritesList.isNotEmpty()
            view.onWordListChanged(favoritesList)
        }
        return returnValue
    }

    override fun configure(view: BaseFragment, context: Context) {
        this.view = view as FavoritesFragment
        super.configure(view, context)
    }
}
