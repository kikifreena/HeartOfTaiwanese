package edu.mills.heartoftaiwanese.activity.home

import android.content.Context
import edu.mills.heartoftaiwanese.activity.BaseFragment
import edu.mills.heartoftaiwanese.activity.BaseViewModel
import edu.mills.heartoftaiwanese.network.WebResultCode
import edu.mills.heartoftaiwanese.repository.ChineseToTaiwaneseHelper
import edu.mills.heartoftaiwanese.repository.EnglishToChineseHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeViewModel : HomeContract.HomeViewModel, BaseViewModel() {
    private lateinit var view: HomeContract.HomeView

    override fun configure(view: BaseFragment, context: Context) {
        super.configure(view, context)
        this.view = view as HomeContract.HomeView
    }

    override fun fetchChinese(english: String) {
        GlobalScope.launch {
            val result = EnglishToChineseHelper(repository).getChinese(english)
            if (result.isTaiwanese) throw IllegalStateException("Should get Chinese, but found Taiwanese!")
            when (result.resultCode) {
                WebResultCode.RESULT_OK -> result.chinese?.let { view.onChineseFetched(it) }
                else -> view.onNetworkError(result.resultCode)
            }
        }
    }

    override fun fetchTaiwanese(chinese: String) {
        GlobalScope.launch {
            val result = ChineseToTaiwaneseHelper(repository).getTaiwanese(chinese)
            if (result.isChinese) throw IllegalStateException("Should get Taiwanese, but found Chinese!")
            when (result.resultCode) {
                WebResultCode.RESULT_OK -> result.taiwanese?.let { view.onTaiwaneseFetched(it) }
                    ?: view.onNetworkError(WebResultCode.INVALID_NOT_FOUND)
                else -> view.onNetworkError(result.resultCode)
            }
        }
    }
}
