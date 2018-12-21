package com.bwei.big.app;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;

public class DTApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context=this;
        super.onCreate();

    }
    public static Context getContext(){
        return context;
    }
}
