package edu.mills.heartoftaiwanese.repository

import android.content.Context
import android.util.Log
import edu.mills.heartoftaiwanese.data.ChineseResult
import edu.mills.heartoftaiwanese.data.DatabaseWord
import edu.mills.heartoftaiwanese.data.TaiwaneseResult
import edu.mills.heartoftaiwanese.data.Word
import edu.mills.heartoftaiwanese.network.TaiwaneseApi
import edu.mills.heartoftaiwanese.network.TranslateApiBuilder
import edu.mills.heartoftaiwanese.network.WebResultCode
import edu.mills.heartoftaiwanese.storage.TranslationDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * A class for making network calls only.
 */
class TranslationRepository(private val context: Context) {
    companion object {
        private const val TAG = "TranslationRepository"
        private const val URL_TO_CRAWL_TW = "http://210.240.194.97/q/THq.asp?w="
    }

    private val cachedTranslationsToChinese = mutableMapOf<String, ChineseResult>()
    private val cachedTranslationsToTaiwanese = mutableMapOf<String, TaiwaneseResult>()
    private val translationDatabaseHelper by lazy { TranslationDatabaseHelper(context) }
    private val taiwaneseApi by lazy { TaiwaneseApi() }

    suspend fun getTaiwanese(chinese: String): TaiwaneseResult {
        return withContext(Dispatchers.IO) {
            val possibleTaiwaneseResult: TaiwaneseResult? = cachedTranslationsToTaiwanese[chinese]
            // Get from the cache if possible...
            if (!cachedTranslationsToTaiwanese.containsKey(chinese) ||
                possibleTaiwaneseResult == null ||
                possibleTaiwaneseResult.resultCode != WebResultCode.RESULT_OK
            ) {
                taiwaneseApi.getTaiwanese(chinese).let {
                    if (it.resultCode == WebResultCode.RESULT_OK) {
                        cachedTranslationsToTaiwanese[chinese] = it
                    }
                    it
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
                TranslateApiBuilder().getChineseTranslation(text)
            } catch (exception: Exception) {
                null // Just let it be null... it is caught later anyway
            }
        }
    }

    @Throws(IOException::class)
    private suspend fun translateTextToEnglish(chinese: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                TranslateApiBuilder().getEnglishTranslation(chinese)
            } catch (exception: Exception) {
                null // Just let it be null... it is caught later anyway
            }
        }
    }

    private suspend fun storeWordInDatabase(word: Word) {
        require(!word.taiwanese.isNullOrBlank() && !word.chinese.isNullOrBlank())
        withContext(Dispatchers.IO) {
            var wordToInsert = word // Default to the original word
            if (word.containsNull()) {
                // There is only Chinese and Taiwanese; there is no English... so we need to get the English
                translateTextToEnglish(word.chinese)?.let { english ->
                    // Got the english, store it in English to Chinese cache for later.
                    cachedTranslationsToChinese[english] =
                        ChineseResult(WebResultCode.RESULT_OK, word.chinese)
                    // Update the word to insert.
                    wordToInsert =
                        Word(english = english, chinese = word.chinese, taiwanese = word.taiwanese)
                } ?: Log.e(TAG, "English translation failed... skipping database storage")
                return@withContext
            }
            val isSuccessful = translationDatabaseHelper.insertWord(wordToInsert)
            if (!isSuccessful) {
                Log.e(TAG, "Full word insertion failed. Not stored in database.")
            }
        }
    }

    /**
     * Method to attempt to get a word from database.
     * @param word a [Word] that has taiwanese, chinese, or english filled in.
     * It will attempt to fetch words in that priority. Leave out Taiwanese if you want to go by Chinese, etc.
     *
     * @return a DatabaseWord or null if no word matching the parameters was found.
     */
    private suspend fun getWordFromDatabase(word: Word): DatabaseWord? {
        return withContext(Dispatchers.IO) {
            if (!word.isAllNull()) { // you can't insert a blank word lol
                when {
                    word.taiwanese != null -> {
                        translationDatabaseHelper.getWordByTaiwanese(word.taiwanese)
                    }
                    word.chinese != null -> {
                        translationDatabaseHelper.getWordByChinese(word.chinese)
                    }
                    word.english != null -> {
                        translationDatabaseHelper.getWordByEnglish(word.english)
                    }
                    else -> {
                        // This should be impossible but it won't compile without it.
                        throw IllegalStateException("How can all of them be null when you checked that they're all not null???")
                    }
                }
            } else throw IllegalArgumentException("A blank word is being attempted. Do not call this method for a blank word.")
        }

    }
}
