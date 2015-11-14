package com.github.droidpl.android.wewantcoffee.gcm;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.github.droidpl.android.wewantcoffee.callback.Callback;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Task used to retrieve the current token associated with the application and send it to the server. It also
 * provides an alias if there is one previously assigned.
 */
public class PushInstanceIDTask extends AsyncTask<String, Void, Void> {

    /**
     * The context used to retrieve the info for user notifications.
     */
    private Context mContext;

    /**
     * The generic operation callback.
     */
    private Callback<String> mCallback;

    /**
     * Exception produced during the task execution.
     */
    private Exception mException;

    /**
     * GCM token brought from the Instance ID task.
     */
    private String mToken;

    /**
     * Constructor to save the context for the task.
     * @param ctx The context for the task.
     */
    public PushInstanceIDTask(@NonNull Context ctx, @NonNull Callback<String> callback){
        mContext = ctx;
        mCallback = callback;
    }


    @Override
    protected Void doInBackground (String... params) {
        if(params.length > 0) { //First parameter is the sender id of GCM
            String gcmSenderId = params[0];
            try {
                InstanceID instanceID = InstanceID.getInstance(mContext);
                //Can produce a exception if google play is not installed
                mToken = instanceID.getToken(gcmSenderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            } catch (IOException e) {
                mException = new RuntimeException("Error while retrieving the instance of GCM. " + e.getMessage(), e);
            }
        }
        return null; // Does not need to resolve with anything
    }

    @Override
    protected void onPostExecute(Void noResult) {
        super.onPostExecute(noResult);
        //Propagate the callback
        if(mCallback != null){
            if(mException == null){
                mCallback.onSuccess(mToken);
            } else {
                mCallback.onFailure(mException);
            }
        }
    }
}