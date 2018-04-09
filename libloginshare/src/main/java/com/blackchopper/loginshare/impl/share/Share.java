package com.blackchopper.loginshare.impl.share;

import android.support.v7.app.AppCompatActivity;

import com.blackchopper.loginshare.model.WechatMessageBody;
import com.tencent.mm.opensdk.modelbase.BaseReq;


/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : LoginShare
 */

  abstract class Share extends AbstractShare {
    public Share(AppCompatActivity activity) {
        super(activity);
    }




    @Override
    protected BaseReq analyzeWeChatMessageBody(WechatMessageBody wechatMessageBody) {
        BaseReq req = null;
        switch (wechatMessageBody.msgType) {
            case WechatMessageBody.MSG_TEXT:
                req = buildTextWeChatMessageBody(wechatMessageBody);
                break;
            case WechatMessageBody.MSG_IMAGE:
                req = buildImageWeChatMessageBody(wechatMessageBody);
                break;
            case WechatMessageBody.MSG_MUSIC:
                req = buildMusicWeChatMessageBody(wechatMessageBody);
                break;
            case WechatMessageBody.MSG_VIDEO:
                req = buildVideoWeChatMessageBody(wechatMessageBody);
                break;
            case WechatMessageBody.MSG_WEB:
                req = buildWebWeChatMessageBody(wechatMessageBody);
                break;
        }
        return req;
    }

    protected abstract BaseReq buildWebWeChatMessageBody(WechatMessageBody wechatMessageBody);

    protected abstract BaseReq buildVideoWeChatMessageBody(WechatMessageBody wechatMessageBody);

    protected abstract BaseReq buildMusicWeChatMessageBody(WechatMessageBody wechatMessageBody);

    protected abstract BaseReq buildImageWeChatMessageBody(WechatMessageBody wechatMessageBody);

    protected abstract BaseReq buildTextWeChatMessageBody(WechatMessageBody wechatMessageBody);
}
