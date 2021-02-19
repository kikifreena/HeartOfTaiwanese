package edu.mills.heartoftaiwanese.data

data class Word(
    val english: String? = null,
    val chinese: String? = null,
    val taiwanese: String? = null
) {
    fun containsNull() =
        english.isNullOrBlank() || chinese.isNullOrBlank() || taiwanese.isNullOrBlank()
}
