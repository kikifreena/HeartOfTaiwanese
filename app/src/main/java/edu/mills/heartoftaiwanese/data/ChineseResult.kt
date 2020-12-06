package edu.mills.heartoftaiwanese.data

import edu.mills.heartoftaiwanese.network.WebResultCodes

data class ChineseResult(val result: WebResultCodes, val chinese: String? = null) {
    val isTaiwanese = false
    val isChinese = true
}