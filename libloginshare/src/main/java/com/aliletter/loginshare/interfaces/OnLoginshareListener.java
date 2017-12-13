package com.aliletter.loginshare.interfaces;


import com.aliletter.loginshare.model.Type;

/**
 * Author：alilettter
 * Github: http://github.com/aliletter
 * Email: 4884280@qq.com
 * data: 2017/12/11
 */

public interface OnLoginshareListener {
    void onLoginSuccess(Type type, String info);

    void onLoginCancel(Type type);

    void onLoginError(Type type, int errorCode);

    void onShareSuccess(Type type);

    void onShareCancel(Type type);

    void onShareError(Type type, int code);

}
