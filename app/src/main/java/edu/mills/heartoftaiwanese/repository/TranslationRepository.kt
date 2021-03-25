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
    }

    private val cachedTranslationsToChinese = mutableMapOf<String, ChineseResult>()
    private val cachedTranslationsToTaiwanese = mutableMapOf<String, TaiwaneseResult>()
    private val translationDatabaseHelper by lazy { TranslationDatabaseHelper(context) }
    private val taiwaneseApi by lazy { TaiwaneseApi() }

    suspend fun getTaiwanese(chinese: String): TaiwaneseResult {
        return withContext(Dispatchers.IO) {
            // Get from the cache if possible...
            val possibleTaiwaneseResult: TaiwaneseResult? = cachedTranslationsToTaiwanese[chinese]
            if (possibleTaiwaneseResult?.resultCode == WebResultCode.RESULT_OK) {
                possibleTaiwaneseResult
            }
            // The cache didn't work, get from database
            else {
                getWordFromDatabase(Word(chinese = chinese))?.toTaiwaneseResult()
                    ?.let { databaseResult ->
                        if (databaseResult.resultCode == WebResultCode.RESULT_OK) {
                            databaseResult
                        } else {
                            getTaiwaneseFromNetwork(chinese)
                        }
                        // The database didn't work, get from network
                    } ?: getTaiwaneseFromNetwork(chinese)
            }
        }
    }

    suspend fun getChinese(english: String): ChineseResult {
        return withContext(Dispatchers.IO) {
            val possibleChineseResult = cachedTranslationsToChinese[english]
            // Get from the cache if possible...
            if (possibleChineseResult?.resultCode == WebResultCode.RESULT_OK) {
                possibleChineseResult
            }
            // cache did not work, use database or network
            else {
                getWordFromDatabase(Word(english = english))?.toChineseResult()
                    ?.let { databaseResult ->
                        if (databaseResult.resultCode == WebResultCode.RESULT_OK) {
                            databaseResult
                        } else {
                            translateTextToChinese(english)
                        }
                    } ?: translateTextToChinese(english)
            }
        }
    }

    suspend fun getFavorites(): List<DatabaseWord> {
        Log.d(TAG, "Getting Favorites")
        return withContext(Dispatchers.IO) {
            translationDatabaseHelper.getAllFavorites()
        }
    }

    suspend fun getRecent(): List<DatabaseWord> {
        Log.d(TAG, "Getting recent")
        return withContext(Dispatchers.IO) {
            translationDatabaseHelper.getRecent()
        }
    }

    /**
     * @param translationId ID from {DatabaseWord.id]
     * @param newFavoriteStatus true if it should be favorite, false if it's no longer favorite
     */
    suspend fun favorite(translationId: Int, newFavoriteStatus: Boolean) {
        return withContext(Dispatchers.IO) {
            translationDatabaseHelper.favoriteWord(translationId, newFavoriteStatus)
        }
    }

    @Throws(IOException::class)
    private suspend fun translateTextToChinese(text: String): ChineseResult {
        Log.d(TAG, "translate text to chinese")
        return try {
            TranslateApiBuilder().getChineseTranslation(text)
        } catch (e: Exception) {
            null
        }?.let {
            ChineseResult(WebResultCode.RESULT_OK, it).apply {
                // success: Store in cache
                cachedTranslationsToChinese[text] = this
                // Success: Store in database
//                storeWordInDatabase(Word(english = text, chinese = this.chinese))
            }
        } ?: ChineseResult(WebResultCode.UNKNOWN_ERROR)
    }

    @Throws(IOException::class)
    private suspend fun translateTextToEnglish(chinese: String): String? {
        Log.d(TAG, "translate text to English")
        return withContext(Dispatchers.IO) {
            try {
                TranslateApiBuilder().getEnglishTranslation(chinese)
            } catch (exception: Exception) {
                null // Just let it be null... it is caught later anyway
            }
        }
    }

    private suspend fun getTaiwaneseFromNetwork(chinese: String): TaiwaneseResult {
        Log.d(TAG, "Getting taiwanese from network!")
        return taiwaneseApi.getTaiwanese(chinese).let { result ->
            if (result.resultCode == WebResultCode.RESULT_OK) {
                cachedTranslationsToTaiwanese[chinese] = result
                // Store in database too
                storeWordInDatabase(Word(chinese = chinese, taiwanese = result.taiwanese))
            }
            result
        }
    }

    private suspend fun storeWordInDatabase(word: Word) {
        Log.d(TAG, "Storing in database...")
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
                    Log.i(TAG, "Will insert word: $wordToInsert")
                } ?: run {
                    Log.e(TAG, "English translation failed... skipping database storage")
                    return@withContext
                }
            }
            val isSuccessful = translationDatabaseHelper.insertWord(wordToInsert)
            if (!isSuccessful) {
                Log.e(TAG, "Full word insertion failed. Not stored in database.")
            } else {
                Log.i(TAG, "Word successfully stored.")
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
        Log.d(TAG, "Checking the database...")
        return withContext(Dispatchers.IO) {
            if (!word.isAllNull()) { // you can't insert a blank word lol
                when {
                    word.taiwanese != null -> {
                        Log.d(TAG, "Checking Taiwanese")
                        translationDatabaseHelper.getWordByTaiwanese(word.taiwanese)
                    }
                    word.chinese != null -> {
                        Log.d(TAG, "Checking chinese")
                        translationDatabaseHelper.getWordByChinese(word.chinese)
                    }
                    word.english != null -> {
                        Log.d(TAG, "Checking english")
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
