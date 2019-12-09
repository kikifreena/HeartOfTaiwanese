package edu.mills.heartoftaiwanese

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

internal class Word(input: String?) {
    private var chinese : String? = null
        private set;
    var taiwanese = ""
        private set;

    init {
        this.chinese = input
    }

    fun run(): Boolean {
        try {
            this.crawlSite()
            return taiwanese != INVALID_MESSAGE
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

    }

    private fun parse(data: String): String {
        try {
            val loc = data.indexOf("台語羅馬字")
            val loc2 = 1 + data.indexOf(">", data.indexOf("<span", loc))
            val loc3 = data.indexOf("</span>", loc2)
            return convertToString(data.substring(loc2, loc3))
        } catch (e: StringIndexOutOfBoundsException) {
            return INVALID_MESSAGE
        }

    }

    private fun convertToString(hex: String): String {
        val split = hex.split("&#|;".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        val sb = StringBuilder()
        println(hex)
        for (curr in split) {
            if (curr.matches("\\d+".toRegex())) {
                sb.append(Integer.parseInt(curr).toChar())
            } else {
                sb.append(curr)
            }
        }
        return sb.toString()
    }

    @Throws(IOException::class)
    private fun crawlSite() {
        val url = URL("$URL_TO_CRAWL_TW$chinese")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Accept-Charset", "UTF-8")
        val response = connection.responseCode
        if (response == HttpURLConnection.HTTP_OK) {
            val br = BufferedReader(InputStreamReader(connection.inputStream))
            var input = br.readLine();
            val resp = StringBuilder();
            while ((input) != null) {
                resp.append(input)
                input = br.readLine();
            }
            br.close()
            taiwanese = this.parse(resp.toString())
        }
    }



    companion object {
        private const val URL_TO_CRAWL_TW = "http://210.240.194.97/q/THq.asp?w="
        private const val INVALID_MESSAGE = "Not found"

    }
}
