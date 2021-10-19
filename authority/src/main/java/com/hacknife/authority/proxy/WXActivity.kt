package com.hacknife.authority.proxy

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import com.hacknife.authority.compat.wechat
import com.hacknife.authority.wechat.WechatAuthorization

import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

open class WXActivity : Activity() {
    private var api: IWXAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            api = WXAPIFactory.createWXAPI(this, wechat, false)
            WechatAuthorization.HEANDLER?.let { api!!.handleIntent(intent, it) }
            finish()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        WechatAuthorization.HEANDLER?.let { api!!.handleIntent(intent, it) }
        finish()
    }

}