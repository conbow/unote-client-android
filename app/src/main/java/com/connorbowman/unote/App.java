package com.connorbowman.unote;

import android.app.Application;

import com.connorbowman.unote.services.ApiService;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new ApiService(getApplicationContext());
        //LeakCanary.install(this);
    }
}
