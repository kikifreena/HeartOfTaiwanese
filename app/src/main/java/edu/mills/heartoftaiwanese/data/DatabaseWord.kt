package edu.mills.heartoftaiwanese.data

import java.util.Date

data class DatabaseWord(
    val word: Word,
    val id: Int,
    val favorite: Boolean = false,
    val accessTime: Date
)
