package edu.mills.heartoftaiwanese.network

import android.os.AsyncTask
import edu.mills.heartoftaiwanese.data.Language
import edu.mills.heartoftaiwanese.data.LanguageContainer
import java.io.IOException

class WordRetriever(input: String?) {
    private var chinese: String? = null
    var taiwanese = ""
        private set

    init {
        chinese = input
    }

    fun run(): Boolean {
        return try {
            chinese?.let {
                taiwanese = ChineseToTaiwaneseHelper().crawlSite(it)
                taiwanese != WebResultCodes.INVALID_NOT_FOUND.toString()
            } ?: false
        } catch (e: IOException) {
            e.printStackTrace()
            false
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
        // TODO: convert to kotlin coroutine
        private lateinit var w: WordRetriever

        override fun doInBackground(vararg lang: LanguageContainer): String? {
            if (lang[0].language == Language.LANGUAGE_ENGLISH) {
                when (val chineseString = EnglishToChineseHelper().fetchChinese(lang[0].text)) {
                    WebResultCodes.UNKNOWN_ERROR.toString() -> {
                        return WebResultCodes.UNKNOWN_ERROR.toString()
                    }
                    WebResultCodes.RATE_LIMITED.toString() -> {
                        return WebResultCodes.RATE_LIMITED.toString()
                    }
                    else -> {
                        w = WordRetriever(chineseString)
                    }
                }
            } else {
                w = WordRetriever(lang[0].text)
            }
            val status = w.run()
            return if (!status) {
                WebResultCodes.UNKNOWN_ERROR.toString()
            } else {
                WebResultCodes.RESULT_OK.toString()
            }
        }
    }
}
