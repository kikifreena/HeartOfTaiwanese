package edu.mills.heartoftaiwanese.activity.recent

import android.content.Context
import android.provider.ContactsContract
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
        GlobalScope.launch {
            repository.favorite(word.id, !word.favorite)
            word.favorite = !word.favorite
        }
    }

    override fun updateWordList(): Boolean {
        val favoritesList = super.getUpdatedListForType(ListType.FAVORITES)
        view.onWordListChanged(favoritesList)
        return favoritesList.isNotEmpty()
    }
}
