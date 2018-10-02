package com.hacknife.loginsharepay.interfaces;

import android.content.Intent;

import com.hacknife.loginsharepay.model.QQMessageBody;
import com.hacknife.loginsharepay.model.WechatMessageBody;
import com.hacknife.loginsharepay.model.WeiboMessageBody;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : LoginShare
 */

public interface ILoginSharePay {
    void launchQQLogin();

    void launchWechatLogin();

    void launchWeiboLogin();

    void payWechat(String partnerId,String prepayId,String nonceStr,String timeStamp,String sign);

    void launchQQShare(QQMessageBody qqMessageBody);

    void launchWeiboShare(WeiboMessageBody weiboMessageBody);

    void launchWechatShare(WechatMessageBody wechatMessageBody);

    void register(OnLoginSharePayListener listener);

    void unRegister();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onNewIntent(Intent intent);
}
