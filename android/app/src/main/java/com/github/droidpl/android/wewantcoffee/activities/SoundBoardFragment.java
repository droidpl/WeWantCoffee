package com.github.droidpl.android.wewantcoffee.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.droidpl.android.wewantcoffee.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SoundBoardFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_sound_board, container, false);
    }


}
