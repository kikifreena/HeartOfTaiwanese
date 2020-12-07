package edu.mills.heartoftaiwanese.network

import android.util.Log
import java.net.HttpURLConnection
import java.net.URL

class EnglishToChineseHelper {
    companion object {
        private const val URL_TO_CRAWL_ENCH =
            "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=en"
    }

    fun fetchChinese(english: String): String {
        Log.d("MainActivity", "Fetching Chinese for $english")
        val url = URL("${URL_TO_CRAWL_ENCH}&dt=t&q=$english")
        // https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=zh&dt=t&q=hello
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept-Charset", "UTF-8")
        Log.d("MainActivity", connection.responseCode.toString())
        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val inputAsString = connection.inputStream.bufferedReader().use { it.readText() }
            val start = inputAsString.indexOf('"')
            val stop = inputAsString.indexOf('"', start + 1)
            return inputAsString.substring(start + 1, stop)
        } else if (connection.responseCode == 429) {
            Log.d("MainActivity", "too many HTTP requests")
            return WebResultCode.RATE_LIMITED.toString()
        } else {
            Log.i("MainActivity", connection.responseMessage)
            return WebResultCode.UNKNOWN_ERROR.toString()
        }
    }
}