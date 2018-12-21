package com.bwei.big.http;

import android.util.Log;

import com.bwei.big.interceptor.LoggingInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {
    public static String get(String urlString) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor())
                //日志拦截器
                .connectTimeout(10, TimeUnit.SECONDS)
                // 连接超时
                .readTimeout(10, TimeUnit.SECONDS)
                // 读取超时
                .writeTimeout(10, TimeUnit.SECONDS)
                // 写入超时
                .build();
        Request request = new Request.Builder().url(urlString).get().build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String result = response.body().string();
            Log.i("dt", "请求结果：" + result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
