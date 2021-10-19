package com.hacknife.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.hacknife.authority.Auth
import com.hacknife.authority.AuthType

class MainActivity : AppCompatActivity() {
    private val auth by lazy { Auth.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth.setSuccessListener { type, json ->
            Log.v("dzq", "success ${type} ${json}")
        }
        auth.setCancelListener {
            Log.v("dzq", "cancel ${it}")
        }
        auth.setErrorListener { type, json ->
            Log.v("dzq", "error ${type} ${json}")
        }
    }

    fun onTencentClick(view: View) {
        auth.auth(AuthType.QQ)
    }

    fun onWechatClick(view: View) {
        auth.auth(AuthType.Wechat)
    }
}