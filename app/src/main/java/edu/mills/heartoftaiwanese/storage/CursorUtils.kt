package edu.mills.heartoftaiwanese.storage

import android.database.Cursor

/**
 * Gets a string for a column name by getting the column index for that column name first.
 */
@Throws(IllegalArgumentException::class)
fun Cursor.getString(tableKey: String): String {
    return getString(getColumnIndexOrThrow(tableKey))
}

/**
 * Get an Int for a column name by getting the column index for the column name.
 */
@Throws(IllegalArgumentException::class)
fun Cursor.getInt(tableKey: String): Int {
    return getInt(getColumnIndexOrThrow(tableKey))
}
