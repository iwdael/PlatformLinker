package com.blackchopper.loginshare.change;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Author：alilettter
 * Github: http://github.com/aliletter
 * Email: 4884280@qq.com
 * data: 2017/12/11
 */

public class SsoHandler extends BaseHandler {

    public SsoHandler(Fragment activity) {
        super(activity);
    }

    private static final int REQUEST_CODE_GET_USER_INFO = 32974;
    private static final String EXTRA_REQUEST_CODE = "com.sina.weibo.intent.extra.REQUEST_CODE";


    protected void fillExtraIntent(Intent intent, int requestCode) {
        super.fillExtraIntent(intent, requestCode);
        if (requestCode == '胎') {
            intent.putExtra("com.sina.weibo.intent.extra.REQUEST_CODE", requestCode);
        }

    }

    protected void resetIntentFillData() {
        super.resetIntentFillData();
    }
}
