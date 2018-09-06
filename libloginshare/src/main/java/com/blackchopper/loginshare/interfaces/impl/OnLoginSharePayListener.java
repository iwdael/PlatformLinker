package com.blackchopper.loginshare.interfaces.impl;

import com.blackchopper.loginshare.interfaces.OnLoginSharePayListener;
import com.blackchopper.loginshare.model.Type;

public class OnLoginSharePayListener implements OnLoginSharePayListener {
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
