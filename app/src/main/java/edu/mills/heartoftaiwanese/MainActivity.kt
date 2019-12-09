package edu.mills.heartoftaiwanese

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chineseEditText = findViewById<EditText>(R.id.editText)
        println(chineseEditText.text)
        val englishEditText = findViewById<EditText>(R.id.editTextEng)
        val b = findViewById<Button>(R.id.submit_ch)
        b.setOnClickListener {
            findViewById<View>(R.id.tw_result).visibility = View.GONE
            findViewById<View>(R.id.result).visibility = View.GONE
            ParseWordTask().execute(LanguageContainer(chineseEditText
                    .text.toString(), LANGUAGE_CHINESE)
            )
        }
        val b2 = findViewById<Button>(R.id.submit_en)
        b2.setOnClickListener {
            findViewById<View>(R.id.tw_result).visibility = View.GONE
            findViewById<View>(R.id.result).visibility = View.GONE
            ParseWordTask().execute(LanguageContainer(englishEditText
                    .text.toString(), LANGUAGE_ENGLISH)
            )
        }
        val clearButton = findViewById<Button>(R.id.clear)
        clearButton.setOnClickListener {
            chineseEditText.setText("")
            englishEditText.setText("")
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
        private var w: Word? = null

        override fun doInBackground(vararg lang: LanguageContainer): Boolean? {
            if (lang[0].language == LANGUAGE_ENGLISH){
                val result = fetchChinese(lang[0].text)
                println(result)
                w = Word(result)
            }
            else {
                w = Word(lang[0].text)
            }
            return w!!.run()
        }

        override fun onPostExecute(status: Boolean) {
            if (status) {
                Log.d("MainActivity-Taiwanese", w.toString())
                findViewById<View>(R.id.tw_result).visibility = View.VISIBLE
                val t = findViewById<TextView>(R.id.result)
                t.visibility = View.VISIBLE
                t.text = w!!.taiwanese
            } else {
                val error = this@MainActivity.getText(R.string.error).toString()
                Toast.makeText(this@MainActivity, error, Toast.LENGTH_LONG).show()
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
        val URL = URL("${URL_TO_CRAWL_ENCH}zh&tl=en&dt=t&q=$english")
        val connection = URL.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Accept-Charset", "UTF-8")
        val response = connection.responseCode
        if (response == HttpURLConnection.HTTP_OK) {
            val br = BufferedReader(InputStreamReader(connection.inputStream))
            var input = br.readLine();
            val resp = StringBuilder();
            while ((input) != null) {
                resp.append(input)
                input = br.readLine();
            }
            br.close()
            val res = resp.toString();
            val start = res.indexOf('"')
            val stop = res.indexOf('"', start)
            return res.substring(start + 1, stop)
        }
        return null
    }
}