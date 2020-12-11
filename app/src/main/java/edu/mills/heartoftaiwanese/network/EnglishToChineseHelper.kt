package edu.mills.heartoftaiwanese.network

import edu.mills.heartoftaiwanese.data.ChineseResult

class EnglishToChineseHelper(
    private val repository: TranslationRepository
) {
    suspend fun getChinese(english: String): ChineseResult {
        val chineseResult = repository.getChinese(english)
        return when (chineseResult.resultCode) {
            WebResultCode.RESULT_OK -> chineseResult.chinese?.let {
                ChineseResult(WebResultCode.RESULT_OK, parse(it))
            } ?: ChineseResult(WebResultCode.UNKNOWN_ERROR)
            else -> chineseResult
        }
    }

    private fun parse(inputAsString: String): String {
        val start = inputAsString.indexOf('"')
        val stop = inputAsString.indexOf('"', start + 1)
        return inputAsString.substring(start + 1, stop)
    }
}