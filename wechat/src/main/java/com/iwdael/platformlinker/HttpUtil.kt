package com.iwdael.platformlinker

import org.json.JSONObject
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

/**
 * author : 段泽全(hacknife)
 * e-mail : hacknife@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */
object HttpUtil {
    fun connect(url: String, map: Map<String, Any>): JSONObject {
        val builder = StringBuilder(url).append("?")
        val params = map.entries
        params.forEachIndexed { index, entry ->
            builder.append("${entry.key}=${entry.value}")
            if (index < params.size - 1) builder.append("&")
        }
        PlatformLog.v("wechat", "url:${builder.toString()}")
        val connection = URL(builder.toString()).openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "GET"
            connection.connect()
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val ins = connection.inputStream
                val json = readInputStream(ins)
                ins.close()
                return JSONObject(json)
            } else {
                return JSONObject()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return JSONObject()
        } finally {
            connection.disconnect()
        }

    }


    private fun readInputStream(ins: InputStream): String {
        return InputStreamReader(ins).readText()
    }


}