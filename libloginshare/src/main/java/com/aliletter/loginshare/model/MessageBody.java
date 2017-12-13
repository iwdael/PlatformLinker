package com.aliletter.loginshare.model;

/**
 * Author：alilettter
 * Github: http://github.com/aliletter
 * Email: 4884280@qq.com
 * data: 2017/12/11
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
