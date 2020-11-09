package edu.mills.heartoftaiwanese.network

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ChineseToTaiwaneseHelper {
    companion object {
        private const val URL_TO_CRAWL_TW = "http://210.240.194.97/q/THq.asp?w="
    }

    @Throws(IOException::class)
    fun crawlSite(chinese: String): String {
        val url = URL("$URL_TO_CRAWL_TW$chinese")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Accept-Charset", "UTF-8")
        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val inputAsString = connection.inputStream.bufferedReader().use { it.readText() }
            return parse(inputAsString)
        } else {
            return WebResultCodes.INVALID_NOT_FOUND.toString()
        }
    }

    private fun parse(data: String): String {
        try {
            val loc = data.indexOf("台語羅馬字")
            val loc2 = 1 + data.indexOf(">", data.indexOf("<span", loc))
            val loc3 = data.indexOf("</span>", loc2)
            return convertToString(data.substring(loc2, loc3))
        } catch (e: StringIndexOutOfBoundsException) {
            return WebResultCodes.INVALID_NOT_FOUND.toString()
        }
    }

    private fun convertToString(hex: String): String {
        val split = hex.split("&#|;".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val sb = StringBuilder()
        for (curr in split) {
            val DIGITS_REGEX = Regex("""\d+""")
            if (curr.matches(DIGITS_REGEX)) {
                sb.append(Integer.parseInt(curr).toChar())
            } else {
                sb.append(curr)
            }
        }
        return sb.toString()
    }

}