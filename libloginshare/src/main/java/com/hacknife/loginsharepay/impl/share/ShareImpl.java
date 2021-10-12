package com.hacknife.loginsharepay.impl.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import com.hacknife.loginsharepay.model.WechatMessageBody;
import com.hacknife.loginsharepay.model.WeiboMessageBody;
import com.hacknife.loginsharepay.util.Util;
//import com.sina.weibo.sdk.api.ImageObject;
//import com.sina.weibo.sdk.api.MultiImageObject;
//import com.sina.weibo.sdk.api.StoryMessage;
//import com.sina.weibo.sdk.api.TextObject;
//import com.sina.weibo.sdk.api.VideoSourceObject;
//import com.sina.weibo.sdk.api.WebpageObject;
//import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import java.io.File;


/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : LoginShare
 */

public abstract class ShareImpl extends Share {

    public ShareImpl(AppCompatActivity activity) {
        super(activity);
    }

//    @Override
//    protected WeiboMultiMessage buildWeiboMultiMessage(WeiboMessageBody weiboMessageBody) {
//        WeiboMultiMessage msg = new WeiboMultiMessage();
//        if (weiboMessageBody.getText() != null)
//            msg.textObject = getTextObject(weiboMessageBody);
//        switch (weiboMessageBody.msgType) {
//            case WeiboMessageBody.MSG_WEB:
//                msg.mediaObject = getMediaObject(weiboMessageBody);
//
//            case WeiboMessageBody.MSG_TEXT_IAMGE:
//                if (weiboMessageBody.getLocalImage() != null)
//                    msg.imageObject = getImageObject(weiboMessageBody);
//                if (weiboMessageBody.getImagesPath() != null)
//                    msg.multiImageObject = getMultiImageObject(weiboMessageBody);
//                break;
//
//            case WeiboMessageBody.MSG_VIDEO:
//                if (weiboMessageBody.getVideoPath() != null)
//                    msg.videoSourceObject = getVideoObject(weiboMessageBody);
//                break;
//        }
//        return msg;
//    }
//
//    protected abstract WebpageObject getMediaObject(WeiboMessageBody weiboMessageBody);
//
//    protected abstract VideoSourceObject getVideoObject(WeiboMessageBody weiboMessageBody);
//
//    protected abstract MultiImageObject getMultiImageObject(WeiboMessageBody weiboMessageBody);
//
//    protected abstract ImageObject getImageObject(WeiboMessageBody weiboMessageBody);
//
//    protected abstract TextObject getTextObject(WeiboMessageBody weiboMessageBody);

//    @Override
//    protected StoryMessage buildStoryMessage(WeiboMessageBody weiboMessageBody) {
//        StoryMessage storyMessage = new StoryMessage();
//        switch (weiboMessageBody.msgType) {
//            case WeiboMessageBody.MSG_TEXT_IAMGE:
//                if (weiboMessageBody.getLocalImage() != null)
//                    storyMessage.setImageUri(Uri.fromFile(new File(weiboMessageBody.getLocalImage())));
//                break;
//            case WeiboMessageBody.MSG_VIDEO:
//                if (weiboMessageBody.getVideoPath() != null)
//                    storyMessage.setVideoUri(Uri.fromFile(new File(weiboMessageBody.getVideoPath())));
//                break;
//        }
//        return storyMessage;
//    }

    @Override
    protected BaseReq buildWebWeChatMessageBody(WechatMessageBody wechatMessageBody) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = wechatMessageBody.getWebpageUrl();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = wechatMessageBody.getTitle();
        msg.description = wechatMessageBody.getDescription();
        Bitmap bmp = BitmapFactory.decodeFile(wechatMessageBody.getLocalImage());
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, Util.THUMB_SIZE, Util.THUMB_SIZE, true);
        if (!bmp.equals(thumbBmp))
            bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        req.transaction = Util.buildTransaction("webpage");
        req.message = msg;
        req.scene = getWecahtTargetScene(wechatMessageBody);
        return req;
    }

    @Override
    protected BaseReq buildVideoWeChatMessageBody(WechatMessageBody wechatMessageBody) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = wechatMessageBody.getVideoUrl();
        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = wechatMessageBody.getTitle();
        msg.description = wechatMessageBody.getDescription();
        Bitmap bmp = BitmapFactory.decodeFile(wechatMessageBody.getLocalImage());
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, Util.THUMB_SIZE, Util.THUMB_SIZE, true);
        if (!bmp.equals(thumbBmp))
            bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        req.transaction = Util.buildTransaction("video");
        req.message = msg;
        req.scene = getWecahtTargetScene(wechatMessageBody);
        return req;
    }

    @Override
    protected BaseReq buildMusicWeChatMessageBody(WechatMessageBody wechatMessageBody) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = wechatMessageBody.getMusicUrl();
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = wechatMessageBody.getTitle();
        msg.description = wechatMessageBody.getDescription();
        Bitmap bmp = BitmapFactory.decodeFile(wechatMessageBody.getLocalImage());
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, Util.THUMB_SIZE, Util.THUMB_SIZE, true);
        if (!bmp.equals(thumbBmp))
            bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        req.transaction = Util.buildTransaction("music");
        req.message = msg;
        req.scene = getWecahtTargetScene(wechatMessageBody);
        return req;
    }

    @Override
    protected BaseReq buildImageWeChatMessageBody(WechatMessageBody wechatMessageBody) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        Bitmap bmp = BitmapFactory.decodeFile(wechatMessageBody.getLocalImage());
        WXImageObject imgObj = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, Util.THUMB_SIZE, Util.THUMB_SIZE, true);
        if (!bmp.equals(thumbBmp))
            bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        req.transaction = Util.buildTransaction("img");
        req.message = msg;
        req.scene = getWecahtTargetScene(wechatMessageBody);
        return req;
    }


    @Override
    protected BaseReq buildTextWeChatMessageBody(WechatMessageBody wechatMessageBody) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        WXTextObject textObj = new WXTextObject();
        textObj.text = wechatMessageBody.getText();
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = wechatMessageBody.getDescription();
        req.transaction = Util.buildTransaction("text");
        req.message = msg;
        req.scene = getWecahtTargetScene(wechatMessageBody);
        return req;
    }

    protected abstract int getWecahtTargetScene(WechatMessageBody wechatMessageBody);
}
