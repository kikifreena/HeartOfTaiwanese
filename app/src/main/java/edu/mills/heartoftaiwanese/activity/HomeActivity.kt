package edu.mills.heartoftaiwanese.activity

import android.os.AsyncTask
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import edu.mills.heartoftaiwanese.R
import edu.mills.heartoftaiwanese.WordRetriever
import edu.mills.heartoftaiwanese.databinding.ActivityHomeBinding


class HomeActivity : MainActivity() {
    companion object {
        const val LANGUAGE_ENGLISH = 1
        const val LANGUAGE_CHINESE = 0
        private const val kResultOk = "SUCCESS"
    }

    private lateinit var binding: ActivityHomeBinding

    override fun getViewBinding() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item) {
            item -> return true
        }
        return true
    }

    override fun initializeClickListeners() {
        super.initializeClickListeners()
        binding.submitCh.setOnClickListener {
            binding.twResult.visibility = View.GONE
            binding.result.visibility = View.GONE
            ParseWordTask().execute(
                LanguageContainer(
                    binding.editTextCh.text.toString(),
                    LANGUAGE_CHINESE
                )
            )
        }
        binding.submitEn.setOnClickListener {
            binding.twResult.visibility = View.GONE
            binding.result.visibility = View.GONE
            ParseWordTask().execute(
                LanguageContainer(
                    binding.editTextEng.text.toString(),
                    LANGUAGE_ENGLISH
                )
            )
        }
        binding.clearButton.setOnClickListener {
            binding.editTextCh.setText("")
            binding.editTextEng.setText("")
            binding.twResult.visibility = View.GONE
            binding.result.visibility = View.GONE
        }
    }

    private data class LanguageContainer internal constructor(
        internal val text: String,
        internal val language: Int
    )

    private inner class ParseWordTask : AsyncTask<LanguageContainer, Int, String>() {
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
            if (lang[0].language == LANGUAGE_ENGLISH) {
                val chineseString = w.fetchChinese(lang[0].text)
                if (chineseString == WordRetriever.kUnknownError) {
                    return WordRetriever.kUnknownError
                } else if (chineseString == WordRetriever.kRateLimited) {
                    return WordRetriever.kRateLimited
                } else {
                    w = WordRetriever(chineseString)
                }
//                Log.d("MainActivity", w.toString())
            } else {
                w = WordRetriever(lang[0].text)
            }
            val status = w.run()
            if (!status) {
                return WordRetriever.kUnknownError
            } else {
                return kResultOk
            }
        }

        override fun onPostExecute(status: String) {
            if (status == WordRetriever.kRateLimited) {
                Toast.makeText(
                    this@HomeActivity,
                    getText(R.string.too_many_requests),
                    Toast.LENGTH_LONG
                ).show()
            } else if (status == WordRetriever.kUnknownError) {
                Toast.makeText(this@HomeActivity, getText(R.string.error), Toast.LENGTH_LONG).show()
            } else {
                Log.d("MainActivity-Taiwanese", w.toString())
                binding.twResult.visibility = View.VISIBLE
                binding.result.visibility = View.VISIBLE
                binding.result.text = w.taiwanese
            }
            super.onPostExecute(status)
        }
    }

}
