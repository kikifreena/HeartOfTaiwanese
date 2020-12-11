package edu.mills.heartoftaiwanese.data

import edu.mills.heartoftaiwanese.network.WebResultCode

data class ChineseResult(val resultCode: WebResultCode, val chinese: String? = null) {
    val isTaiwanese = false
    val isChinese = true
}