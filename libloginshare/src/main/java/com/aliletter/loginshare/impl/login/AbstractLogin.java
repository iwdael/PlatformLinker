package com.aliletter.loginshare.impl.login;

import android.support.v7.app.AppCompatActivity;

import com.sina.weibo.sdk.auth.WbAuthListener;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.aliletter.loginshare.impl.BaseLoginShare;

/**
 * Author：alilettter
 * Github: http://github.com/aliletter
 * Email: 4884280@qq.com
 * data: 2017/12/11
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
