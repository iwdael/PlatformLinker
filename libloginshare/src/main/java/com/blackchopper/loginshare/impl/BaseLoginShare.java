package com.blackchopper.loginshare.impl;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.blackchopper.loginshare.change.SsoHandler;
import com.blackchopper.loginshare.change.WbShareHandler;
import com.blackchopper.loginshare.constant.Config;
import com.blackchopper.loginshare.interfaces.ILogonShare;
import com.blackchopper.loginshare.interfaces.OnLoginshareListener;
import com.blackchopper.loginshare.messager.Messager;
import com.blackchopper.loginshare.model.QQMessageBody;
import com.blackchopper.loginshare.model.WechatMessageBody;
import com.blackchopper.loginshare.model.WeiboMessageBody;
import com.blackchopper.loginshare.proxy.ProxyFragment;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import static com.blackchopper.loginshare.constant.Config.TAG;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : LoginShare
 */

public abstract class BaseLoginShare implements ILogonShare {
    protected AppCompatActivity activity;

    //替身Fragment
    protected ProxyFragment proxyFragment;
    //qq
    protected Tencent tencent;
    //weibo
    protected SsoHandler ssoHandler;
    protected WbShareHandler shareHandler;
    //wechat
    protected IWXAPI iWXAPI;
    protected OnLoginshareListener listener;
    //获取Key,secret，scope等。
    protected ApplicationInfo appInfo;
    protected PackageManager packageManager;
    //qq的分享与登陆共用一个回调接口，加一个字段区分当前接口的接收类型
    protected boolean isQQLogin = false;
    protected String qqValue;
    protected String wechatValue;
    protected String wechatSecret;
    protected String weiboValue;
    protected String weiboRedirectUrl;
    protected String weiboScope;


    public BaseLoginShare(AppCompatActivity activity) {
        this.activity = activity;
        try {
            packageManager = this.activity.getPackageManager();
            appInfo = packageManager.getApplicationInfo(this.activity.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (appInfo == null) {
            try {
                throw new Exception("Appinfo is not access !");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        proxyFragment = getFragment(this.activity);

        qqValue = String.valueOf(appInfo.metaData.get(Config.qq));
        wechatValue = String.valueOf(appInfo.metaData.get(Config.wechat));
        wechatSecret = String.valueOf(appInfo.metaData.get(Config.wechatSecret));
        weiboValue = String.valueOf(appInfo.metaData.get(Config.weibo));
        weiboRedirectUrl = String.valueOf(appInfo.metaData.get(Config.weiboRedirectUrl));
        weiboScope = String.valueOf(appInfo.metaData.get(Config.weiboScope));
        if (qqValue.equals("null")) {
            Log.v(TAG, "qq meta-data is not access !");
        }
        if (wechatValue.equals("null")) {
            Log.v(TAG, "wechat meta-data is not access !");
        }
        if (wechatSecret.equals("null")) {
            Log.v(TAG, "wechatSecret meta-data is not access !");
        }
        if (weiboValue.equals("null")) {
            Log.v(TAG, "weibo meta-data is not access !");
        }
        if (weiboRedirectUrl.equals("null")) {
            Log.v(TAG, "weiboRedirectUrl meta-data is not access !");
        }
        if (weiboScope.equals("null")) {
            Log.v(TAG, "weiboScope meta-data is not access !");
        }
    }

    @Override
    public void launchQQLogin() {
        isQQLogin = true;
        if (tencent == null)
            tencent = Tencent.createInstance(qqValue, proxyFragment.getContext());
    }

    @Override
    public void launchWechatLogin() {
        if (iWXAPI == null) {
            iWXAPI = WXAPIFactory.createWXAPI(proxyFragment.getContext(), wechatValue);
            iWXAPI.registerApp(wechatValue);
        }
    }

    @Override
    public void launchWeiboLogin() {

        WbSdk.install(proxyFragment.getContext(), new AuthInfo(proxyFragment.getContext(), weiboValue, weiboRedirectUrl, weiboScope));

        if (ssoHandler == null) {
            ssoHandler = new SsoHandler(proxyFragment);
        }
    }

    @Override
    public void launchQQShare(QQMessageBody qqMessageBody) {
        isQQLogin = false;
        if (tencent == null)
            tencent = Tencent.createInstance(qqValue, proxyFragment.getContext());
    }

    @Override
    public void launchWeiboShare(WeiboMessageBody weiboMessageBody) {
        WbSdk.install(proxyFragment.getContext(), new AuthInfo(proxyFragment.getContext(), weiboValue, weiboRedirectUrl, weiboScope));
        shareHandler = new WbShareHandler(proxyFragment.getContext());
        shareHandler.registerApp();
    }

    @Override
    public void launchWechatShare(WechatMessageBody wechatMessageBody) {
        if (iWXAPI == null) {
            iWXAPI = WXAPIFactory.createWXAPI(proxyFragment.getContext(), wechatValue);
            iWXAPI.registerApp(wechatValue);
        }
    }

    @Override
    public void register(OnLoginshareListener listener) {
        this.listener = listener;
        Messager.getInstance().register(this);
    }

    @Override
    public void unRegister() {
        Messager.getInstance().unRegister(this);
        removeFragment(activity);
        this.listener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //QQ登录的回调
        if (tencent != null) {
            tencent.onActivityResultData(requestCode, resultCode, data, getIUIListener());
        }
        //微博登录的回调
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        shareHandler.doResultIntent(intent, getWbShareCallback());
    }

    private void removeFragment(AppCompatActivity activity) {
        ProxyFragment proxyFragment = findFragment(activity);
        boolean isNewInstance = proxyFragment == null;
        if (!isNewInstance) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(proxyFragment).commitAllowingStateLoss();
        }
    }

    private ProxyFragment getFragment(AppCompatActivity activity) {
        ProxyFragment proxyFragment = findFragment(activity);
        boolean isNewInstance = proxyFragment == null;
        if (isNewInstance) {
            proxyFragment = new ProxyFragment(this);
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction
                    .add(proxyFragment, Config.TAG)
                    .commitAllowingStateLoss();
            //    fragmentTransaction.commit();
        }
        return proxyFragment;
    }

    private ProxyFragment findFragment(AppCompatActivity activity) {
        return (ProxyFragment) activity.getSupportFragmentManager().findFragmentByTag(Config.TAG);
    }

    public Tencent getTencent() {
        return tencent;
    }

    public abstract IUiListener getIUIListener();

    public abstract SsoHandler getSsoHandler();

    protected abstract WbShareCallback getWbShareCallback();
}
