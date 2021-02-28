package edu.mills.heartoftaiwanese.network

import edu.mills.heartoftaiwanese.data.TaiwaneseResult
import java.net.HttpURLConnection
import java.net.URL

/**
 * Class for scraping the website in order to obtain Taiwanese results.
 */
class TaiwaneseApi {
    companion object {
        private const val URL_TO_CRAWL_TW = "http://210.240.194.97/q/THq.asp?w="
        private val digitsRegex by lazy {
            Regex("""\d+""")
        }
    }

    fun getTaiwanese(chinese: String): TaiwaneseResult {
        val url = URL("${URL_TO_CRAWL_TW}$chinese")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Accept-Charset", "UTF-8")
        return if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            TaiwaneseResult(
                WebResultCode.RESULT_OK,
                connection.inputStream.bufferedReader()
                    // Parse the text after reading it
                    .use { parse(it.readText()) }
            )
        } else {
            TaiwaneseResult(WebResultCode.INVALID_NOT_FOUND)
        }
    }

    /**
     * Do NOT modify this function unless you know what you're doing (I don't).
     * Used for parsing the response when getting Taiwanese.
     * @see convertToString
     *
     * TODO this code is unreadable, fix it
     */
    private fun parse(data: String): String {
        val loc = data.indexOf("台語羅馬字")
        val loc2 = 1 + data.indexOf(">", data.indexOf("<span", loc))
        val loc3 = data.indexOf("</span>", loc2)
        return convertToString(data.substring(loc2, loc3))
    }

    /**
     * Do NOT modify this function unless you know what you're doing (I don't).
     *
     * The Taiwanese gets returned as some hexadecimal string-like combination that is not human readable.
     * This function turns the string back into a human-readable string.
     *
     * @param hex the hexadecimal string representation to be converted
     */
    private fun convertToString(hex: String): String {
        val split = hex.split("&#|;".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return StringBuilder().apply {
            for (curr in split) {
                if (curr.matches(digitsRegex)) {
                    // Get the int value of the hex, then convert it to string
                    append(Integer.parseInt(curr).toChar())
                } else {
                    append(curr)
                }
            }
        }.toString()
    }
}
