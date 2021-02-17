package edu.mills.heartoftaiwanese.network

import retrofit2.http.GET
import retrofit2.http.Query

interface TranslateApi {
    @GET("translate_a/single")
    suspend fun getTranslation(
        @Query("q") stringToTranslate: String,
        @Query("sl") sourceLanguage: LanguageChoice = LanguageChoice.EN,
        @Query("tl") destLanguage: LanguageChoice = LanguageChoice.ZH,
        @Query("client") client: String = "gtx",
        @Query("dt") dt: String = "t"
    ): List<Any>
}

enum class LanguageChoice { EN, ZH }