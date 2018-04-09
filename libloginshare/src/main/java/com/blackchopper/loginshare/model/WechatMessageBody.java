package com.blackchopper.loginshare.model;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : LoginShare
 */

public class WechatMessageBody {
    public static final int WECHAT = 1;
    public static final int TIMELINE = 2;
    public static final int COLLECTION = 3;
    public static final int MSG_TEXT = 4;
    public static final int MSG_IMAGE = 5;
    public static final int MSG_MUSIC = 6;
    public static final int MSG_VIDEO = 7;
    public static final int MSG_WEB = 8;

    public Integer type;
    public Integer msgType;

    private String text;
    private String image;
    private String musicUrl;
    private String videoUrl;
    private String description;
    private String webpageUrl;
    private String title;


    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getWebpageUrl() {
        return webpageUrl;
    }

    public WechatMessageBody type(int type) {
        this.type = type;
        return this;
    }

    public WechatMessageBody msgType(int msgType) {
        this.msgType = msgType;
        return this;
    }

    public WechatMessageBody text(String text) {
        this.text = text;
        return this;
    }

    public WechatMessageBody title(String title) {
        this.title = title;
        return this;
    }

    public WechatMessageBody webpageUrl(String webpageUrl) {
        this.webpageUrl = webpageUrl;
        return this;
    }

    public WechatMessageBody videoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public WechatMessageBody description(String description) {
        this.description = description;
        return this;
    }

    public WechatMessageBody musicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
        return this;
    }

    public WechatMessageBody image(String image) {
        this.image = image;
        return this;
    }


    public WechatMessageBody build() {
        if (type == null | msgType == null) {
            try {
                throw new Exception("WechatMessageBody build error !");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (msgType == MSG_TEXT) {
            if (text == null) {
                try {
                    throw new Exception("WechatMessageBody build error !");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (msgType == MSG_IMAGE) {
            if (image == null) {
                try {
                    throw new Exception("WechatMessageBody build error !");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (msgType == MSG_MUSIC) {
            if (image == null | musicUrl == null | text == null | description == null | title == null) {
                try {
                    throw new Exception("WechatMessageBody build error !");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (msgType == MSG_VIDEO) {
            if (image == null | videoUrl == null | text == null | description == null) {
                try {
                    throw new Exception("WechatMessageBody build error !");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (msgType == MSG_WEB) {
            if (image == null | webpageUrl == null | title == null | description == null) {
                try {
                    throw new Exception("WechatMessageBody build error !");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    public String getTitle() {
        return title;
    }
}
