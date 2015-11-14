package com.github.droidpl.android.wewantcoffee.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.droidpl.android.wewantcoffee.R;
import com.github.droidpl.android.wewantcoffee.model.dude.Step;

/**
 * Created by rob on 14/11/15.
 */
public class DudeDirectionsAdapter extends RecyclerView.Adapter<DudeDirectionsAdapter.ViewHolder> {

    private Step[] mSteps;

    public DudeDirectionsAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        View itemView = inf.inflate(R.layout.dude_direction_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Step step = mSteps[position];
        holder.text.setText(Html.fromHtml(step.html_instructions));
    }

    @Override
    public int getItemCount() {
        return mSteps != null ? mSteps.length : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.dude_direction_item_textview);
        }
    }

    public void setItems(Step[] steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }
}
