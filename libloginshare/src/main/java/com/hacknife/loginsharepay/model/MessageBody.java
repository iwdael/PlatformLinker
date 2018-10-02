package com.hacknife.loginsharepay.model;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : LoginShare
 */

public class MessageBody {
    public static QQMessageBody QQMessageBodyBuilder() {
        return new QQMessageBody();
    }

    public static WeiboMessageBody WeiboMessageBodyBuilder() {
        return new WeiboMessageBody();
    }

    public static WechatMessageBody WechatMessageBodyBuilder() {
        return new WechatMessageBody();
    }
}
