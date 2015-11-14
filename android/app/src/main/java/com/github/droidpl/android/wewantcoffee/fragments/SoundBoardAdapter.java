package com.github.droidpl.android.wewantcoffee.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.droidpl.android.wewantcoffee.R;
import com.github.droidpl.android.wewantcoffee.model.SoundItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rob on 14/11/15.
 */
public class SoundBoardAdapter extends RecyclerView.Adapter<SoundBoardAdapter.ViewHolder> {

    SoundBoardAdapterListener mListener;
    List<SoundItem> mSounds;

    public SoundBoardAdapter(SoundBoardAdapterListener mListener) {
        this.mListener = mListener;

        mSounds = new ArrayList<>();
        mSounds.add(new SoundItem(R.mipmap.ic_launcher, "One!", R.raw.mario_data));
        mSounds.add(new SoundItem(R.mipmap.ic_launcher, "Two", R.raw.mario_data));
        mSounds.add(new SoundItem(R.mipmap.ic_launcher, "3", R.raw.mario_data));
        mSounds.add(new SoundItem(R.mipmap.ic_launcher, "4", R.raw.mario_data));
        mSounds.add(new SoundItem(R.mipmap.ic_launcher, "5", R.raw.mario_data));
        mSounds.add(new SoundItem(R.mipmap.ic_launcher, "6", R.raw.mario_data));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        View itemView = inf.inflate(R.layout.soundboard_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SoundItem sound = mSounds.get(position);
        holder.image.setImageResource(sound.iconResId);
        holder.text.setText(sound.name);
    }

    @Override
    public int getItemCount() {
        return mSounds != null ? mSounds.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView image;
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.soundboard_item_imageview);
            text = (TextView) itemView.findViewById(R.id.soundboard_item_textview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onSoundBoardItemClicked(mSounds.get(getAdapterPosition()));
            }
        }
    }

    public void handleDrag(int draggedPos, int toPos){
        SoundItem item = mSounds.remove(draggedPos);
        mSounds.add(toPos, item);

        notifyItemMoved(draggedPos, toPos);
    }


    interface SoundBoardAdapterListener{
        void onSoundBoardItemClicked(SoundItem sound);
    }
}
