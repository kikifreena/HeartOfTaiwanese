package edu.mills.heartoftaiwanese.data

import java.util.Date

data class DatabaseWord(
    val word: Word,
    val id: Int,
    var favorite: Boolean = false,
    var accessTime: Date
)
