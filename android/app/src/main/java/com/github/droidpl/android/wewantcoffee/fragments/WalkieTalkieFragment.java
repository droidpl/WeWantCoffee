package com.github.droidpl.android.wewantcoffee.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.droidpl.android.wewantcoffee.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalkieTalkieFragment extends Fragment {


    public static WalkieTalkieFragment newInstance() {

        Bundle args = new Bundle();

        WalkieTalkieFragment fragment = new WalkieTalkieFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public WalkieTalkieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_walkie_talkie, container, false);
    }


}
