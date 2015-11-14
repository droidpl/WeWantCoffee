package com.github.droidpl.android.wewantcoffee.service;


import com.github.droidpl.android.wewantcoffee.callback.Callback;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GCMWebService {

    public static void sendRegistrationId(String regId, final Callback<Void> callback){
        JSONObject json = new JSONObject();
        try {
            json.put("RegistrationId", regId);
            json.put("Model", android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL);
            Call call = Http.client().newCall( new Request.Builder()
                    .url("http://droidpl-go.appspot.com/regsave")
                    .method("POST", RequestBody.create(MediaType.parse("application/json"), json.toString()))
                    .build());
            call.enqueue(new com.squareup.okhttp.Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    callback.onFailure(null);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    if(response.isSuccessful()) {
                        callback.onSuccess(null);
                    }else{
                        callback.onFailure(null);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onFailure(e);
        }
    }
}
