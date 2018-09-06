package com.blackchopper.loginsharepay.impl;

import android.support.v7.app.AppCompatActivity;

import com.blackchopper.loginsharepay.model.Type;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;

public class LoginSharePay extends LoginShare {

    public LoginSharePay(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    protected void payWechat(PayReq req) {
        iWXAPI.sendReq(req);
    }

    //wechat pay resq
    @Override
    public void onResp(BaseResp resp) {
        super.onResp(resp);
        if (listener == null) return;
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0)
                listener.onPaySuccess(Type.Wechat);
            if (resp.errCode == -2)
                listener.onPayCancel(Type.Wechat);
            if (resp.errCode == -1)
                listener.onPayError(Type.Wechat, resp.errCode);
        }
     }
}
