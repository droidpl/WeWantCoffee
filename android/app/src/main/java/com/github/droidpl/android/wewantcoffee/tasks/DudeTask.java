package com.github.droidpl.android.wewantcoffee.tasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.github.droidpl.android.wewantcoffee.callback.Callback;
import com.github.droidpl.android.wewantcoffee.model.dude.ApiResult;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by steven on 14/11/15.
 */
public class DudeTask extends AsyncTask<String, Void, Void> {

    /**
     * The generic operation callback.
     */
    private Callback<ApiResult> mCallback;

    /**
     * Exception produced during the task execution.
     */
    private Exception mException;

    /**
     * Directions Result.
     */
    private ApiResult mResult;

    /**
     * Constructor to save the context for the task.
     */
    public DudeTask(@NonNull Callback<ApiResult> callback){
        mCallback = callback;
    }

    @Override
    protected Void doInBackground (String... params) {
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=700%20hidden%20ridge,%20irving,%20tx&destination=iowa&sensor=false");
            InputStream stream = (InputStream) url.getContent();
            String json = stream2String(stream);
            mResult = new Gson().fromJson(json, ApiResult.class);

        } catch (MalformedURLException e) {
            mException = new RuntimeException("Error while getting the URL. " + e.getMessage(), e);
        } catch (IOException e) {
            mException = new RuntimeException("Error while retrieving the InputStream. " + e.getMessage(), e);
        }
        return null; // Does not need to resolve with anything
    }

    private String stream2String(InputStream inputStream) {
        StringBuilder total = new StringBuilder();
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = r.readLine()) != null) {
                total.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return total.toString();
    }

    @Override
    protected void onPostExecute(Void noResult) {
        super.onPostExecute(noResult);
        //Propagate the callback
        if(mCallback != null){
            if(mException == null && mResult != null){
                mCallback.onSuccess(mResult);
            } else {
                mCallback.onFailure(mException);
            }
        }
    }
}