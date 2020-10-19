package edu.mills.heartoftaiwanese

import android.os.AsyncTask
import android.util.Log
import edu.mills.heartoftaiwanese.data.Language
import edu.mills.heartoftaiwanese.data.LanguageContainer
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class WordRetriever(input: String?) {
    private var chinese: String? = null
    var taiwanese = ""
        private set

    companion object {
        private const val URL_TO_CRAWL_TW = "http://210.240.194.97/q/THq.asp?w="
        private const val URL_TO_CRAWL_ENCH =
            "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=en"
        private const val INVALID_MESSAGE = "Not found"
        const val kUnknownError = "UNKNOWN ERROR"
        const val kRateLimited = "RATE LIMITED"
        private const val kResultOk = "SUCCESS"
    }

    init {
        chinese = input
    }

    fun run(): Boolean {
        try {
            crawlSite()
            return taiwanese != INVALID_MESSAGE
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    private fun parse(data: String): String {
        try {
            val loc = data.indexOf("台語羅馬字")
            val loc2 = 1 + data.indexOf(">", data.indexOf("<span", loc))
            val loc3 = data.indexOf("</span>", loc2)
            return convertToString(data.substring(loc2, loc3))
        } catch (e: StringIndexOutOfBoundsException) {
            return INVALID_MESSAGE
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

    @Throws(IOException::class)
    private fun crawlSite() {
        val url = URL("$URL_TO_CRAWL_TW$chinese")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Accept-Charset", "UTF-8")
        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val inputAsString = connection.inputStream.bufferedReader().use { it.readText() }
            taiwanese = parse(inputAsString)
        }
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
            return kRateLimited
        } else {
            Log.i("MainActivity", connection.responseMessage)
            return kUnknownError
        }
    }


    inner class ParseWordTask : AsyncTask<LanguageContainer, Int, String>() {
        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to [.execute]
         * by the caller of this task.
         *
         *
         * This method can call [.publishProgress] to publish updates
         * on the UI thread.
         *
         * @see .onPreExecute
         * @see .onPostExecute
         *
         * @see .publishProgress
         */
        private lateinit var w: WordRetriever

        override fun doInBackground(vararg lang: LanguageContainer): String? {
            if (lang[0].language == Language.LANGUAGE_ENGLISH) {
                val chineseString = w.fetchChinese(lang[0].text)
                if (chineseString == kUnknownError) {
                    return kUnknownError
                } else if (chineseString == kRateLimited) {
                    return kRateLimited
                } else {
                    w = WordRetriever(chineseString)
                }
            } else {
                w = WordRetriever(lang[0].text)
            }
            val status = w.run()
            if (!status) {
                return kUnknownError
            } else {
                return kResultOk
            }
        }

    }
}
