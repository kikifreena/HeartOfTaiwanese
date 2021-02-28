package edu.mills.heartoftaiwanese.activity.home

import android.content.Context
import edu.mills.heartoftaiwanese.network.WebResultCode
import edu.mills.heartoftaiwanese.repository.ChineseToTaiwaneseHelper
import edu.mills.heartoftaiwanese.repository.EnglishToChineseHelper
import edu.mills.heartoftaiwanese.repository.TranslationRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeViewModel : HomeContract.HomeViewModel {
    private lateinit var view: HomeContract.HomeView
    private lateinit var repository: TranslationRepository
    private var isConfigured = false

    override fun configure(view: HomeContract.HomeView, context: Context) {
        if (!isConfigured) {
            this.view = view
            repository = TranslationRepository(context)
            isConfigured = true
        }
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
