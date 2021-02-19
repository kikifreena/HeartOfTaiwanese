package edu.mills.heartoftaiwanese.storage

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TranslationDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    override fun onCreate(db: SQLiteDatabase?) {
        TODO("Not yet implemented - under construction")

//        db.execSQL("CREATE TABLE DRINK ("
//                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + "NAME TEXT, "
//                + "DESCRIPTION TEXT, "
//                + "IMAGE_RESOURCE_ID INTEGER);");
    }

    // private static void insertDrink(SQLiteDatabase db, String name,
    //                                    String description, int resourceId) {
    //        ContentValues drinkValues = new ContentValues();
    //        drinkValues.put("NAME", name);
    //        drinkValues.put("DESCRIPTION", description);
    //        drinkValues.put("IMAGE_RESOURCE_ID", resourceId);
    //        db.insert("DRINK", null, drinkValues);
    //    }

    companion object {
        const val DB_NAME = "translate"
        const val DB_VERSION = 0
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
