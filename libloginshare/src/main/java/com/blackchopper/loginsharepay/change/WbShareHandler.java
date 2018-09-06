package com.blackchopper.loginsharepay.change;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.WeiboAppManager;
import com.sina.weibo.sdk.api.StoryMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareToStoryActivity;
import com.sina.weibo.sdk.share.WbShareTransActivity;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.MD5;
import com.sina.weibo.sdk.utils.Utility;
import com.sina.weibo.sdk.web.WebRequestType;
import com.sina.weibo.sdk.web.param.ShareWebViewRequestParam;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : LoginShare
 */

public class WbShareHandler {
    private boolean hasRegister = false;
    private Context context;
    private int progressColor = -1;
    private int progressId = -1;

    public WbShareHandler(Context context) {
        this.context = context;
    }

    public boolean registerApp() {
        this.sendBroadcast(this.context, "com.sina.weibo.sdk.Intent.ACTION_WEIBO_REGISTER", WbSdk.getAuthInfo().getAppKey(), (String)null, (Bundle)null);
        this.hasRegister = true;
        return true;
    }

    private void sendBroadcast(Context context, String action, String key, String packageName, Bundle data) {
        Intent intent = new Intent(action);
        String appPackage = context.getPackageName();
        intent.putExtra("_weibo_sdkVersion", "0041005000");
        intent.putExtra("_weibo_appPackage", appPackage);
        intent.putExtra("_weibo_appKey", key);
        intent.putExtra("_weibo_flag", 538116905);
        intent.putExtra("_weibo_sign", MD5.hexdigest(Utility.getSign(context, appPackage)));
        if(!TextUtils.isEmpty(packageName)) {
            intent.setPackage(packageName);
        }

        if(data != null) {
            intent.putExtras(data);
        }

        context.sendBroadcast(intent, "com.sina.weibo.permission.WEIBO_SDK_PERMISSION");
    }

    public void shareMessage(WeiboMultiMessage message, boolean clientOnly) {
        if(!this.hasRegister) {
            throw new RuntimeException("please call WbShareHandler.registerApp(),before use share function");
        } else if(WbSdk.isWbInstall(this.context) || !clientOnly) {
            if(!clientOnly && !WbSdk.isWbInstall(this.context)) {
                this.startWebShare(message);
            } else {
                this.startClientShare(message);
            }

        }
    }

    public void shareToStory(StoryMessage storyMessage) {
        Intent intent = new Intent();
        intent.putExtra("_weibo_message_stroy", storyMessage);
        intent.putExtra("startActivity", this.context.getClass().getName());
        intent.putExtra("progressColor", this.progressColor);
        intent.putExtra("progressId", this.progressId);
        intent.setClass(this.context, WbShareToStoryActivity.class);
        this.context.startActivity(intent);
    }

    private void startClientShare(WeiboMultiMessage message) {
        Bundle data = new Bundle();
        data.putInt("_weibo_command_type", 1);
        data.putString("_weibo_transaction", String.valueOf(System.currentTimeMillis()));
        data.putLong("callbackId", 0L);
        data.putAll(message.toBundle(data));
        Intent intent = new Intent();
        intent.setClass(this.context, WbShareTransActivity.class);
        intent.putExtra("startPackage", WeiboAppManager.getInstance(this.context).getWbAppInfo().getPackageName());
        intent.putExtra("startAction", "com.sina.weibo.sdk.action.ACTION_WEIBO_ACTIVITY");
        intent.putExtra("startFlag", 0);
        intent.putExtra("startActivity", this.context.getClass().getName());
        intent.putExtra("progressColor", this.progressColor);
        intent.putExtra("progressId", this.progressId);
        if(data != null) {
            intent.putExtras(data);
        }

        try {
            this.context.startActivity(intent);
        } catch (Exception var5) {
            LogUtil.v("weibo sdk error ", var5.toString());
        }

    }

    private void startWebShare(WeiboMultiMessage message) {
        Intent webIntent = new Intent(this.context, WbShareTransActivity.class);
        String appPackage = this.context.getPackageName();
        ShareWebViewRequestParam webParam = new ShareWebViewRequestParam(WbSdk.getAuthInfo(), WebRequestType.SHARE, "", 1, "微博分享", (String)null, this.context);
        webParam.setContext(this.context);
        webParam.setHashKey("");
        webParam.setPackageName(appPackage);
        Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(this.context);
        if(token != null && !TextUtils.isEmpty(token.getToken())) {
            webParam.setToken(token.getToken());
        }

        webParam.setMultiMessage(message);
        Bundle bundle = new Bundle();
        webParam.fillBundle(bundle);
        webIntent.putExtras(bundle);
        webIntent.putExtra("startFlag", 0);
        webIntent.putExtra("startActivity", this.context.getClass().getName());
        webIntent.putExtra("startAction", "com.sina.weibo.sdk.action.ACTION_WEIBO_ACTIVITY");
        webIntent.putExtra("gotoActivity", "com.sina.weibo.sdk.web.WeiboSdkWebActivity");
        this.context.startActivity(webIntent);
    }

    /**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : LoginShare
 */
    @Deprecated
    public boolean isWbAppInstalled() {
        return WbSdk.isWbInstall(this.context);
    }

    public void doResultIntent(Intent intent, WbShareCallback callback) {
        if(callback != null) {
            Bundle bundle = intent.getExtras();
            if(bundle != null) {
                int resultCode = bundle.getInt("_weibo_resp_errcode");
                switch(resultCode) {
                    case 0:
                        callback.onWbShareSuccess();
                        break;
                    case 1:
                        callback.onWbShareCancel();
                        break;
                    case 2:
                        callback.onWbShareFail();
                }

            }
        }
    }

    public boolean supportMulti() {
        return false;
    }

    public void setProgressColor(int color) {
        this.progressColor = color;
    }

    public void setProgressId(int id) {
        this.progressId = id;
    }
}
