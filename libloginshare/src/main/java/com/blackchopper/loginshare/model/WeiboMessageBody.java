package com.blackchopper.loginshare.model;

import android.net.Uri;

import java.util.ArrayList;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : LoginShare
 */

public class WeiboMessageBody {
    public static final int WEIBO = 0;
    public static final int WEIBO_STORY = -1;
    public static final int MSG_TEXT_IAMGE = 1;
    public static final int MSG_VIDEO = 2;
    public static final int MSG_WEB = 3;
    public Integer type;
    public Integer msgType;


    private String text;
    private String actionUrl;
    private String title;
    private String imageUrl;
    private String description;
    private ArrayList<Uri> imagesPath;
    private String videoPath;
    private String localImage;

    public String getLocalImage() {
        return localImage;
    }

    public void setLocalImage(String localImage) {
        this.localImage = localImage;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ArrayList<Uri> getImagesPath() {
        return imagesPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public String getDescription() {
        return description;
    }

    public WeiboMessageBody text(String text) {
        this.text = text;
        return this;
    }

    public WeiboMessageBody description(String description) {
        this.description = description;
        return this;
    }

    public WeiboMessageBody actionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
        return this;
    }

    public WeiboMessageBody title(String title) {
        this.title = title;
        return this;
    }

    public WeiboMessageBody type(int type) {
        this.type = type;
        return this;
    }

    public WeiboMessageBody msgType(int msgType) {
        this.msgType = msgType;
        return this;
    }

    public WeiboMessageBody imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public WeiboMessageBody imagesPath(ArrayList<Uri> text) {
        this.imagesPath = imagesPath;
        return this;
    }

    public WeiboMessageBody videoPath(String videoPath) {
        this.videoPath = videoPath;
        return this;
    }

    public WeiboMessageBody build() {
        if (msgType == null | type == null) {
            try {
                throw new Exception("WeiboMessageBody build error !");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (type == WEIBO) {
            if (msgType == MSG_TEXT_IAMGE && text == null && imageUrl == null && imagesPath == null) {
                try {
                    throw new Exception("WeiboMessageBody build error !");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (msgType == MSG_VIDEO) {
                if (videoPath == null) {
                    try {
                        throw new Exception("WeiboMessageBody build error !");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (msgType == MSG_WEB) {
                if (actionUrl == null | title == null | imageUrl == null | description == null) {
                    if (videoPath == null) {
                        try {
                            throw new Exception("WeiboMessageBody build error !");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } else {
            if (msgType == MSG_VIDEO) {
                if (videoPath == null) {
                    try {
                        throw new Exception("WeiboMessageBody build error !");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (imageUrl == null) {
                    try {
                        throw new Exception("WeiboMessageBody build error !");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (text != null) {
            if (actionUrl == null | title == null) {
                try {
                    throw new Exception("WeiboMessageBody build error !");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

}
