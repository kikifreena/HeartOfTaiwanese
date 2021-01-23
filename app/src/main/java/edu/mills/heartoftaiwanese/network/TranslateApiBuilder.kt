package edu.mills.heartoftaiwanese.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TranslateApiBuilder {
    val client: TranslateApi by lazy {
        initRestClient()
    }

    private val TAG = javaClass.simpleName

    fun initRestClient(): TranslateApi {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl("https://translate.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit: Retrofit = builder.client(httpClient.build()).build()

        return retrofit.create(TranslateApi::class.java)
    }

    suspend fun getTranslation() {
        withContext(Dispatchers.IO) {
            val thisClient = client
            val response = client.getTranslation(
                stringToTranslate = "hello",
                callback = object : Callback<List<Any>> {
                    override fun onResponse(
                        call: Call<List<Any>>?,
                        response: Response<List<Any>>?
                    ) {
                        Log.d(TAG, response.toString())
                        TODO("Not yet implemented")
                    }

                    override fun onFailure(call: Call<List<Any>>?, t: Throwable?) {
                        t?.printStackTrace()
                        t?.message?.let { Log.e(TAG, it) }
                    }
                })
            Log.d("TranslateApiBuilder", response.toString())
        }
    }

}