package com.iwdael.platformlinker

import com.iwdael.platformlinker.Platforms.Companion.CUSTOM_ERR_CODE


object PlatformLinker {
    private val platforms = Platforms.instance
    const val TAG = "usage"
    internal var isDebug = false

    fun openDebug() {
        isDebug = true
    }

    fun authorize(authorize: Authorize, listener: AuthorizeListener) {
        val platform = platforms[authorize]
        if (platform == null) {
            PlatformLog.e("auth", "平台缺失:${authorize}")
            listener.onFail(authorize, CUSTOM_ERR_CODE)
        } else
            platform.authorize(listener)
    }

    inline fun authorize(
        authorize: Authorize,
        crossinline start: (() -> Unit) = {},
        crossinline success: ((Auth) -> Unit) = {},
        crossinline fail: ((Int) -> Unit) = {},
        crossinline cancel: (() -> Unit) = {},
    ) {
        authorize(authorize, object : AuthorizeListener {
            override fun onStart(authorize: Authorize) = start.invoke()
            override fun onSuccess(authorize: Authorize, auth: Auth) = success.invoke(auth)
            override fun onFail(authorize: Authorize, errorCode: Int) = fail.invoke(errorCode)
            override fun onCancel(authorize: Authorize) = cancel.invoke()
        })
    }

    fun share(message: Message, listener: ShareListener) {
        val platform = platforms[message.authorize()]
        if (platform == null) {
            PlatformLog.e("auth", "平台缺失:${message.authorize()}")
            listener.onFail(message.authorize(), CUSTOM_ERR_CODE)
        } else {
            message.checkParameter()
            platform.share(message, listener)
        }
    }

    inline fun share(
        message: Message,
        crossinline start: (() -> Unit) = {},
        crossinline success: (() -> Unit) = {},
        crossinline fail: ((Int) -> Unit) = {},
        crossinline cancel: (() -> Unit) = {}
    ) {
        share(message, object : ShareListener {
            override fun onStart(authorize: Authorize) {
                start.invoke()
            }

            override fun onSuccess(authorize: Authorize) {
                success.invoke()
            }

            override fun onFail(authorize: Authorize, errorCode: Int) {
                fail.invoke(errorCode)
            }

            override fun onCancel(authorize: Authorize) {
                cancel.invoke()
            }
        })
    }

    fun share(authorize: Authorize, message: Any, listener: ShareListener) {
        val platform = platforms[authorize]
        if (platform == null) {
            PlatformLog.e("auth", "平台缺失:${authorize}")
            listener.onFail(authorize, CUSTOM_ERR_CODE)
        } else {
            PlatformLog.usageShare()
            platform.share(message, listener)
        }
    }

    inline fun share(
        authorize: Authorize,
        message: Any,
        crossinline start: (() -> Unit) = {},
        crossinline success: (() -> Unit) = {},
        crossinline fail: ((Int) -> Unit) = {},
        crossinline cancel: (() -> Unit) = {}
    ) {
        share(authorize, message, object : ShareListener {
            override fun onStart(authorize: Authorize) {
                start.invoke()
            }

            override fun onSuccess(authorize: Authorize) {
                success.invoke()
            }

            override fun onFail(authorize: Authorize, errorCode: Int) {
                fail.invoke(errorCode)
            }

            override fun onCancel(authorize: Authorize) {
                cancel.invoke()
            }
        })
    }

}