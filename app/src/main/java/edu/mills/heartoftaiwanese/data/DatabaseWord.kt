package edu.mills.heartoftaiwanese.data

import edu.mills.heartoftaiwanese.network.WebResultCode
import java.util.Date

/**
 * @property word
 * @property id
 * @property favorite
 * @property accessTime
 */
data class DatabaseWord(
    val word: Word,
    val id: Int,
    var favorite: Boolean = false,
    var accessTime: Date
) {
    fun toTaiwaneseResult(): TaiwaneseResult = if (word.taiwanese == null) {
        TaiwaneseResult(WebResultCode.INVALID_NOT_FOUND)
    } else {
        TaiwaneseResult(WebResultCode.RESULT_OK, word.taiwanese)
    }

    fun toChineseResult(): ChineseResult = if (word.taiwanese == null) {
        ChineseResult(WebResultCode.INVALID_NOT_FOUND)
    } else {
        ChineseResult(WebResultCode.RESULT_OK, word.chinese)
    }
}
