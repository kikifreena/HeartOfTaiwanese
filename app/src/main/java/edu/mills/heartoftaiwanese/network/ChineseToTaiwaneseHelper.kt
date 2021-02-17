package edu.mills.heartoftaiwanese.network

import androidx.lifecycle.ViewModel
import edu.mills.heartoftaiwanese.data.TaiwaneseResult

/**
 * Class for requesting network calls and parsing the result.
 * This class does not make network calls. It calls [TranslationRepository] to make the calls.
 * Then it will parse them and send them to the [ViewModel].
 */
class ChineseToTaiwaneseHelper(
    private val repository: TranslationRepository
) {
    suspend fun getTaiwanese(chinese: String): TaiwaneseResult {
        val taiwaneseResult: TaiwaneseResult = repository.getTaiwanese(chinese)
        return when (taiwaneseResult.resultCode) {
            WebResultCode.RESULT_OK -> taiwaneseResult.taiwanese?.let {
                try {
                    TaiwaneseResult(WebResultCode.RESULT_OK, it)
                } catch (exception: StringIndexOutOfBoundsException) {
                    exception.printStackTrace()
                    TaiwaneseResult(WebResultCode.UNKNOWN_ERROR, "")
                }
            } ?: TaiwaneseResult(WebResultCode.INVALID_NOT_FOUND, "")
            else -> taiwaneseResult
        }
    }
}