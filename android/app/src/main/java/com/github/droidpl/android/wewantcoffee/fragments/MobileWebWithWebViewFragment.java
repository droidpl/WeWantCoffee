package com.github.droidpl.android.wewantcoffee.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.github.droidpl.android.wewantcoffee.R;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class MobileWebWithWebViewFragment extends Fragment {

    public static MobileWebWithWebViewFragment newInstance() {
        Bundle args = new Bundle();

        MobileWebWithWebViewFragment fragment = new MobileWebWithWebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public MobileWebWithWebViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mobile_web_with_web_view, container, false);

        WebView webview = (WebView) v.findViewById(R.id.wvMobile);
        webview.loadUrl("http://jqtjs.com/preview/demos/main/");

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);

        return v;
    }


}
