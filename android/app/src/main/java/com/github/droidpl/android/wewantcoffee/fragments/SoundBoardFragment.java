package com.github.droidpl.android.wewantcoffee.fragments;


import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.droidpl.android.wewantcoffee.R;
import com.github.droidpl.android.wewantcoffee.model.SoundItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class SoundBoardFragment extends Fragment implements SoundBoardAdapter.SoundBoardAdapterListener {

    private RecyclerView mRecyclerView;
    private SoundBoardAdapter mAdapter;
    private SoundPool mPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

    public static SoundBoardFragment newInstance() {

        Bundle args = new Bundle();

        SoundBoardFragment fragment = new SoundBoardFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public SoundBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sound_board, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);

        setupRecyclerView();

        return v;
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
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
    public void onSoundBoardItemClicked(SoundItem item) {
        //PLAY THE SOUND!
        int soundId = mPool.load(getContext(), item.soundResId, 1);
        mPool.play(soundId, 1, 1, 1, 0, 1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPool = null;
    }
}
