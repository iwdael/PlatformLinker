package com.hacknife.authority.wechat

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hacknife.authority.Auth
import com.hacknife.authority.AuthType
import com.hacknife.authority.Authorization
import com.hacknife.authority.compat.*
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.json.JSONObject


/**
 * author : 段泽全(hacknife)
 * e-mail : hacknife@outlook.com
 * time   : 2019/8/5
 * desc   : MVVM
 * version: 1.0
 */
class WechatAuthorization(private val activity: AppCompatActivity, auth: Auth) :
    Authorization(auth), IWXAPIEventHandler {
    companion object {
        var HEANDLER: IWXAPIEventHandler? = null
    }

    private val api by lazy {
        HEANDLER = WechatAuthority@ this
        WXAPIFactory
            .createWXAPI(activity.applicationContext, activity.wechat)
            .apply {
                registerApp(activity.wechat)
                activity.getProxyFragment()?.setDestroy {
//                    Log.v("dzq","destroy handler")
                    HEANDLER = null
                }
            }
    }

    override fun auth() {
        api.registerApp(activity.wechat)
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state = activity.packageName
        api.sendReq(req)
    }

    override fun onResp(baseResp: BaseResp) {
        if (baseResp.type == ConstantsAPI.COMMAND_SENDAUTH) {
            baseResp as SendAuth.Resp
            if (baseResp.errCode == 0) {

                if (activity.wechatSecret == null) {
                    val json = JSONObject()
                    json.put("authResult", baseResp.authResult)
                    json.put("code", baseResp.code)
                    json.put("country", baseResp.country)
                    json.put("lang", baseResp.lang)
                    json.put("url", baseResp.url)
                    json.put("state", baseResp.state)
                    json.put("errCode", baseResp.errCode)
                    json.put("errStr", baseResp.errStr)
                    json.put("openId", baseResp.openId)
                    json.put("transaction", baseResp.transaction)
                    if (baseResp.errCode == 0)
                        auth.successListener?.invoke(AuthType.Wechat, json.toString())
                    else
                        auth.errorListener?.invoke(AuthType.Wechat, json.toString())
                } else {
                    WECHAT_ACCESS_TOKEN.execute(
                        mapOf<String, String>(
                            "appid" to activity.wechat!!,
                            "secret" to activity.wechatSecret!!,
                            "code" to baseResp.code,
                            "grant_type" to "authorization_code"
                        )
                    ) { oauth2 ->
                        if (oauth2?.isNotEmpty() == true && oauth2.contains("access_token")) {
                            val jsonAuto = JSONObject(oauth2)
                            WECHAT_USERINFO.execute(
                                mapOf(
                                    "access_token" to jsonAuto.getString("access_token"),
                                    "openid" to jsonAuto.getString("openid")
                                )
                            ) { info ->
                                if (info?.isNotEmpty() == true) {
                                    val jsonInfo = JSONObject(info)
                                    jsonInfo.put("access_token", jsonAuto.getString("access_token"))
                                    auth.successListener?.invoke(AuthType.Wechat, jsonInfo.toString())
                                } else {
                                    auth.errorListener?.invoke(AuthType.Wechat, null)
                                }
                            }
                        } else {
                            auth.errorListener?.invoke(AuthType.Wechat, oauth2)
                        }
                    }
                }

            } else {
                auth.errorListener?.invoke(AuthType.Wechat, null)
            }
        }

    }

    override fun onReq(p0: BaseReq?) {
        Log.v("dzq", "${p0}")
    }
}