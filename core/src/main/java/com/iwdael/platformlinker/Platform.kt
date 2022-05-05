package com.iwdael.platformlinker

import android.content.Intent


interface Platform {
    fun authorize(listener: AuthorizeListener)
    fun share(message: Any, listener: ShareListener)
    fun handleIntent(intent: Intent?)
}