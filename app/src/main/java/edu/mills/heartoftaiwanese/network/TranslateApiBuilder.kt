package edu.mills.heartoftaiwanese.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TranslateApiBuilder {
    companion object {
        private const val TAG = "TranslateApiBuilder"
    }

    private val client: TranslateApi by lazy {
        initRestClient()
    }

    private fun initRestClient(): TranslateApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = Builder()
            .addInterceptor(interceptor)
            .build()

        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl("https://translate.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit: Retrofit = builder.client(httpClient).build()

        return retrofit.create(TranslateApi::class.java)
    }

    suspend fun getChineseTranslation(stringToTranslate: String): String {
        return withContext(Dispatchers.IO) {
            val response = client.getTranslation(
                stringToTranslate = stringToTranslate,
                sourceLanguage = LanguageChoice.EN,
                destLanguage = LanguageChoice.ZH_TW
            )
            Log.d(TAG, "${response.first()}")
            val result: String = extractResultFromResponseList(response.first().toString())
            Log.d(Companion.TAG, result)
            result
        }
    }

    suspend fun getEnglishTranslation(stringToTranslate: String): String {
        return withContext(Dispatchers.IO) {
            val response = client.getTranslation(
                stringToTranslate = stringToTranslate,
                sourceLanguage = LanguageChoice.ZH_TW,
                destLanguage = LanguageChoice.EN
            )
            Log.d(TAG, "${response.first()}")
            val result: String = extractResultFromResponseList(response.first().toString())
            Log.d(Companion.TAG, result)
            result
        }
    }

    /**
     * Response.first() will get you something like this: [[你好, hello, null, null, 10.0]]
     * So you need to extract it.
     */
    private fun extractResultFromResponseList(responseListString: String): String {
        Log.d(Companion.TAG, responseListString)
        return responseListString.trim { it == '[' || it == ']' }.trim()
            .split(",")
            .first()
    }
}
