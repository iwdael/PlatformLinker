package com.hacknife.authority.compat

import android.content.Context

/**
 * author : 段泽全(hacknife)
 * e-mail : hacknife@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */


const val QQ: String = "qq"
const val WECHAT = "wechat"
const val WEIBO = "weibo"
const val WEIBO_REDIRECT_URL = "weiboRedirectUrl"
const val WEB_SCOPE = "weiboScope"
const val WEIBO_USERINFO_URL = "https://api.weibo.com/2/users/show.json"


const val WECHAT_SECRET = "wechatSecret"


val Context.qq get() = "${appInfo.metaData.get(QQ)}"
val Context.wechat get() = appInfo.metaData.getString(WECHAT)
val Context.wechatSecret get() = appInfo.metaData.getString(WECHAT_SECRET)
val Context.weibo get() = appInfo.metaData.getString(WEIBO)