package com.blackchopper.loginshare.change;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.WeiboAppManager;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAppInfo;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.AidTask;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.NetworkHelper;
import com.sina.weibo.sdk.utils.SecurityHelper;
import com.sina.weibo.sdk.utils.UIUtils;
import com.sina.weibo.sdk.utils.Utility;
import com.sina.weibo.sdk.web.WebRequestType;
import com.sina.weibo.sdk.web.WeiboCallbackManager;
import com.sina.weibo.sdk.web.WeiboSdkWebActivity;
import com.sina.weibo.sdk.web.param.AuthWebViewRequestParam;

/**
 * Author：alilettter
 * Github: http://github.com/aliletter
 * Email: 4884280@qq.com
 * data: 2017/12/11
 */

public class BaseHandler {

    protected Fragment mAuthFragment;
    protected WbAuthListener authListener;
    protected static final String OAUTH2_BASE_URL = "https://open.weibo.cn/oauth2/authorize?";
    protected final int SSO_TYPE_INVALID = 3;
    protected int ssoRequestCode = -1;
    protected int ssoRequestType = 3;

    public BaseHandler(Fragment activity) {
        this.mAuthFragment = activity;
        AidTask.getInstance(this.mAuthFragment.getContext()).aidTaskInit(WbSdk.getAuthInfo().getAppKey());
    }

    public void authorize(WbAuthListener listener) {
        this.authorize('胍', listener, BaseHandler.AuthType.ALL);
    }

    public void authorizeClientSso(WbAuthListener listener) {
        this.authorize('胍', listener, BaseHandler.AuthType.SsoOnly);
    }

    public void authorizeWeb(WbAuthListener listener) {
        this.authorize('胍', listener, BaseHandler.AuthType.WebOnly);
    }

    private void authorize(int requestCode, WbAuthListener listener, BaseHandler.AuthType authType) {
        this.resetIntentFillData();
        if (listener == null) {
            throw new RuntimeException("please set auth listener");
        } else {
            this.authListener = listener;
            if (authType == BaseHandler.AuthType.WebOnly) {
                if (listener != null) {
                    this.startWebAuth();
                }

            } else {
                boolean onlyClientSso = false;
                if (authType == BaseHandler.AuthType.SsoOnly) {
                    onlyClientSso = true;
                }

                if (this.isWbAppInstalled()) {
                    this.startClientAuth(requestCode);
                } else if (onlyClientSso) {
                    this.authListener.onFailure(new WbConnectErrorMessage());
                } else {
                    this.startWebAuth();
                }

            }
        }
    }

    protected void startClientAuth(int requestCode) {
        try {
            WbAppInfo wbAppInfo = WeiboAppManager.getInstance(this.mAuthFragment.getContext()).getWbAppInfo();
            Intent intent = new Intent();
            intent.setClassName(wbAppInfo.getPackageName(), wbAppInfo.getAuthActivityName());
            intent.putExtras(WbSdk.getAuthInfo().getAuthBundle());
            intent.putExtra("_weibo_command_type", 3);
            intent.putExtra("_weibo_transaction", String.valueOf(System.currentTimeMillis()));
            intent.putExtra("aid", Utility.getAid(this.mAuthFragment.getContext(), WbSdk.getAuthInfo().getAppKey()));
            if (!SecurityHelper.validateAppSignatureForIntent(this.mAuthFragment.getContext(), intent)) {
                return;
            }

            this.fillExtraIntent(intent, requestCode);

            try {
                this.mAuthFragment.startActivityForResult(intent, this.ssoRequestCode);
            } catch (Exception var5) {
                if (this.authListener != null) {
                    this.authListener.onFailure(new WbConnectErrorMessage());
                }

                this.couldNotStartWbSsoActivity();
            }
        } catch (Exception var6) {
            ;
        }

    }

    protected void fillExtraIntent(Intent intent, int requestCode) {
    }

    protected void resetIntentFillData() {
        this.ssoRequestCode = '胍';
    }

    protected void startWebAuth() {
        AuthInfo authInfo = WbSdk.getAuthInfo();
        WeiboParameters requestParams = new WeiboParameters(authInfo.getAppKey());
        requestParams.put("client_id", authInfo.getAppKey());
        requestParams.put("redirect_uri", authInfo.getRedirectUrl());
        requestParams.put("scope", authInfo.getScope());
        requestParams.put("response_type", "code");
        requestParams.put("version", "0041005000");
        requestParams.put("luicode", "10000360");
        Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(this.mAuthFragment.getContext());
        if (token != null && !TextUtils.isEmpty(token.getToken())) {
            requestParams.put("trans_token", token.getToken());
            requestParams.put("trans_access_token", token.getToken());
        }

        requestParams.put("lfid", "OP_" + authInfo.getAppKey());
        String aid = Utility.getAid(this.mAuthFragment.getContext(), authInfo.getAppKey());
        if (!TextUtils.isEmpty(aid)) {
            requestParams.put("aid", aid);
        }

        requestParams.put("packagename", authInfo.getPackageName());
        requestParams.put("key_hash", authInfo.getKeyHash());
        String url = "https://open.weibo.cn/oauth2/authorize?" + requestParams.encodeUrl();
        if (!NetworkHelper.hasInternetPermission(this.mAuthFragment.getContext())) {
            UIUtils.showAlert(this.mAuthFragment.getContext(), "Error", "Application requires permission to access the Internet");
        } else {
            String mAuthListenerKey = null;
            if (this.authListener != null) {
                WeiboCallbackManager manager = WeiboCallbackManager.getInstance();
                mAuthListenerKey = manager.genCallbackKey();
                manager.setWeiboAuthListener(mAuthListenerKey, this.authListener);
            }

            AuthWebViewRequestParam param = new AuthWebViewRequestParam(authInfo, WebRequestType.AUTH, mAuthListenerKey, "微博登录", url, this.mAuthFragment.getContext());
            Intent intent = new Intent(this.mAuthFragment.getContext(), WeiboSdkWebActivity.class);
            Bundle bundle = new Bundle();
            param.fillBundle(bundle);
            intent.putExtras(bundle);
            this.mAuthFragment.startActivity(intent);
        }

    }

    public void authorizeCallBack(int requestCode, int resultCode, Intent data) {
        if ('胍' == requestCode) {
            if (resultCode == -1) {
                if (!SecurityHelper.checkResponseAppLegal(this.mAuthFragment.getContext(), WeiboAppManager.getInstance(this.mAuthFragment.getContext()).getWbAppInfo(), data)) {
                    this.authListener.onFailure(new WbConnectErrorMessage("your install weibo app is counterfeit", "8001"));
                    return;
                }

                String error = Utility.safeString(data.getStringExtra("error"));
                String error_type = Utility.safeString(data.getStringExtra("error_type"));
                String error_description = Utility.safeString(data.getStringExtra("error_description"));
                LogUtil.d("WBAgent", "error: " + error + ", error_type: " + error_type + ", error_description: " + error_description);
                if (TextUtils.isEmpty(error) && TextUtils.isEmpty(error_type) && TextUtils.isEmpty(error_description)) {
                    Bundle bundle = data.getExtras();
                    Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(bundle);
                    if (accessToken != null && accessToken.isSessionValid()) {
                        LogUtil.d("WBAgent", "Login Success! " + accessToken.toString());
                        AccessTokenKeeper.writeAccessToken(this.mAuthFragment.getContext(), accessToken);
                        this.authListener.onSuccess(accessToken);
                    }
                } else if (!"access_denied".equals(error) && !"OAuthAccessDeniedException".equals(error)) {
                    LogUtil.d("WBAgent", "Login failed: " + error);
                    this.authListener.onFailure(new WbConnectErrorMessage(error_type, error_description));
                } else {
                    LogUtil.d("WBAgent", "Login canceled by user.");
                    this.authListener.cancel();
                }
            } else if (resultCode == 0) {
                if (data != null) {
                    this.authListener.cancel();
                } else {
                    this.authListener.cancel();
                }
            }
        }

    }

    /**
     * @deprecated
     */
    @Deprecated
    public boolean isWbAppInstalled() {
        return WbSdk.isWbInstall(this.mAuthFragment.getContext());
    }

    protected void couldNotStartWbSsoActivity() {
    }

    protected static enum AuthType {
        ALL,
        SsoOnly,
        WebOnly;

        private AuthType() {
        }
    }
}
