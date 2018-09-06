package com.blackchopper.example.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.blackchopper.loginsharepay.constant.Config;
import com.blackchopper.loginsharepay.messager.Messager;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * Author: aliletter
 * Github: http://github.com/aliletter
 * Data: 2017/9/6.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    private ApplicationInfo info;
    private String wecahtValue = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            info = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            wecahtValue = String.valueOf(info.metaData.get(Config.wechat));
            api = WXAPIFactory.createWXAPI(this, wecahtValue, false);
            api.handleIntent(getIntent(), this);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Messager.getInstance().sendReq(baseReq);
    }

    @Override
    public void onResp(BaseResp resp) {
        Messager.getInstance().sendResp(resp);
        finish();
    }


}
