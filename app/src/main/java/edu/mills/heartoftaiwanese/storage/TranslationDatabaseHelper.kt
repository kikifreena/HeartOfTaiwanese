package edu.mills.heartoftaiwanese.storage

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
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
            "CREATE TABLE $tTranslations ("
                    + "$tId INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "$tKeyAccessTime INTEGER"
                    + "$tKeyChinese TEXT, "
                    + "$tKeyEnglish TEXT, "
                    + "$tKeyTaiwanese TEXT, "
                    // boolean, 0 for not favorite, 1 for favorite
                    + "$tKeyFavorite INTEGER);"
        )
    }

    private fun insertWords(word: Word) {
        require(!word.containsNull())
        val translationValues = ContentValues(3).apply {
            put(tKeyChinese, word.chinese)
            put(tKeyEnglish, word.english)
            put(tKeyTaiwanese, word.taiwanese)
        }
        writableDatabase.insert(tTranslations, null, translationValues)
        writableDatabase.close()
    }

    // Gets the 5 most recently added translations, or all if there are 5 or less.
    fun getNewest(): List<DatabaseWord> {
        return readableDatabase.query(
            tTranslations, allColumns, null, null, null, null, "$tId DESC", 5.toString()
        ).let {
            val wordList = convertToDatabaseWord(it)
            it.close()
            readableDatabase.close()
            wordList
        }
    }

    // Gets the 5 most recently viewed translations, or all if there are 5 or less.
    fun getRecent(): List<DatabaseWord> {
        return readableDatabase.query(
            tTranslations, allColumns, null, null, null, null, "$tKeyAccessTime DESC", 5.toString()
        ).let {
            val wordList = convertToDatabaseWord(it)
            it.close()
            readableDatabase.close()
            wordList
        }
    }

    fun getAllFavorites(): List<DatabaseWord> {
        TODO("Not yet implemented")
    }

    fun getWordByTaiwanese(): DatabaseWord {
        TODO("Not yet implemented")
    }

    fun getWordByChinese(chinese: String): DatabaseWord {
        TODO("Not yet implemented")
    }

    fun getWordById(id: String): DatabaseWord {
        TODO("Not yet implemented")
    }

    /**
     * Update the last accessed time for an entry in the database.
     *
     * @param wordId the ID of the database entry.
     */
    fun updateLastAccessTime(wordId: Int) {
        TODO("Not yet implemented")
    }

    private fun convertToDatabaseWord(cursor: Cursor): List<DatabaseWord> {
        return mutableListOf<DatabaseWord>().apply {
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

    companion object {
        private const val DB_NAME = "translator"
        private const val DB_VERSION = 0

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
            arrayOf(tKeyChinese, tKeyEnglish, tKeyTaiwanese, tKeyFavorite, tKeyAccessTime)

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