package com.blackchopper.loginsharepay.impl.login;

import android.support.v7.app.AppCompatActivity;

import com.blackchopper.loginsharepay.change.SsoHandler;
import com.blackchopper.loginsharepay.model.Type;
import com.blackchopper.loginsharepay.net.LoginShareHttp;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.tencent.connect.UserInfo;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.blackchopper.loginsharepay.constant.Config;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : LoginShare
 */

public abstract class Login extends AbstractLogin {
    public Login(AppCompatActivity activity) {
        super(activity);
    }

    //微博
    @Override
    public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
        if (oauth2AccessToken.isSessionValid()) {
            Map<String, String> body = new HashMap<>();
            body.put("uid", oauth2AccessToken.getUid());
            body.put("access_token", oauth2AccessToken.getToken());
            new LoginShareHttp(Config.WEIBO_USERINFO_URL, body) {
                @Override
                public void onResult(int code, String result) {
                    if (listener == null) {
                        return;
                    }
                    if (code == 0) {
                        listener.onLoginSuccess(Type.Weibo, result);
                    } else {
                        listener.onLoginError(Type.Weibo, code);
                    }
                }
            };
        }
    }

    //微博
    @Override
    public void cancel() {
        if (listener != null)
            listener.onLoginCancel(Type.Weibo);
    }

    //微博
    @Override
    public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
        if (listener != null)
            listener.onLoginError(Type.Weibo, Integer.parseInt(wbConnectErrorMessage.getErrorCode()));
    }

    //qq
    @Override
    public void onComplete(Object o) {
        if (listener == null) return;
        if (!isQQLogin) {
            listener.onShareSuccess(Type.QQ);
            return;
        }
        JSONObject jsonObject = (JSONObject) o;
        try {
            if (!jsonObject.isNull("openid")) {
                String openid = jsonObject.getString("openid");
                String accessToken = jsonObject.getString("access_token");
                String expires = jsonObject.getString("expires_in");
                tencent.setOpenId(openid);
                tencent.setAccessToken(accessToken, expires);
                UserInfo userInfo = new UserInfo(proxyFragment.getContext(), tencent.getQQToken());
                userInfo.getUserInfo(this);
            } else {
                if (jsonObject.isNull("figureurl_qq_1")) return;
                jsonObject.put("openid", tencent.getOpenId());
                listener.onLoginSuccess(Type.QQ, jsonObject.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //qq
    @Override
    public void onError(UiError uiError) {
        if (listener == null) return;
        if (isQQLogin) listener.onLoginError(Type.QQ, uiError.errorCode);
        else
            listener.onShareError(Type.QQ, uiError.errorCode);
    }

    //qq
    @Override
    public void onCancel() {
        if (listener == null) return;
        if (isQQLogin)
            listener.onLoginCancel(Type.QQ);
        else
            listener.onShareCancel(Type.QQ);
    }


    //微信
    @Override
    public void onResp(BaseResp baseResp) {
        //等于1为登陆的回调，其他的默认为分享的回调。
        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            SendAuth.Resp res = (SendAuth.Resp) baseResp;
            if (res.errCode == 0) {
                //login success
                Map<String, String> body = new HashMap<>();
                body.put("appid", wechatValue);
                body.put("secret", wechatSecret);
                body.put("code", res.code);
                body.put("grant_type", "authorization_code");
                new LoginShareHttp(Config.WECHAT_ACCESS_TOKEN, body) {
                    @Override
                    public void onResult(int code, String result) {
                        if (listener == null) return;
                        if (code == 0) {
                            reqWechatInfo(result);
                        } else {
                            listener.onLoginError(Type.Wechat, code);
                        }
                    }
                };
            } else {
                //login fail 全部默认登录失败，没有登录取消这个说法
                if (listener != null)
                    listener.onLoginError(Type.Wechat, res.errCode);
            }
        }
    }


    //微信,这个方法好像并没有什么卵用
    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public IUiListener getIUIListener() {
        return this;
    }

    @Override
    public SsoHandler getSsoHandler() {
        return ssoHandler;
    }

    private void reqWechatInfo(String result) {
        try {
            JSONObject access = new JSONObject(result);
            Map<String, String> body = new HashMap<>();
            body.put("access_token", access.getString("access_token"));
            body.put("openid", access.getString("openid"));
            new LoginShareHttp(Config.WECHAT_USERINFO, body) {
                @Override
                public void onResult(int code, String result) {
                    if (listener == null) return;
                    if (code == 0) {
                        listener.onLoginSuccess(Type.Wechat, result);
                    } else {
                        listener.onLoginError(Type.Wechat, code);
                    }
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
