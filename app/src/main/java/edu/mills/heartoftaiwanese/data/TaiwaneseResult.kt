package edu.mills.heartoftaiwanese.data

import edu.mills.heartoftaiwanese.network.WebResultCode

data class TaiwaneseResult(val resultCode: WebResultCode, val taiwanese: String? = null) {
    val isTaiwanese = true
    val isChinese = false
}