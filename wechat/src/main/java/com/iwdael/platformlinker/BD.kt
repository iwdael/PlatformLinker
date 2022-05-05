package com.iwdael.platformlinker

import org.json.JSONObject

object BD {
    fun createAccessToken(
        wechatID: String,
        wechatSecret: String,
        code: String
    ): Map<String, String> {
        return mapOf(
            "appid" to wechatID,
            "secret" to wechatSecret,
            "code" to code,
            "grant_type" to "authorization_code",
        )
    }

    fun createUserinfo(openid: String, access_token: String): Map<String, String> {
        return mapOf("openid" to openid, "access_token" to access_token)
    }

}