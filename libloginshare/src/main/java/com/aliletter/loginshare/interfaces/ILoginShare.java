package com.aliletter.loginshare.interfaces;

import android.content.Intent;

import com.aliletter.loginshare.model.QQMessageBody;
import com.aliletter.loginshare.model.WechatMessageBody;
import com.aliletter.loginshare.model.WeiboMessageBody;

/**
 * Author：alilettter
 * Github: http://github.com/aliletter
 * Email: 4884280@qq.com
 * data: 2017/12/11
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
