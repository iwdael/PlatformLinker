package com.iwdael.platformlinker

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.WindowManager


open class TransparentActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.clearFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        super.onCreate(savedInstanceState)
        window.decorView.alpha = 0.0f
    }
}