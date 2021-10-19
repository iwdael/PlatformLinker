package com.hacknife.authority

import androidx.appcompat.app.AppCompatActivity

/**
 * author : 段泽全(hacknife)
 * e-mail : hacknife@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */
abstract class Auth {
    var successListener: ((AuthType, String) -> Unit)? = null
    var cancelListener: ((AuthType) -> Unit)? = null
    var errorListener: ((AuthType, Any?) -> Unit)? = null

    fun setSuccessListener(l: (AuthType, String) -> Unit): Auth {
        this.successListener = l
        return this
    }

    fun setCancelListener(l: (AuthType) -> Unit): Auth {
        this.cancelListener = l
        return this
    }

    fun setErrorListener(l: (AuthType, Any?) -> Unit): Auth {
        this.errorListener = l
        return this
    }

    abstract fun auth(type: AuthType)

    companion object {
        fun create(activity: AppCompatActivity) = Authorize(activity) as Auth
    }


}