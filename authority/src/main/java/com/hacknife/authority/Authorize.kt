package com.hacknife.authority

import androidx.appcompat.app.AppCompatActivity
import com.hacknife.authority.tencent.TencentAuthorization
import com.hacknife.authority.wechat.WechatAuthorization

/**
 * author : 段泽全(hacknife)
 * e-mail : hacknife@outlook.com
 * time   : 2019/8/5
 * desc   : 授权
 * version: 1.0
 */
class Authorize(private val activity: AppCompatActivity) : Auth() {
    private val tencentAuth by lazy { TencentAuthorization(activity, this) }
    private val wechatAuth by lazy { WechatAuthorization(activity, this) }
    override fun auth(type: AuthType) {
        when (type) {
            AuthType.QQ -> tencentAuth.auth()
            AuthType.Wechat -> wechatAuth.auth()
        }
    }

}