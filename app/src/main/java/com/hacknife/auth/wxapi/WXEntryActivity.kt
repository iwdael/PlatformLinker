package com.hacknife.auth.wxapi

import android.os.Bundle
import com.hacknife.authority.proxy.WXActivity

class WXEntryActivity : WXActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.v("dzq","create")
    }

    override fun onDestroy() {
        super.onDestroy()
//        Log.v("dzq","destroy")
    }
}