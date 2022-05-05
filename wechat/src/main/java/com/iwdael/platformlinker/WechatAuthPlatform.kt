package com.iwdael.platformlinker

import android.content.Intent
import android.content.pm.PackageManager
import com.iwdael.platformlinker.Platforms.Companion.CUSTOM_ERR_CODE
import com.tencent.mm.opensdk.constants.ConstantsAPI.COMMAND_SENDAUTH
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelbase.BaseResp.ErrCode.*
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.json.JSONObject

/**
 * author : 段泽全(hacknife)
 * e-mail : hacknife@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */
abstract class WechatAuthPlatform : Platform {


    companion object {
        private const val TAG = "wechat-auth"
        private const val WECHAT_ID = "wechat_id"
        private const val WECHAT_SECRET = "wechat_secret"
        private const val NOT_FOUND_CONTEXT = "找不到上下文"
        private const val NOT_FOUND_APP_ID = "找不到WECHAT ID"
        private const val NOT_FOUND_APP_SECRET = "找不到WECHAT SECRET"
        private const val NOT_FOUND_LISTENER = "找不到AuthorizeListener"
        private const val URL_GET_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token"
        private const val URL_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo"
    }


    private val wechatID by lazy {
        val activity = Platforms.instance.lifecycle.topActivity()!!
        val appInfo =
            activity.packageManager
                .getApplicationInfo(activity.packageName, PackageManager.GET_META_DATA)
        appInfo.metaData.get(WECHAT_ID)?.toString() ?: throw  Exception(NOT_FOUND_APP_ID)
    }
    private val wechatSecret by lazy {
        val activity = Platforms.instance.lifecycle.topActivity()!!
        val appInfo =
            activity.packageManager
                .getApplicationInfo(activity.packageName, PackageManager.GET_META_DATA)
        appInfo.metaData.get(WECHAT_SECRET)?.toString() ?: ""
    }


    internal val iwxapi by lazy {
        val activity = Platforms.instance.lifecycle.topActivity()!!
        WXAPIFactory.createWXAPI(activity.applicationContext, wechatID, true)
            .apply { registerApp(wechatID) }
    }
    private val wxAPIEventHandler = object : IWXAPIEventHandler {
        override fun onReq(p0: BaseReq?) {
            PlatformLog.v(TAG, "onReq:${p0?.javaClass?.simpleName}")
        }

        override fun onResp(p0: BaseResp?) {
            PlatformLog.v(TAG, "onResp:${p0?.javaClass?.simpleName}")
            p0 ?: return
            if (p0.type == COMMAND_SENDAUTH) {
                p0 as SendAuth.Resp
                val listener =
                    Platforms.instance.lifecycle.authorizeListener(Authorize.WECHAT)
                if (p0.errCode == ERR_OK) {
                    if (wechatSecret.isEmpty()) {
                        if (listener == null) {
                            PlatformLog.v(TAG, NOT_FOUND_LISTENER)
                        } else {
                            listener.onSuccess(Authorize.WECHAT, Auth("",
                                "",
                                "",
                                "",
                                "",
                                JSONObject()
                                    .apply {
                                        put("code", p0.code)
                                        put("state", p0.state)
                                        put("url", p0.url)
                                        put("lang", p0.lang)
                                        put("country", p0.country)
                                    }
                                    .toString()
                            ))
                        }
                        return
                    }
                    ThreadCompat.run<JSONObject>({
                        HttpUtil.connect(
                            URL_GET_ACCESS_TOKEN,
                            BD.createAccessToken(wechatID, wechatSecret, p0.code)
                        )
                    }) { refresh ->
                        if (!refresh.has("refresh_token") || !refresh.has("access_token")) {
                            PlatformLog.v(TAG, "获取AccessToken失败")
                            if (listener == null) {
                                PlatformLog.v(TAG, NOT_FOUND_LISTENER)
                            } else {
                                listener.onFail(Authorize.WECHAT, CUSTOM_ERR_CODE)
                            }
                        } else {
                            ThreadCompat.run<JSONObject>({
                                HttpUtil.connect(
                                    URL_GET_USER_INFO,
                                    BD.createUserinfo(
                                        refresh["openid"].toString(),
                                        refresh["access_token"].toString(),
                                    )
                                )
                            }) { userInfo ->
                                if (!userInfo.has("nickname") || !userInfo.has("openid")) {
                                    PlatformLog.v(TAG, "获取USERINFO失败")
                                    if (listener == null) {
                                        PlatformLog.v(TAG, NOT_FOUND_LISTENER)
                                    } else {
                                        listener.onFail(
                                            Authorize.WECHAT,
                                            CUSTOM_ERR_CODE
                                        )
                                    }
                                } else {
                                    val auth = Auth(
                                        userInfo.getString("nickname"),
                                        userInfo.getString("headimgurl"),
                                        userInfo.getInt("sex").toString(),
                                        userInfo.getString("province"),
                                        userInfo.getString("city"),
                                        refresh.copyTo(userInfo).toString()
                                    )
                                    if (listener == null) {
                                        PlatformLog.v(TAG, NOT_FOUND_LISTENER)
                                    } else {
                                        listener.onSuccess(Authorize.WECHAT, auth)
                                    }
                                }
                            }
                        }


                    }

                } else if (p0.errCode == ERR_USER_CANCEL) {
                    if (listener == null) {
                        PlatformLog.v(TAG, NOT_FOUND_LISTENER)
                    } else {
                        listener.onCancel(Authorize.TENCENT)
                    }
                } else if (p0.errCode == ERR_AUTH_DENIED) {
                    if (listener == null) {
                        PlatformLog.v(TAG, NOT_FOUND_LISTENER)
                    } else {
                        listener.onFail(Authorize.TENCENT, CUSTOM_ERR_CODE)
                    }
                } else {
                    if (listener == null) {
                        PlatformLog.v(TAG, NOT_FOUND_LISTENER)
                    } else {
                        listener.onFail(Authorize.TENCENT, CUSTOM_ERR_CODE)
                    }
                }
                PlatformLog.v("wechat", "onResp:${p0.type} , ${p0.errCode}")
            }

        }

    }

    override fun handleIntent(data: Intent?) {
        PlatformLog.v("wechat", "handleIntent")
        iwxapi.handleIntent(data, wxAPIEventHandler)
    }

    override fun authorize(listener: AuthorizeListener) {
        Platforms.instance.lifecycle.bindAuthorizeListener(Authorize.WECHAT, listener)
        Platforms.instance.lifecycle.authorizeListener(Authorize.WECHAT)
            ?.onStart(Authorize.WECHAT)
        iwxapi.sendReq(SendAuth.Req().apply {
            scope = "snsapi_userinfo"
            state = "wechat_sdk_platform_linker"
        })
    }


}