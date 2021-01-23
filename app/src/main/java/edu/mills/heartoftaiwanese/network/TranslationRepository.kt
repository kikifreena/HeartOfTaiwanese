package edu.mills.heartoftaiwanese.network

import android.util.Log
import com.google.cloud.translate.v3.LocationName
import com.google.cloud.translate.v3.TranslateTextRequest
import com.google.cloud.translate.v3.TranslationServiceClient
import edu.mills.heartoftaiwanese.data.ChineseResult
import edu.mills.heartoftaiwanese.data.TaiwaneseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * A class for making network calls only.
 */
class TranslationRepository {
    companion object {
        private const val URL_TO_CRAWL_TW = "http://210.240.194.97/q/THq.asp?w="
        private const val URL_TO_CRAWL_ENCH =
            "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=en"
    }

    suspend fun getTaiwanese(chinese: String): TaiwaneseResult {
        return withContext(Dispatchers.IO) {
            val url = URL("${URL_TO_CRAWL_TW}$chinese")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Accept-Charset", "UTF-8")
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                TaiwaneseResult(WebResultCode.RESULT_OK, connection.inputStream.bufferedReader()
                    .use { it.readText() })
            } else {
                TaiwaneseResult(WebResultCode.INVALID_NOT_FOUND)
            }
        }
    }

    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()

    var client = OkHttpClient()

    @Throws(IOException::class)
    fun translate(url: String, json: String): String? {
        val body = json.toRequestBody(JSON)
        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        client.newCall(request).execute().use { response -> return response.body?.string() }
    }

    suspend fun getChinese(english: String): ChineseResult {
        return withContext(Dispatchers.IO) {
            Log.d("MainActivity", "Fetching Chinese for $english")
            translateText(english)?.let {
                ChineseResult(WebResultCode.RESULT_OK, it)
            } ?: ChineseResult(WebResultCode.UNKNOWN_ERROR)
        }
    }

    @Throws(IOException::class)
    private suspend fun translateText(text: String): String? {
        // TODO(developer): Replace these variables before running the sample.
        val projectId = "translation-ench"
        val targetLanguage = "zh-TW"
        return withContext(Dispatchers.IO) {
            TranslateApiBuilder().getTranslation()
            ""
        }
    }

    // Translating Text
    @Throws(IOException::class)
    suspend fun translateText(projectId: String?, targetLanguage: String?, text: String?): String? {
        return withContext(Dispatchers.IO) {
            // Initialize client that will be used to send requests. This client only needs to be created
            // once, and can be reused for multiple requests. After completing all of your requests, call
            // the "close" method on the client to safely clean up any remaining background resources.
            val client = TranslationServiceClient.create()

            var retValue: String?
            client.use { client ->
                val parent =
                    LocationName.of(projectId, "global")

                // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
                val request = TranslateTextRequest.newBuilder()
                    .setParent(parent.toString())
                    .setMimeType("text/plain")
                    .setTargetLanguageCode(targetLanguage)
                    .addContents(text)
                    .build()
                val response =
                    client.translateText(request)

                retValue = response.translationsList.first().translatedText
            }
            Log.d("TranslationRepository", retValue.toString())
            client.close()
            retValue
        }
    }
}

