package com.hacknife.authority.compat

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * author : 段泽全(hacknife)
 * e-mail : hacknife@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */

const val WECHAT_ACCESS_TOKEN: String = "https://api.weixin.qq.com/sns/oauth2/access_token"
const val WECHAT_USERINFO = "https://api.weixin.qq.com/sns/userinfo"

fun String.execute(map: Map<String, String>, callback: ((String?) -> Unit)) {
    val baseUrl = this
    GlobalScope.launch {
        val builder = StringBuilder()
        map.entries.forEachIndexed { index, entry ->
            if (index!=0) builder.append("&")
            builder.append(
                String.format(
                    "%s=%s",
                    entry.key,
                    URLEncoder.encode(entry.value, StandardCharsets.UTF_8.toString())
                )
            )
        }
        val url = URL("$baseUrl?$builder")
//        Log.v("dzq", "${url}")
        val connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.useCaches = true
        connection.requestMethod = "GET"
        connection.connect()
        if (connection.responseCode == 200) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val stringBuilder = StringBuilder()
            var line: String? = null
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            GlobalScope.launch(context = Dispatchers.Main) {
                callback.invoke(stringBuilder.toString())
            }

        } else {
            GlobalScope.launch(context = Dispatchers.Main) {
                callback.invoke(null)
            }
        }
    }
}