package com.hacknife.authority.tencent

import androidx.appcompat.app.AppCompatActivity
import com.hacknife.authority.Auth
import com.hacknife.authority.AuthType
import com.hacknife.authority.Authorization
import com.hacknife.authority.compat.getProxyFragment
import com.hacknife.authority.compat.qq
import com.tencent.connect.UserInfo
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.json.JSONObject

class TencentAuthorization(
    private val activity: AppCompatActivity,
    auth: Auth
) : Authorization(auth), IUiListener {
    init {
        Tencent.setIsPermissionGranted(true)
    }

    private val tencent by lazy { Tencent.createInstance(activity.qq, activity.applicationContext) }
    override fun auth() {
        activity.getProxyFragment()
            ?.let { proxyFragment ->
                proxyFragment.setActivityResult { requestCode, resultCode, data ->
                    Tencent.onActivityResultData(requestCode, resultCode, data, IUiListener@ this)
                }
                tencent.login(proxyFragment, "all", IUiListener@ this)
            }
    }

    override fun onComplete(jsonObject: Any) {
        jsonObject as JSONObject
        if (jsonObject.has("openid")) {
            tencent.openId = jsonObject.getString("openid")
            tencent.setAccessToken(
                jsonObject.getString("access_token"),
                jsonObject.getString("expires_in")
            )
            UserInfo(activity.applicationContext, tencent.qqToken)
                .getUserInfo(IUiListener@ this)
        } else {
            jsonObject.put("openid", tencent.openId)
            jsonObject.put("access_token", tencent.accessToken)
            jsonObject.put("expires_in", tencent.expiresIn)
            auth.successListener?.invoke(AuthType.QQ,jsonObject.toString())
        }
    }

    override fun onCancel() {
        auth.cancelListener?.invoke(AuthType.QQ)
    }

    override fun onWarning(p0: Int) {

    }

    override fun onError(p0: UiError?) {
        auth.errorListener?.invoke(AuthType.QQ,p0)
    }
}