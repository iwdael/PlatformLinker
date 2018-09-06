package com.blackchopper.loginsharepay.interfaces.impl;

import com.blackchopper.loginsharepay.model.Type;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : loginsharepay
 */
public class OnLoginSharePayListener implements com.blackchopper.loginsharepay.interfaces.OnLoginSharePayListener{
    @Override
    public void onLoginSuccess(Type type, String info) {

    }

    @Override
    public void onLoginCancel(Type type) {

    }

    @Override
    public void onLoginError(Type type, int errorCode) {

    }

    @Override
    public void onShareSuccess(Type type) {

    }

    @Override
    public void onShareCancel(Type type) {

    }

    @Override
    public void onShareError(Type type, int code) {

    }

    @Override
    public void onPaySuccess(Type type) {

    }

    @Override
    public void onPayCancel(Type type) {

    }

    @Override
    public void onPayError(Type type, int code) {

    }
}
