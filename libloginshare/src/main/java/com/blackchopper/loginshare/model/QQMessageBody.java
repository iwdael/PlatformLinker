package com.blackchopper.loginshare.model;

import java.util.ArrayList;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : LoginShare
 */

public class QQMessageBody {
    public static int QQChat = 1;
    public static int QZone = 2;
    public Integer type;
    protected String title;
    protected String summary;
    protected String targetUrl;
    protected String imageUrl;
    protected ArrayList<String> imagesUtl;

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ArrayList<String> getImagesUtl() {
        return imagesUtl;
    }

    public QQMessageBody() {
    }

    public QQMessageBody type(int type) {
        this.type = type;
        return this;
    }

    public QQMessageBody title(String title) {
        this.title = title;
        return this;
    }

    public QQMessageBody summary(String summary) {
        this.summary = summary;
        return this;
    }

    public QQMessageBody targetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
        return this;
    }

    public QQMessageBody imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public QQMessageBody imagesUtl(ArrayList<String> imagesUtl) {
        this.imagesUtl = imagesUtl;
        return this;
    }

    public QQMessageBody build() {
        if (type == null) {
            try {
                throw new Exception("QQMessageBody build error !");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (type == QQChat & (title == null | summary == null | targetUrl == null | imageUrl == null)) {
            try {
                throw new Exception("QQMessageBody build error !");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (type == QZone & (title == null | summary == null | targetUrl == null | imagesUtl == null)) {
            try {
                throw new Exception("QQMessageBody build error !");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }
}
