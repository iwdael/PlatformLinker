package com.iwdael.platformlinker

import android.content.Intent
import android.os.Bundle

class TencentAuthActivity : TransparentActivity() {
    companion object {
        const val BUNDLE_EXTRA_TYPE_IS_AUTHORIZE = "BUNDLE_EXTRA_TYPE_IS_AUTHORIZE"
        const val BUNDLE_EXTRA_SHARE_BUNDLE = "BUNDLE_EXTRA_BUNDLE"
        const val BUNDLE_EXTRA_SHARE_TYPE_IS_FRIEND = "BUNDLE_EXTRA_SHARE_TYPE_IS_FRIEND"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authType = intent.getBooleanExtra(BUNDLE_EXTRA_TYPE_IS_AUTHORIZE, true)
        PlatformLog.v("tencent-auth-activity", "authType:${authType}")
        if (authType) {
            TencentPlatform.instance.authorize()
        } else {
            TencentPlatform.instance.share(
                intent.getBundleExtra(BUNDLE_EXTRA_SHARE_BUNDLE)!!,
                intent.getBooleanExtra(BUNDLE_EXTRA_SHARE_TYPE_IS_FRIEND, true),
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        TencentPlatform.instance.activityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        finish()
    }
}