package com.github.droidpl.android.syncommand;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

public class SyncCommandActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mClient;
    private Message mLastMessage;
    private boolean mIsConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_command);
        mClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mClient.isConnected()) {
            mClient.connect();
        }
        Nearby.Messages.subscribe(mClient, new MessageListener() {
            @Override
            public void onFound(Message message) {
                Action action = Action.parse(message);
                Log.i("Received message", action.toString());
                //TODO send to someone
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mClient.isConnected()){
            if(mLastMessage != null) {
                Nearby.Messages.unpublish(mClient, mLastMessage).setResultCallback(null);
            }
            Nearby.Messages.unsubscribe(mClient, this).setResultCallback(null);
        }
        mClient.disconnect();
    }

    public void broadcastAction(Action action, final ActionCallback callback){
        Message message = new Message(action.toString().getBytes());
        Nearby.Messages.publish(mClient, message)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            callback.success(action);
                        } else {
                            callback.error();
                    }
                }
            });
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
