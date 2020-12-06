package edu.mills.heartoftaiwanese.data

import edu.mills.heartoftaiwanese.network.WebResultCodes

data class TaiwaneseResult(val result: WebResultCodes, val taiwanese: String? = null) {
    val isTaiwanese = true
    val isChinese = false
}