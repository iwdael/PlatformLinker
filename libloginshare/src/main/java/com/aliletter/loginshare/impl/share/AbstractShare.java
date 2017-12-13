package com.aliletter.loginshare.impl.share;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aliletter.loginshare.impl.login.Login;
import com.aliletter.loginshare.model.QQMessageBody;
import com.aliletter.loginshare.model.Type;
import com.aliletter.loginshare.model.WechatMessageBody;
import com.aliletter.loginshare.model.WeiboMessageBody;
import com.sina.weibo.sdk.api.StoryMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;

/**
 * Author：alilettter
 * Github: http://github.com/aliletter
 * Email: 4884280@qq.com
 * data: 2017/12/12
 */

  abstract class AbstractShare extends Login implements WbShareCallback {

    public AbstractShare(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void launchWeiboShare(WeiboMessageBody weiboMessageBody) {
        super.launchWeiboShare(weiboMessageBody);
        if (weiboMessageBody.type == WeiboMessageBody.WEIBO)
            shareHandler.shareMessage(buildWeiboMultiMessage(weiboMessageBody), false);
        else
            shareHandler.shareToStory(buildStoryMessage(weiboMessageBody));


    }

    protected abstract StoryMessage buildStoryMessage(WeiboMessageBody weiboMessageBody);


    protected abstract WeiboMultiMessage buildWeiboMultiMessage(WeiboMessageBody weiboMessageBody);


    @Override
    public void launchQQShare(QQMessageBody qqMessageBody) {
        super.launchQQShare(qqMessageBody);
        Bundle bundle = new Bundle();
        if (qqMessageBody.type == QQMessageBody.QQChat) {
            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            bundle.putString(QQShare.SHARE_TO_QQ_TITLE, qqMessageBody.getTitle());
            bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, qqMessageBody.getSummary());
            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, qqMessageBody.getTargetUrl());
            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, qqMessageBody.getImageUrl());
            bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, appInfo.loadLabel(packageManager).toString());
            tencent.shareToQQ(activity, bundle, this);
        } else {
            bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, qqMessageBody.getTitle());//必填
            bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, qqMessageBody.getSummary());//选填
            bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, qqMessageBody.getTargetUrl());//必填
            bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, qqMessageBody.getImagesUtl());
            tencent.shareToQzone(activity, bundle, this);
        }
    }

    @Override
    public void launchWechatShare(WechatMessageBody wechatMessageBody) {
        super.launchWechatShare(wechatMessageBody);
        iWXAPI.sendReq(analyzeWeChatMessageBody(wechatMessageBody));
    }

    protected abstract BaseReq analyzeWeChatMessageBody(WechatMessageBody wechatMessageBody);


    @Override
    public void onReq(BaseReq baseReq) {
        super.onReq(baseReq);
    }

    @Override
    public void onResp(BaseResp resp) {
        super.onResp(resp);
        if (listener == null) return;
        if (resp.getType() != 1) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    listener.onShareSuccess(Type.Wechat);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    listener.onShareCancel(Type.Wechat);
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    listener.onShareError(Type.Wechat, BaseResp.ErrCode.ERR_AUTH_DENIED);
                    break;
                case BaseResp.ErrCode.ERR_UNSUPPORT:
                    listener.onShareError(Type.Wechat, BaseResp.ErrCode.ERR_UNSUPPORT);
                    break;
                default:
                    listener.onShareError(Type.Wechat, -1);
                    break;
            }
        }
    }


    @Override
    protected WbShareCallback getWbShareCallback() {
        return this;
    }

    @Override
    public void onWbShareSuccess() {
        if (listener != null) {
            listener.onShareSuccess(Type.Weibo);
        }
    }

    @Override
    public void onWbShareCancel() {
        if (listener != null) {
            listener.onShareCancel(Type.Weibo);
        }
    }

    @Override
    public void onWbShareFail() {
        if (listener != null) {
            listener.onShareError(Type.Weibo, -1);
        }
    }
}
