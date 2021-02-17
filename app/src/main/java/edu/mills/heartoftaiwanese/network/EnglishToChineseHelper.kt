package edu.mills.heartoftaiwanese.network

import edu.mills.heartoftaiwanese.data.ChineseResult

class EnglishToChineseHelper(
    private val repository: TranslationRepository
) {
    suspend fun getChinese(english: String): ChineseResult {
        val chineseResult = repository.getChinese(english)
        return when (chineseResult.resultCode) {
            WebResultCode.RESULT_OK -> chineseResult.chinese?.let {
                ChineseResult(WebResultCode.RESULT_OK, it)
            } ?: ChineseResult(WebResultCode.UNKNOWN_ERROR)
            else -> chineseResult
        }
    }
}