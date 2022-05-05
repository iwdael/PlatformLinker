package com.iwdael.platformlinker


class Platforms : LinkedHashMap<Authorize, Platform>() {
    val lifecycle = PlatformLinkerActivityLifecycle()

    companion object {
        val instance = Platforms()
        const val CUSTOM_ERR_CODE = -10001
    }
}