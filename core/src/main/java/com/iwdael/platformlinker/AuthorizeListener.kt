package com.iwdael.platformlinker


interface AuthorizeListener {
    fun onStart(authorize: Authorize)
    fun onSuccess(authorize: Authorize, auth: Auth)
    fun onFail(authorize: Authorize, errorCode: Int)
    fun onCancel(authorize: Authorize)
}