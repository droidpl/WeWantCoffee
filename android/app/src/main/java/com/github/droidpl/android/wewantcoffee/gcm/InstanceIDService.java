package com.github.droidpl.android.wewantcoffee.gcm;

import android.support.annotation.Nullable;
import android.util.Log;

import com.github.droidpl.android.wewantcoffee.callback.Callback;
import com.github.droidpl.android.wewantcoffee.service.GCMWebService;
import com.google.android.gms.iid.InstanceIDListenerService;

public class InstanceIDService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh () {
        super.onTokenRefresh();
        new PushInstanceIDTask(this, new Callback<String>() {
            @Override
            public void onSuccess(@Nullable String data) {
                GCMWebService.sendRegistrationId(data, new Callback<Void>() {
                    @Override
                    public void onSuccess(@Nullable Void data) {
                        //Nothing here
                        Log.i("Registered", "Token registered " + data);
                    }

                    @Override
                    public void onFailure(@Nullable Throwable exception) {
                        Log.e("Error", "Error registering token");
                    }
                });
            }

            @Override
            public void onFailure(@Nullable Throwable exception) {
                Log.e("Error", "Error registering token");
            }
        }).execute();
    }
}
