package com.blackchopper.loginshare.net;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class FileDownloadHttp {
    private Context mContext;
    private HttpURLConnection mUrlConnection;
    private String mUrl;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public FileDownloadHttp(Context context, String url) {
        this.mUrl = url;
        this.mContext = context;
    }

   
    public void executor() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fos = null;
                try {
                    URL url = new URL(mUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(3000);
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        String path = Environment.getExternalStorageDirectory().getPath() + "/Android/data/";
                        String packageName = mContext.getPackageName();
                        path = path + packageName + "/loginshare/";
                        new File(path).mkdirs();
                        Log.i(FileDownloadHttp.class.getName(), path);
                        path = path + System.currentTimeMillis() + ".jpg";
                        Log.i(FileDownloadHttp.class.getName(), path);
                        InputStream is = conn.getInputStream();
                        fos = new FileOutputStream(new File(path));
                        byte[] b = new byte[1024];
                        int len = 0;
                        while ((len = is.read(b)) != -1) {
                            fos.write(b, 0, len);
                        }
                        fos.flush();
                        final String finalPath = path;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                onResult(finalPath);
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                onError();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onError();
                        }
                    });
                }
            }
        }).start();

    }

    public abstract void onResult(String path);

    public abstract void onError();

}
