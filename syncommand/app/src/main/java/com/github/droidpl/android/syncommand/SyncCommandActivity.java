package com.github.droidpl.android.syncommand;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import com.github.droidpl.android.syncommand.adapters.SoundBoardAdapter;
import com.github.droidpl.android.syncommand.model.SoundItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.List;


public class SyncCommandActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, SoundBoardAdapter.SoundBoardAdapterListener{

    private GoogleApiClient mClient;
    private Message mLastMessage;

    private RecyclerView mRecyclerView;
    private SoundBoardAdapter mAdapter;
    private List<SoundItem> mSounds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_command);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mSounds = SoundBoardAdapter.getSounds();
        setupRecyclerView();
    }


    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new SoundBoardAdapter(mSounds, this);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

            private RecyclerView.ViewHolder raisedView;

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder dragged, RecyclerView.ViewHolder target) {
                int draggedPosition = dragged.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                mAdapter.handleDrag(draggedPosition, toPosition);

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                SoundItem item = mAdapter.getSoundItem(viewHolder.getAdapterPosition());
                if(item == null){
                    //WTF!?
                    return;
                }
                mAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                onSoundBoardItemClicked(item);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
                    viewHolder.itemView.animate().z(25f).start();
                    raisedView = viewHolder;
                } else {
                    if(raisedView != null) {
                        raisedView.itemView.animate().z(0f).start();
                        raisedView = null;
                    }
                }
                super.onSelectedChanged(viewHolder, actionState);
            }
        };

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onSoundBoardItemClicked(SoundItem sound) {
        Toast.makeText(this, sound.name, Toast.LENGTH_SHORT).show();
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

    public void broadcastAction(final Action action, final ActionCallback callback){
        mLastMessage = new Message(action.toString().getBytes());
        Nearby.Messages.publish(mClient, mLastMessage)
            .setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    mLastMessage = null;
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