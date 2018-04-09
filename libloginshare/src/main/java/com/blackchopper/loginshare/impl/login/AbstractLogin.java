package com.blackchopper.loginshare.impl.login;

import android.support.v7.app.AppCompatActivity;

import com.blackchopper.loginshare.impl.BaseLoginShare;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : LoginShare
 */

  abstract class AbstractLogin extends BaseLoginShare implements WbAuthListener, IUiListener {
    public AbstractLogin(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void launchWeiboLogin() {
        super.launchWeiboLogin();
        ssoHandler.authorize(this);
    }

    @Override
    public void launchQQLogin() {
        super.launchQQLogin();
        tencent.login(proxyFragment, "all", this);
    }

    @Override
    public void launchWechatLogin() {
        super.launchWechatLogin();
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = proxyFragment.getContext().getPackageName();
        iWXAPI.sendReq(req);
    }


}
