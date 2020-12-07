package edu.mills.heartoftaiwanese.activity.home

import edu.mills.heartoftaiwanese.network.ChineseToTaiwaneseHelper
import edu.mills.heartoftaiwanese.network.TranslationRepository
import edu.mills.heartoftaiwanese.network.WebResultCode
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeViewModel : HomeContract.HomeViewModel {
    private lateinit var view: HomeContract.HomeView
    private lateinit var repository: TranslationRepository

    override fun configure(view: HomeContract.HomeView) {
        this.view = view
        repository = TranslationRepository()
    }

    override fun fetchChinese(english: String) {
        // ToDo: Call the Helper class similar to below
        view.onChineseFetched("河馬")
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