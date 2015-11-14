package com.github.droidpl.android.wewantcoffee.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.droidpl.android.wewantcoffee.R;
import com.github.droidpl.android.wewantcoffee.model.freebase.ResultItem;

/**
 * Created by rob on 14/11/15.
 */
public class FreebaseAdapter extends RecyclerView.Adapter<FreebaseAdapter.ViewHolder> {

    private Context mContext;

    private ResultItem[] mResults;

    public FreebaseAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        View itemView = inf.inflate(R.layout.freebase_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ResultItem resultItem = mResults[position];
        holder.text.setText(Html.fromHtml(resultItem.name));
        Glide.with(mContext).load("https://www.googleapis.com/freebase/v1/image/" + resultItem.mid).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mResults != null ? mResults.length : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.freebase_name);
            image = (ImageView) itemView.findViewById(R.id.freebase_image);
        }
    }

    public void setItems(ResultItem[] results) {
        mResults = results;
        notifyDataSetChanged();
    }
}
