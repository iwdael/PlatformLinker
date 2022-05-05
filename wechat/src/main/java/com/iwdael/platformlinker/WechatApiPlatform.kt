package com.iwdael.platformlinker

import android.content.Intent
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler


abstract class WechatApiPlatform : WechatSharePlatform() {
    private var customHandler: IWXAPIEventHandler? = null
    fun setWxApiEventHandler(handler: IWXAPIEventHandler) {
        customHandler = handler
    }

    fun clearWxApiEventHandler() {
        customHandler = null
    }

    fun getWxApi() = iwxapi

    override fun handleIntent(data: Intent?) {
        super.handleIntent(data)
        customHandler?.let { iwxapi.handleIntent(data, it) }
    }
}