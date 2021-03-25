package edu.mills.heartoftaiwanese.storage

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.content.contentValuesOf
import edu.mills.heartoftaiwanese.data.DatabaseWord
import edu.mills.heartoftaiwanese.data.Word
import java.util.Date

class TranslationDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $tTranslations (" +
                    "$tId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$tKeyAccessTime INTEGER, " +
                    "$tKeyChinese TEXT, " +
                    "$tKeyEnglish TEXT, " +
                    "$tKeyTaiwanese TEXT, " +
                    // boolean, 0 for not favorite, 1 for favorite
                    "$tKeyFavorite INTEGER);"
        )
    }

    /**
     * @param word a fully filled out Word. None of the parameters should be null.
     * @return true if the operation was successful
     */
    fun insertWord(word: Word): Boolean {
        Log.d(TAG, "The word to insert: $word")
        require(!word.containsNull())
        val translationValues = contentValuesOf(
            tKeyChinese to word.chinese,
            tKeyEnglish to word.english,
            tKeyTaiwanese to word.taiwanese,
            tKeyFavorite to 0,
            tKeyAccessTime to currentTime
        )
        return writableDatabase.use {
            it.insert(tTranslations, null, translationValues) != -1L
        }
    }

    /**
     * Gets the 5 most recently added translations, or all if there are 5 or less.
     * @return a list of the newest (most recently added) words
     */
    fun getNewest(): List<DatabaseWord> {
        return readableDatabase.use { db ->
            db.query(
                tTranslations, allColumns, null, null, null, null, "$tId DESC", 5.toString()
            ).use { convertToDatabaseWord(it) }
        }
    }

    // Gets the 5 most recently viewed translations, or all if there are 5 or less.
    fun getRecent(): List<DatabaseWord> {
        return readableDatabase.use { db ->
            db.query(
                tTranslations, allColumns, null, null,
                null, null, "$tKeyAccessTime DESC", 5.toString()
            ).use {
                val wordList = convertToDatabaseWord(it)
                readableDatabase.close()
                wordList
            }
        }
    }

    /**
     * @return all of the entries in which "favorites" is true.
     */
    fun getAllFavorites(): List<DatabaseWord> {
        return readableDatabase.use { db ->
            db.query(
                tTranslations, allColumns, "$tKeyFavorite = ?", arrayOf("1"),
                null, null, "$tKeyAccessTime DESC"
            ).use {
                val wordList = convertToDatabaseWord(it)
                readableDatabase.close()
                wordList
            }
        }
    }

    fun getWordByTaiwanese(taiwanese: String): DatabaseWord? {
        return readableDatabase.use { db ->
            db.query(
                tTranslations, allColumns, "$tKeyTaiwanese = ?",
                arrayOf(taiwanese), null, null, null, 1.toString()
            ).use { convertToDatabaseWord(it).firstOrNull() }
        }
    }

    fun getWordByEnglish(english: String): DatabaseWord? {
        return readableDatabase.use { db ->
            db.query(
                tTranslations,
                allColumns,
                "$tKeyEnglish = ?",
                arrayOf(english),
                null,
                null,
                null,
                1.toString()
            ).use { convertToDatabaseWord(it).firstOrNull() }
        }
    }

    fun getWordByChinese(chinese: String): DatabaseWord? {
        return readableDatabase.query(
            tTranslations, allColumns, "$tKeyChinese = ?",
            arrayOf("$chinese"), null, null, null, 1.toString()
        ).use {
            val wordList = convertToDatabaseWord(it).firstOrNull()
            readableDatabase.close()
            wordList
        }
    }

    fun getWordById(id: String): DatabaseWord? {
        return readableDatabase.use { db ->
            db.query(
                tTranslations, allColumns,
                "$tId = ?", arrayOf(id), null, null, null, 1.toString()
            ).use {
                convertToDatabaseWord(it).firstOrNull()
            }
        }
    }

    /**
     * Update the last accessed time for an entry in the database.
     *
     * @param wordId the ID of the database entry.
     * @return true if the operation was successful and exactly one row was updated.
     */
    fun updateLastAccessTime(wordId: Int): Boolean {
        return readableDatabase.use { db ->
            db.update(
                tTranslations, contentValuesOf(tKeyAccessTime to currentTime),
                "$tId = ?", arrayOf(wordId.toString())
            ) == 1
        }
    }

    private fun convertToDatabaseWord(cursor: Cursor): List<DatabaseWord> {
        return mutableListOf<DatabaseWord>().apply {
            if (cursor.moveToFirst()) {
                add(
                    DatabaseWord(
                        Word(
                            english = cursor.getString(tKeyEnglish),
                            chinese = cursor.getString(tKeyChinese),
                            taiwanese = cursor.getString(tKeyTaiwanese)
                        ),
                        id = cursor.getInt(tId),
                        favorite = cursor.getInt(tKeyFavorite) == 1,
                        accessTime = Date(cursor.getInt(tKeyAccessTime).toLong())
                    )
                )
            }
            while (cursor.moveToNext()) {
                add(
                    DatabaseWord(
                        Word(
                            english = cursor.getString(tKeyEnglish),
                            chinese = cursor.getString(tKeyChinese),
                            taiwanese = cursor.getString(tKeyTaiwanese)
                        ),
                        id = cursor.getInt(tId),
                        favorite = cursor.getInt(tKeyFavorite) == 1,
                        accessTime = Date(cursor.getInt(tKeyAccessTime).toLong())
                    )
                )
            }
        }
    }

    private val currentTime: String
        get() = Date().time.toString()

    companion object {
        private const val TAG = "TranslationDatabaseHelper"
        private const val DB_NAME = "translator"
        private const val DB_VERSION = 1

        // table
        private const val tTranslations = "TRANSLATIONS"

        // Table keys
        private const val tId = "_id"
        private const val tKeyChinese = "CHINESE"
        private const val tKeyEnglish = "ENGLISH"
        private const val tKeyTaiwanese = "TAIWANESE"
        private const val tKeyFavorite = "IS_FAVORITE"
        private const val tKeyAccessTime = "LAST_ACCESSED"

        private val allColumns =
            arrayOf(tId, tKeyEnglish, tKeyTaiwanese, tKeyChinese, tKeyFavorite, tKeyAccessTime)
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     *
     *
     * The SQLite ALTER TABLE documentation can be found
     * [here](http://sqlite.org/lang_altertable.html). If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     *
     *
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     *
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}
