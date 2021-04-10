package edu.mills.heartoftaiwanese.activity

import android.content.Context
import edu.mills.heartoftaiwanese.data.DatabaseWord
import edu.mills.heartoftaiwanese.repository.TranslationRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class BaseViewModel {
    internal lateinit var repository: TranslationRepository

    open fun configure(view: BaseFragment, context: Context) {
        if (!this::repository.isInitialized) {
            repository = TranslationRepository(context)
        }
    }

    enum class ListType {
        RECENTS,
        FAVORITES
    }

    fun getUpdatedListForType(listType: ListType): List<DatabaseWord> {
        val resultList = mutableListOf<DatabaseWord>()
        GlobalScope.launch {
            when (listType) {
                ListType.RECENTS -> repository.getRecent()
                ListType.FAVORITES -> repository.getFavorites()
            }
        }
        return resultList

    }
}
