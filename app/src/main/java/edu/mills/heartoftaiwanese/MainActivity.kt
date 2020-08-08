package edu.mills.heartoftaiwanese

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        submit_ch.setOnClickListener {
            tw_result.visibility = View.GONE
            result.visibility = View.GONE
            ParseWordTask().execute(LanguageContainer(editText
                    .text.toString(), LANGUAGE_CHINESE)
            )
        }
        submit_en.setOnClickListener {
            findViewById<View>(R.id.tw_result).visibility = View.GONE
            findViewById<View>(R.id.result).visibility = View.GONE
            ParseWordTask().execute(LanguageContainer(editTextEng
                    .text.toString(), LANGUAGE_ENGLISH)
            )
        }
        val clearButton = findViewById<Button>(R.id.clear)
        clearButton.setOnClickListener {
            editText.setText("")
            editTextEng.setText("")
            findViewById<View>(R.id.tw_result).visibility = View.GONE
            findViewById<View>(R.id.result).visibility = View.GONE
        }
    }

    private data class LanguageContainer internal constructor(internal val text: String, internal val language: Int)

    private inner class ParseWordTask : AsyncTask<LanguageContainer, Int, Boolean>() {
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
        private lateinit var w: Word

        override fun doInBackground(vararg lang: LanguageContainer): Boolean? {
            if (lang[0].language == LANGUAGE_ENGLISH){
                val result = fetchChinese(lang[0].text)
                w = Word(result)
            }
            else {
                w = Word(lang[0].text)
            }
            return w.run()
        }

        override fun onPostExecute(status: Boolean) {
            if (status) {
                Log.d("MainActivity-Taiwanese", w.toString())
                tw_result.visibility = View.VISIBLE
                result.visibility = View.VISIBLE
                result.text = w.taiwanese
            } else {
                Toast.makeText(this@MainActivity,
                    this@MainActivity.getText(R.string.error).toString(), Toast.LENGTH_LONG).show()
            }
            super.onPostExecute(status)
        }
    }

    companion object {
        const val LANGUAGE_ENGLISH = 1
        const val LANGUAGE_CHINESE = 0
        private const val URL_TO_CRAWL_ENCH = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=";
    }

    protected fun fetchChinese(english: String): String? {
        val url = URL("${URL_TO_CRAWL_ENCH}zh&tl=en&dt=t&q=$english")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Accept-Charset", "UTF-8")
        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val inputAsString = connection.inputStream.bufferedReader().use { it.readText() }
            val start = inputAsString.indexOf('"')
            val stop = inputAsString.indexOf('"', start)
            return inputAsString.substring(start + 1, stop)
        }
        return null
    }
}
