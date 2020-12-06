package edu.mills.heartoftaiwanese.network

import android.util.Log
import edu.mills.heartoftaiwanese.data.ChineseResult
import edu.mills.heartoftaiwanese.data.TaiwaneseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class TranslationRepository private constructor() {
    companion object {
        private const val URL_TO_CRAWL_TW = "http://210.240.194.97/q/THq.asp?w="
        private const val URL_TO_CRAWL_ENCH =
            "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=en"
    }

    suspend fun getTaiwanese(chinese: String): TaiwaneseResult {
        return withContext(Dispatchers.IO) {
            val url = URL("${URL_TO_CRAWL_TW}$chinese")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Accept-Charset", "UTF-8")
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                TaiwaneseResult(WebResultCodes.RESULT_OK, connection.inputStream.bufferedReader()
                    .use { it.readText() })
            } else {
                TaiwaneseResult(WebResultCodes.INVALID_NOT_FOUND)
            }
        }
    }

    suspend fun getChinese(english: String): ChineseResult {
        return withContext(Dispatchers.IO) {
            Log.d("MainActivity", "Fetching Chinese for $english")
            val connection =
                URL("${URL_TO_CRAWL_ENCH}&dt=t&q=$english").openConnection() as HttpURLConnection
            // https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=zh&dt=t&q=hello
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept-Charset", "UTF-8")
            when (connection.responseCode) {
                HttpURLConnection.HTTP_OK -> {
                    val inputAsString: String =
                        connection.inputStream.bufferedReader().use { it.readText() }
                    val start = inputAsString.indexOf('"')
                    val stop = inputAsString.indexOf('"', start + 1)
                    ChineseResult(
                        WebResultCodes.RESULT_OK,
                        inputAsString.substring(start + 1, stop)
                    )
                }
                429 -> {
                    Log.e("TranslationRepository", "too many HTTP requests")
                    ChineseResult(WebResultCodes.RATE_LIMITED)
                }
                else -> {
                    Log.e("TranslationRepository", connection.responseMessage)
                    ChineseResult(WebResultCodes.UNKNOWN_ERROR)
                }
            }
        }
    }
}
