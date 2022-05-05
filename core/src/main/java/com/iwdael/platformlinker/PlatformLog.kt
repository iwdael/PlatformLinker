package com.iwdael.platformlinker

import android.util.Log

object PlatformLog {
    private const val prefix = "platform-linker-"
    fun v(tag: String, format: String) {
        if (PlatformLinker.isDebug) Log.v("${prefix}${tag}", format)
    }

    fun e(tag: String, format: String, vararg vars: String) {
        if (PlatformLinker.isDebug) Log.e("${prefix}${tag}", String.format(format, vars))
    }

    fun usageShare() {
        v(
            PlatformLinker.TAG,
            "分享到QQ空间:https://wiki.connect.qq.com/%e5%88%86%e4%ba%ab%e5%88%b0qq%e7%a9%ba%e9%97%b4%ef%bc%88%e6%97%a0%e9%9c%80qq%e7%99%bb%e5%bd%95%ef%bc%89"
        )
        v(
            PlatformLinker.TAG,
            "分享到QQ好友:https://wiki.connect.qq.com/%e5%88%86%e4%ba%ab%e6%b6%88%e6%81%af%e5%88%b0qq%ef%bc%88%e6%97%a0%e9%9c%80qq%e7%99%bb%e5%bd%95%ef%bc%89"
        )
        v(
            PlatformLinker.TAG,
            "分享到微信:https://developers.weixin.qq.com/doc/oplatform/Mobile_App/Share_and_Favorites/Android.html"
        )
    }
}