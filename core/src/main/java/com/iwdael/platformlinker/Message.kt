package com.iwdael.platformlinker


interface Message {
    fun check()
    fun authorize(): Authorize
    fun checkParameter() {
        try {
            check()
        } catch (e: Exception) {
            PlatformLog.usageShare()
            throw  e
        }
    }
}