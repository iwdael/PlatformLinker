package com.hacknife.authority.proxy

import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * author : 段泽全(hacknife)
 * e-mail : hacknife@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */
class ProxyFragment : Fragment() {
    private var activityResult: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit)? = null
    private var destroy: (() -> Unit)? = null
    fun setActivityResult(result: (requestCode: Int, resultCode: Int, data: Intent?) -> Unit) {
        activityResult = result
    }

    fun setDestroy(l: (() -> Unit)) {
        destroy = l
    }

    companion object {
        const val TAG = "ProxyFragment"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResult?.invoke(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        destroy?.invoke()
    }
}