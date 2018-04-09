package com.blackchopper.loginshare.interfaces;

import android.content.Intent;

import com.blackchopper.loginshare.model.QQMessageBody;
import com.blackchopper.loginshare.model.WechatMessageBody;
import com.blackchopper.loginshare.model.WeiboMessageBody;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : LoginShare
 */

public interface ILoginShare {
    void launchQQLogin();

    void launchWechatLogin();

    void launchWeiboLogin();

    void launchQQShare(QQMessageBody qqMessageBody);

    void launchWeiboShare(WeiboMessageBody weiboMessageBody);

    void launchWechatShare(WechatMessageBody wechatMessageBody);

    void register(OnLoginshareListener listener);

    void unRegister();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onNewIntent(Intent intent);
}
