package edu.mills.heartoftaiwanese.activity

import android.content.Context
import edu.mills.heartoftaiwanese.repository.TranslationRepository

open class BaseViewModel {
    internal lateinit var repository: TranslationRepository

    open fun configure(view: BaseFragment, context: Context) {
        if (!this::repository.isInitialized) {
            repository = TranslationRepository(context)
        }
    }
}