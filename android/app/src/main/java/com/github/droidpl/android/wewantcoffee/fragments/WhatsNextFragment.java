package com.github.droidpl.android.wewantcoffee.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.droidpl.android.wewantcoffee.R;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhatsNextFragment extends Fragment {

    public static WhatsNextFragment newInstance() {
        Bundle args = new Bundle();

        WhatsNextFragment fragment = new WhatsNextFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public WhatsNextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_whats_next, container, false);

        TextView tvWhatsNext = (TextView) v.findViewById(R.id.tvWhatsNext);

        String[] columns = {CalendarContract.Instances._ID, CalendarContract.Instances.TITLE, CalendarContract.Instances.BEGIN};

        long begin = new Date().getTime();
        long end = begin + (24 * 60 * 60 * 1000);
        Cursor cursor = CalendarContract.Instances.query(getActivity().getContentResolver(), columns, begin, end);
        cursor.moveToFirst();

        Date start = new Date(Long.parseLong(cursor.getString(2)));

        tvWhatsNext.setText(cursor.getString(1) + " @ " + start.toString());

        return v;
    }


}
