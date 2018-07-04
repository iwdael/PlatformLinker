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

    public static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    public static File writeBytesToFile(byte[] b, String outputFile) {
        File file = null;
        FileOutputStream os = null;

        try {
            file = new File(outputFile);
            os = new FileOutputStream(file);
            os.write(b);
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            }

        }

        return file;
    }

    public void executor() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(mUrl);
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setDoOutput(true);// 使用 URL 连接进行输出
                    httpConn.setDoInput(true);// 使用 URL 连接进行输入
                    httpConn.setUseCaches(false);// 忽略缓存
                    httpConn.setRequestMethod("GET");// 设置URL请求方法
                    //可设置请求头
                    httpConn.setRequestProperty("Content-Type", "application/octet-stream");
                    httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                    httpConn.setRequestProperty("Charset", "UTF-8");
                    byte[] file = input2byte(httpConn.getInputStream());
                    String path = Environment.getExternalStorageDirectory().getPath() + "Android/data/";
                    String packageName = mContext.getPackageName();
                    path = path + packageName.replace(".", "/") + "/loginshare/";
                    new File(path).mkdirs();
                    path = path + System.currentTimeMillis() + ".jpg";
                    writeBytesToFile(file, path);
                    Log.i(FileDownloadHttp.this.getClass().getName(), path);
                    final String finalPath = path;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onResult(finalPath);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
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
