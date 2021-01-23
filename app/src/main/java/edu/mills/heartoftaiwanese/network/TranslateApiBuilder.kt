package edu.mills.heartoftaiwanese.network

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TranslateApiBuilder {
    val client: TranslateApi by lazy {
        initRestClient()
    }

    fun initRestClient(): TranslateApi {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

        val builder: Retrofit.Builder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit: Retrofit = builder.client(httpClient.build()).build()

        return retrofit.create(TranslateApi::class.java)
    }

    suspend fun getTranslation() {
        val response = client.getTranslation("hello")
        Log.d("TranslateApiBuilder", response.toString())
    }

}