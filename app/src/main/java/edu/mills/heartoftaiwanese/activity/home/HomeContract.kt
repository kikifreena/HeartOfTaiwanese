package edu.mills.heartoftaiwanese.activity.home

import edu.mills.heartoftaiwanese.network.WebResultCodes

interface HomeContract {
    interface HomeView {
        fun onChineseFetched(chinese: String)
        fun onTaiwaneseFetched(taiwanese: String)
        fun onNetworkError(error: WebResultCodes)
    }

    interface HomeViewModel {
        fun configure(view: HomeView)
        fun fetchChinese(english: String)
        fun fetchTaiwanese(chinese: String)
    }
}