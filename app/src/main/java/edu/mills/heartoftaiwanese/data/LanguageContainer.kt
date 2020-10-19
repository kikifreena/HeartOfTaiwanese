package edu.mills.heartoftaiwanese.data

data class LanguageContainer(
    val text: String,
    val language: Language
)

enum class Language {
    LANGUAGE_ENGLISH,
    LANGUAGE_CHINESE
}