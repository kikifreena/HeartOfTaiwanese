package edu.mills.heartoftaiwanese.activity.home

import androidx.lifecycle.ViewModel

class HomeViewModel : HomeContract.HomeViewModel, ViewModel() {
    private lateinit var view: HomeContract.HomeView

    override fun configure(view: HomeContract.HomeView) {
        this.view = view
    }

    override fun fetchChinese() {
        TODO("Not yet implemented")
    }

    override fun fetchTaiwanese() {
        TODO("Not yet implemented")
    }

}