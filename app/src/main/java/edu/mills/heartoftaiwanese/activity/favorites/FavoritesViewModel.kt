package edu.mills.heartoftaiwanese.activity.favorites

import android.content.Context
import edu.mills.heartoftaiwanese.activity.BaseFragment
import edu.mills.heartoftaiwanese.activity.BaseViewModel
import edu.mills.heartoftaiwanese.data.DatabaseWord
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavoritesViewModel : BaseViewModel(), FavoritesContract.FavoritesViewModel {
    private lateinit var view: FavoritesContract.FavoritesView

    override fun configure(view: BaseFragment, context: Context) {
        super.configure(view, context)
        this.view = view as FavoritesFragment
        this.view.onConfigurationSuccess()
    }

    override fun favoriteWord(word: DatabaseWord) {
        GlobalScope.launch {
            repository.favorite(word.id, !word.favorite)
            updateWordList()
        }
    }

    /**
     * Get the updated word list. Return true if there are more than 0 words, false if there are none.
     */
    override fun updateWordList(): Boolean {
        val favoritesList = super.getUpdatedListForType(ListType.FAVORITES)
        view.onWordListChanged(favoritesList)
        return favoritesList.isNotEmpty()
    }
}
