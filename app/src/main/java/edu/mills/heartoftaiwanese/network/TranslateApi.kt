package edu.mills.heartoftaiwanese.network

import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.http.POST
import retrofit2.http.Query

interface TranslateApi {
    @POST("translate_a/single")
    suspend fun getTranslation(
        @Query("q") stringToTranslate: String,
        @Query("sl") sourceLanguage: LanguageChoice = LanguageChoice.EN,
        @Query("dl") destLanguage: LanguageChoice = LanguageChoice.ZH,
        @Query("client") client: String = "gtx",
        @Query("dt") dt: String = "t",
        callback: Callback<List<Any>>
    ): ResponseBody
}

enum class LanguageChoice { EN, ZH }