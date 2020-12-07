package edu.mills.heartoftaiwanese.data

import edu.mills.heartoftaiwanese.network.WebResultCode

data class ChineseResult(val result: WebResultCode, val chinese: String? = null) {
    val isTaiwanese = false
    val isChinese = true
}