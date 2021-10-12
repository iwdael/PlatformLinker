package com.hacknife.loginsharepay.impl.login;


import androidx.appcompat.app.AppCompatActivity;

import com.hacknife.loginsharepay.impl.BaseLoginShare;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : LoginShare
 */

  abstract class AbstractLogin extends BaseLoginShare implements IUiListener {
    public AbstractLogin(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void launchWeiboLogin() {
        super.launchWeiboLogin();
//        ssoHandler.authorize(this);
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
