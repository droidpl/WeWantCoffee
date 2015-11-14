package com.github.droidpl.android.syncommand;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.github.droidpl.android.syncommand.adapters.SoundBoardAdapter;
import com.github.droidpl.android.syncommand.model.SoundItem;

public class SyncCommandActivity extends AppCompatActivity implements SoundBoardAdapter.SoundBoardAdapterListener {

    RecyclerView mRecyclerView;
    SoundBoardAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_command);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        setupRecyclerView();
    }


    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplication(), 3));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new SoundBoardAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT|ItemTouchHelper.UP|ItemTouchHelper.DOWN, 0) {

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

    }
}
