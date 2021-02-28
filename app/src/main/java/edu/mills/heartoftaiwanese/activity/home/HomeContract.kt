package edu.mills.heartoftaiwanese.activity.home

import android.content.Context
import edu.mills.heartoftaiwanese.network.WebResultCode

interface HomeContract {
    interface HomeView {
        fun onChineseFetched(chinese: String)
        fun onTaiwaneseFetched(taiwanese: String)
        fun onNetworkError(error: WebResultCode)
    }

    interface HomeViewModel {
        fun configure(view: HomeView, context: Context)
        fun fetchChinese(english: String)
        fun fetchTaiwanese(chinese: String)
    }
}
