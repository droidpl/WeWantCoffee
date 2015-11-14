package com.github.droidpl.android.wewantcoffee;

import android.app.Application;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.droidpl.android.wewantcoffee.callback.Callback;
import com.github.droidpl.android.wewantcoffee.gcm.PushInstanceIDTask;
import com.github.droidpl.android.wewantcoffee.service.GCMWebService;

public class CoffeeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new PushInstanceIDTask(this, new Callback<String>() {
            @Override
            public void onSuccess(@Nullable String data) {
                GCMWebService.sendRegistrationId(data, new Callback<Void>() {
                    @Override
                    public void onSuccess(@Nullable Void data) {
                        Log.i("Success", "GCM token registered " + data);
                    }

                    @Override
                    public void onFailure(@Nullable Throwable exception) {
                        Log.e("Error", "Cannot register");
                    }
                });
            }

            @Override
            public void onFailure(@Nullable Throwable exception) {
                Log.e("Error", "Cannot register");
            }
        }).execute();
    }
}
