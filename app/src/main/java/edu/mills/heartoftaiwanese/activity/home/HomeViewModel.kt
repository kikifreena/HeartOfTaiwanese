package edu.mills.heartoftaiwanese.activity.home

import androidx.lifecycle.ViewModel
import edu.mills.heartoftaiwanese.network.TranslationRepository

class HomeViewModel : HomeContract.HomeViewModel, ViewModel() {
    private lateinit var view: HomeContract.HomeView
    private lateinit var repository: TranslationRepository

    override fun configure(view: HomeContract.HomeView) {
        this.view = view
        repository = TranslationRepository()
    }

    override fun fetchChinese(english: String) {
        view.onChineseFetched("")
    }

    override fun fetchTaiwanese(chinese: String) {
        view.onTaiwaneseFetched("")
    }

}