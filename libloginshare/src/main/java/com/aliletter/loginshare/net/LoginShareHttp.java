package com.aliletter.loginshare.net;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Author：alilettter
 * Github: http://github.com/aliletter
 * Email: 4884280@qq.com
 * data: 2017/12/11
 */

public abstract class LoginShareHttp {
    private Map<String, String> mBody;
    private HttpURLConnection mUrlConnection;
    private String mUrl;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public LoginShareHttp(String mUrl, Map<String, String> mBody) {
        this.mBody = mBody;
        this.mUrl = mUrl;
        new Thread(new Runnable() {
            @Override
            public void run() {
                executor();
            }
        }).start();
    }

    public void executor() {
        try {
            StringBuilder builder = new StringBuilder();
            if (this.mBody != null) {
                Iterator var4 = this.mBody.keySet().iterator();
                int pos = 0;
                while (true) {
                    if (!var4.hasNext()) {
                        mUrl = mUrl + "?" + builder.toString();
                        break;
                    }
                    String key = (String) var4.next();
                    if (pos > 0) {
                        builder.append("&");
                    }
                    try {
                        builder.append(String.format("%s=%s", new Object[]{key, URLEncoder.encode((String) this.mBody.get(key), "utf-8")}));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    ++pos;
                }
            }
            URL turl = new URL(mUrl);
            this.mUrlConnection = (HttpURLConnection) turl.openConnection();
            this.mUrlConnection.setConnectTimeout(5000);
            this.mUrlConnection.setReadTimeout(5000);
            this.mUrlConnection.setUseCaches(true);
            this.mUrlConnection.setRequestMethod("GET");
            this.mUrlConnection.connect();
            if (this.mUrlConnection.getResponseCode() == 200) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(mUrlConnection.getInputStream()));
                    final StringBuilder outBuilder = new StringBuilder();
                    String line = null;
                    try {
                        while ((line = reader.readLine()) != null) {
                            outBuilder.append(line);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onResult(0, outBuilder.toString());
                        }
                    });
                } catch (Exception var7) {
                    var7.printStackTrace();
                }
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            onResult(mUrlConnection.getResponseCode(), "");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void onResult(int code, String result);
}
