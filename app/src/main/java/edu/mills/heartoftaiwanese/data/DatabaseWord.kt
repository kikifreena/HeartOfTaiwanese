package edu.mills.heartoftaiwanese.data

import java.util.Date

/**
 * @property word
 * @property id
 * @property favorite
 * @property accessTime
 */
data class DatabaseWord(
    val word: Word,
    val id: Int,
    var favorite: Boolean = false,
    var accessTime: Date
)
