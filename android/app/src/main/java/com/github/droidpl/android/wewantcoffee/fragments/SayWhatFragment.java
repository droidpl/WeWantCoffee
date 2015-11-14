package com.github.droidpl.android.wewantcoffee.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.droidpl.android.wewantcoffee.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class SayWhatFragment extends Fragment {

    private static final int REQUEST_SPEECH = 0;

    public static SayWhatFragment newInstance() {
        Bundle args = new Bundle();

        SayWhatFragment fragment = new SayWhatFragment();
        fragment.setArguments(args);

        return fragment;
    }


    public SayWhatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Say What?");

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_say_what, container, false);

        Button button = (Button) v.findViewById(R.id.btnSpeech);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                startActivityForResult(i, REQUEST_SPEECH);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_SPEECH:
                ArrayList results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                Toast.makeText(getActivity(), (String) results.get(0), Toast.LENGTH_LONG).show();
                break;
        }
    }
}
