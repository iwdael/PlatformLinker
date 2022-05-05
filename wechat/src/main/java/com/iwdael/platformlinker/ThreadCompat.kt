package com.iwdael.platformlinker

import android.os.Handler
import android.os.Looper


object ThreadCompat {
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    fun <T> run(runBlock: (() -> T), callback: ((T) -> Unit)) {
        Thread {
            val body = runBlock.invoke()
            handler.post {
                callback.invoke(body)
            }
        }.start()
    }
}
