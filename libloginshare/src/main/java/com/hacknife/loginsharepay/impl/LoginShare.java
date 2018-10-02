package com.hacknife.loginsharepay.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.hacknife.loginsharepay.impl.share.ShareImpl;
import com.hacknife.loginsharepay.model.WechatMessageBody;
import com.hacknife.loginsharepay.model.WeiboMessageBody;
import com.hacknife.loginsharepay.util.Util;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.utils.Utility;

import java.io.File;

import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneFavorite;
import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;
import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline;


/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : LoginShare
 */

public abstract class LoginShare extends ShareImpl {

    public LoginShare(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    protected VideoSourceObject getVideoObject(WeiboMessageBody weiboMessageBody) {
        VideoSourceObject videoSourceObject = new VideoSourceObject();
        videoSourceObject.videoPath = Uri.fromFile(new File(weiboMessageBody.getVideoPath()));
        return videoSourceObject;
    }

    @Override
    protected WebpageObject getMediaObject(WeiboMessageBody weiboMessageBody) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = weiboMessageBody.getTitle();
        mediaObject.description = weiboMessageBody.getDescription();
         // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        Bitmap bmp = BitmapFactory.decodeFile(weiboMessageBody.getLocalImage());
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, Util.THUMB_SIZE, Util.THUMB_SIZE, true);
        if (!bmp.equals(thumbBmp))
            bmp.recycle();
        mediaObject.setThumbImage(thumbBmp);
        mediaObject.actionUrl = weiboMessageBody.getActionUrl();
        mediaObject.defaultText = "Webpage 默认文案";
        return mediaObject;
    }

    @Override
    protected MultiImageObject getMultiImageObject(WeiboMessageBody weiboMessageBody) {
        MultiImageObject multiImageObject = new MultiImageObject();
        multiImageObject.setImageList(weiboMessageBody.getImagesPath());
        return multiImageObject;
    }

    @Override
    protected ImageObject getImageObject(WeiboMessageBody weiboMessageBody) {
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeFile(weiboMessageBody.getLocalImage());
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    @Override
    protected TextObject getTextObject(WeiboMessageBody weiboMessageBody) {
        TextObject textObject = new TextObject();
        textObject.text = weiboMessageBody.getText();
        textObject.title = weiboMessageBody.getTitle();
        textObject.actionUrl = weiboMessageBody.getActionUrl();
        return textObject;
    }

    @Override

    protected int getWecahtTargetScene(WechatMessageBody wechatMessageBody) {
        int scene = 0;
        switch (wechatMessageBody.type) {
            case WechatMessageBody.WECHAT:
                scene = WXSceneSession;
                break;
            case WechatMessageBody.COLLECTION:
                scene = WXSceneFavorite;
                break;
            case WechatMessageBody.TIMELINE:
                scene = WXSceneTimeline;
                break;
        }
        return scene;
    }


}

