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
    private val digitsRegex by lazy {
        Regex("""\d+""")
    }

    suspend fun getTaiwanese(chinese: String): TaiwaneseResult {
        val taiwaneseResult: TaiwaneseResult = repository.getTaiwanese(chinese)
        return when (taiwaneseResult.resultCode) {
            WebResultCode.RESULT_OK -> taiwaneseResult.taiwanese?.let {
                try {
                    TaiwaneseResult(WebResultCode.RESULT_OK, parse(it))
                } catch (exception: StringIndexOutOfBoundsException) {
                    exception.printStackTrace()
                    TaiwaneseResult(WebResultCode.UNKNOWN_ERROR, "")
                }
            } ?: TaiwaneseResult(WebResultCode.INVALID_NOT_FOUND, "")
            else -> taiwaneseResult
        }
    }

    private fun parse(data: String): String {
        val loc = data.indexOf("台語羅馬字")
        val loc2 = 1 + data.indexOf(">", data.indexOf("<span", loc))
        val loc3 = data.indexOf("</span>", loc2)
        return convertToString(data.substring(loc2, loc3))
    }

    private fun convertToString(hex: String): String {
        val split = hex.split("&#|;".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val sb = StringBuilder()
        for (curr in split) {
            if (curr.matches(digitsRegex)) {
                sb.append(Integer.parseInt(curr).toChar())
            } else {
                sb.append(curr)
            }
        }
        return sb.toString()
    }
}