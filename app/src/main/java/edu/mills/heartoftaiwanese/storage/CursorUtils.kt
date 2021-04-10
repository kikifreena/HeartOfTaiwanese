package edu.mills.heartoftaiwanese.storage

import android.database.Cursor

/**
 * Gets a string for a column name by getting the column index for that column name first.
 */
@Throws(IllegalArgumentException::class)
fun Cursor.getString(tableKey: String): String  = getString(getColumnIndexOrThrow(tableKey))

/**
 * Get an Int for a column name by getting the column index for the column name.
 */
@Throws(IllegalArgumentException::class)
fun Cursor.getInt(tableKey: String): Int = getInt(getColumnIndexOrThrow(tableKey))

/**
 * 1 is true, 0 is false
 */
fun Boolean.toInt() = if (this) 1 else 0
