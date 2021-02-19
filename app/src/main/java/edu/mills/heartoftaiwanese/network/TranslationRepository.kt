package edu.mills.heartoftaiwanese.network

import android.util.Log
import edu.mills.heartoftaiwanese.data.ChineseResult
import edu.mills.heartoftaiwanese.data.TaiwaneseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * A class for making network calls only.
 */
class TranslationRepository {
    companion object {
        private const val URL_TO_CRAWL_TW = "http://210.240.194.97/q/THq.asp?w="
        private val digitsRegex by lazy {
            Regex("""\d+""")
        }
    }

    private val cachedTranslationsToChinese = mutableMapOf<String, ChineseResult>()
    private val cachedTranslationsToTaiwanese = mutableMapOf<String, TaiwaneseResult>()

    suspend fun getTaiwanese(chinese: String): TaiwaneseResult {
        return withContext(Dispatchers.IO) {
            val possibleTaiwaneseResult: TaiwaneseResult? = cachedTranslationsToTaiwanese[chinese]
            // Get from the cache if possible...
            if (!cachedTranslationsToTaiwanese.containsKey(chinese) ||
                possibleTaiwaneseResult == null ||
                possibleTaiwaneseResult.resultCode != WebResultCode.RESULT_OK
            ) {
                val url = URL("${URL_TO_CRAWL_TW}$chinese")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Accept-Charset", "UTF-8")
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    TaiwaneseResult(
                        WebResultCode.RESULT_OK,
                        connection.inputStream.bufferedReader()
                            // Parse the text after reading it
                            .use { parse(it.readText()) }
                    )
                        .apply {
                            // Store the cached translation in the short term memory to avoid another network call
                            cachedTranslationsToTaiwanese[chinese] = this
                        }
                } else {
                    TaiwaneseResult(WebResultCode.INVALID_NOT_FOUND)
                }
            } else {
                possibleTaiwaneseResult
            }
        }
    }

    suspend fun getChinese(english: String): ChineseResult {
        return withContext(Dispatchers.IO) {
            val possibleChineseResult = cachedTranslationsToChinese[english]
            // Get from the cache if possible...
            if (!cachedTranslationsToChinese.containsKey(english) ||
                possibleChineseResult == null ||
                possibleChineseResult.resultCode != WebResultCode.RESULT_OK
            ) {
                Log.d("MainActivity", "Fetching Chinese for $english")
                translateTextToChinese(english)?.let {
                    ChineseResult(WebResultCode.RESULT_OK, it).apply {
                        cachedTranslationsToChinese[english] = this
                    }
                } ?: ChineseResult(WebResultCode.UNKNOWN_ERROR)
            } else {
                possibleChineseResult
            }
        }
    }

    @Throws(IOException::class)
    private suspend fun translateTextToChinese(text: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                TranslateApiBuilder().getTranslation(text)
            } catch (exception: Exception) {
                null // Just let it be null... it is caught later anyway
            }
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
