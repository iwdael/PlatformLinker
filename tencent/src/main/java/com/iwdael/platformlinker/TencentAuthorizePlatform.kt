package com.iwdael.platformlinker

import android.content.Intent
import android.content.pm.PackageManager
import com.iwdael.platformlinker.Platforms.Companion.CUSTOM_ERR_CODE
import com.tencent.connect.UserInfo
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.json.JSONObject

abstract class TencentAuthorizePlatform : Platform {


    companion object {
        private const val APP_ID = "tencent_appid"
        private const val FILE_PROVIDER = "com.iwdael.platformlinker.qq.fileprovider"
        const val NOT_FOUND_CONTEXT = "找不到上下文"
        private const val NOT_FOUND_APP_ID = "找不到QQ ID"
        private const val NOT_FOUND_LISTENER = "找不到AuthorizeListener"
        private const val TAG = "tencent-auth"
        private const val OPEN_ID = "openid"
        private const val ACCESS_TOKEN = "access_token"
        private const val EXPIRES_IN = "expires_in"
        private const val NICK_NAME = "nickname"
        private const val GENDER = "nickname"
        private const val PROVINCE = "province"
        private const val CITY = "city"
        private const val FIGURE_URL = "figureurl_qq"
    }


    val appID by lazy {
        val activity = Platforms.instance.lifecycle.topActivity()!!
        val appInfo =
            activity.packageManager
                .getApplicationInfo(activity.packageName, PackageManager.GET_META_DATA)
        appInfo.metaData.get(APP_ID)?.toString() ?: throw  Exception(NOT_FOUND_APP_ID)
    }

    val tencent by lazy {
        val activity = Platforms.instance.lifecycle.topActivity()!!
        Tencent.createInstance(appID, activity.application, FILE_PROVIDER)
            .apply {
                Tencent.setIsPermissionGranted(true)
            }
    }


    private var authData: JSONObject? = null
    private val authCallback = object : IUiListener {
        override fun onComplete(p0: Any?) {
            val activity = Platforms.instance.lifecycle.topActivity()
            val listener = Platforms.instance.lifecycle.authorizeListener(Authorize.TENCENT)
            if (activity == null) {
                PlatformLog.v(TAG, NOT_FOUND_CONTEXT)
                listener?.onFail(Authorize.TENCENT, CUSTOM_ERR_CODE)
                return
            }

            if (listener == null) {
                PlatformLog.v(TAG, NOT_FOUND_LISTENER)
            }
            val auth = JSONObject("$p0")
            if (!auth.has(OPEN_ID) || !auth.has(ACCESS_TOKEN)) {
                PlatformLog.e(TAG, "授权失败")
                listener?.onFail(Authorize.TENCENT, CUSTOM_ERR_CODE)
                return
            }
            PlatformLog.v(TAG, "授权成功:$p0")
            authData = auth
            tencent.openId = auth[OPEN_ID].toString()
            tencent.setAccessToken(auth[ACCESS_TOKEN].toString(), auth[EXPIRES_IN].toString())
            UserInfo(activity, tencent.qqToken).getUserInfo(userInfoCallback)
        }

        override fun onError(p0: UiError?) {
            PlatformLog.e(TAG, "授权失败:$p0")
            Platforms.instance.lifecycle.authorizeListener(Authorize.TENCENT)
                ?.onFail(Authorize.TENCENT, p0?.errorCode ?: CUSTOM_ERR_CODE)
        }

        override fun onCancel() {
            PlatformLog.v(TAG, "授权取消")
            Platforms.instance.lifecycle.authorizeListener(Authorize.TENCENT)
                ?.onCancel(Authorize.TENCENT)
        }

        override fun onWarning(p0: Int) {
            PlatformLog.v(TAG, "授权警告:${p0}")
        }
    }

    private val userInfoCallback = object : IUiListener {
        override fun onComplete(p0: Any?) {
            val infoData = JSONObject("$p0")
            val listener = Platforms.instance.lifecycle.authorizeListener(Authorize.TENCENT)
            if (!infoData.has(NICK_NAME) || !infoData.has(FIGURE_URL)) {
                PlatformLog.e(TAG, "获取用户信息失败")
                listener?.onFail(Authorize.TENCENT, CUSTOM_ERR_CODE)
                return
            }
            PlatformLog.v(TAG, "获取用户信息成功:$p0")
            val source = JSONObject()
            authData?.let { data -> data.keys().forEach { source.put(it, data[it]) } }
            infoData.let { data -> data.keys().forEach { source.put(it, data[it]) } }
            val auth = Auth(
                source[NICK_NAME].toString(),
                source[FIGURE_URL].toString(),
                source[GENDER].toString(),
                source[PROVINCE].toString(),
                source[CITY].toString(),
                source.toString()
            )

            if (listener == null) {
                PlatformLog.v(TAG, NOT_FOUND_LISTENER)
            } else {
                listener.onSuccess(Authorize.TENCENT, auth)
            }
        }

        override fun onError(p0: UiError?) {
            PlatformLog.e(TAG, "获取用户信息失败:$p0")
            val listener = Platforms.instance.lifecycle.authorizeListener(Authorize.TENCENT)
            if (listener == null) {
                PlatformLog.v(TAG, NOT_FOUND_LISTENER)
            } else {
                listener.onFail(Authorize.TENCENT, CUSTOM_ERR_CODE)
            }
        }

        override fun onCancel() {
            PlatformLog.e(TAG, "获取用户信息取消")
            val listener = Platforms.instance.lifecycle.authorizeListener(Authorize.TENCENT)
            if (listener == null) {
                PlatformLog.v(TAG, NOT_FOUND_LISTENER)
            } else {
                listener.onFail(Authorize.TENCENT, CUSTOM_ERR_CODE)
            }
        }

        override fun onWarning(p0: Int) {
            PlatformLog.v(TAG, "获取用户信息警告:${p0}")
        }
    }

    override fun authorize(listener: AuthorizeListener) {
        Platforms.instance.lifecycle.bindAuthorizeListener(Authorize.TENCENT, listener)
        val activity = Platforms.instance.lifecycle.topActivity()
        if (activity == null) {
            PlatformLog.v(TAG, NOT_FOUND_CONTEXT)
            listener.onFail(Authorize.TENCENT, CUSTOM_ERR_CODE)
            return
        }
        Platforms.instance.lifecycle.authorizeListener(Authorize.TENCENT)
            ?.onStart(Authorize.TENCENT)
        val intent = Intent(activity, TencentAuthActivity::class.java)
        intent.putExtra(TencentAuthActivity.BUNDLE_EXTRA_TYPE_IS_AUTHORIZE, true)
        activity.startActivity(intent)
    }

    fun authorize() {
        val activity = Platforms.instance.lifecycle.topActivity()!!
        PlatformLog.v("qq", "在${activity.javaClass.simpleName}发起授权,appID:${appID}")
        if (!tencent.isSessionValid) {
            val params = HashMap<String, Any>()
            params[Constants.KEY_SCOPE] = "all"
            params[Constants.KEY_QRCODE] = false
            params[Constants.KEY_ENABLE_SHOW_DOWNLOAD_URL] = false
            tencent.login(activity, authCallback, params)
        } else {
            tencent.logout(activity)
            tencent.login(activity, "all", authCallback)
        }
    }

    open fun activityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_LOGIN)
            Tencent.onActivityResultData(requestCode, resultCode, data, authCallback)
    }

    override fun handleIntent(intent: Intent?) {

    }

}