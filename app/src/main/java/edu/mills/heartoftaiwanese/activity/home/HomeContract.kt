package edu.mills.heartoftaiwanese.activity.home

interface HomeContract {
    interface HomeView {
        fun onChineseFetched()
        fun onTaiwaneseFetched()
    }

    interface HomeViewModel {
        fun configure(view: HomeView)
        fun fetchChinese()
        fun fetchTaiwanese()
    }
}