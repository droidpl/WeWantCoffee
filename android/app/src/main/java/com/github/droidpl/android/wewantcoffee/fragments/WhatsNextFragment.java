package com.github.droidpl.android.wewantcoffee.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.droidpl.android.wewantcoffee.R;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhatsNextFragment extends Fragment {

    private TextView tvWhatsNext;
    private static final int PERMISSION_REQUEST = 1;

    public static WhatsNextFragment newInstance() {
        Bundle args = new Bundle();

        WhatsNextFragment fragment = new WhatsNextFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("What's Next?");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_whats_next, container, false);
        tvWhatsNext = (TextView) v.findViewById(R.id.tvWhatsNext);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int calendar = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR);
        if(calendar == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_CALENDAR}, PERMISSION_REQUEST);
        }else{
            setData();
        }
    }

    public void setData(){
        String[] columns = {CalendarContract.Instances._ID, CalendarContract.Instances.TITLE, CalendarContract.Instances.BEGIN};
        long begin = new Date().getTime();
        long end = begin + (24 * 60 * 60 * 1000);
        Cursor cursor = CalendarContract.Instances.query(getActivity().getContentResolver(), columns, begin, end);
        cursor.moveToFirst();

        Date start = new Date(Long.parseLong(cursor.getString(2)));
        tvWhatsNext.setText(cursor.getString(1) + " @ " + start.toString());
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setData();
                } else {
                    Toast.makeText(getActivity(), "No permissions to read on the calendar. Enter again", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
