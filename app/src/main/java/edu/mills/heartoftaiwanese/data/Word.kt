package edu.mills.heartoftaiwanese.data

data class Word(
    val english: String? = null,
    val chinese: String? = null,
    val taiwanese: String? = null
) {
    /**
     *
     * @return true if there is a empty English, Chinese, or Taiwanese string; false otherwise
     */
    fun containsNull() =
        english.isNullOrBlank() || chinese.isNullOrBlank() || taiwanese.isNullOrBlank()

    /**
     * Check if the data object is still useful
     *
     * @return true if there is a non-empty English, Chinese, or Taiwanese string; false otherwise
     */
    fun isAllNull() =
        english.isNullOrBlank() && chinese.isNullOrBlank() && taiwanese.isNullOrBlank()
}
