package com.github.droidpl.android.wewantcoffee.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.github.droidpl.android.wewantcoffee.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AngularJsFragment extends Fragment {

    private WebView mWebView;

    public static AngularJsFragment newInstance() {

        Bundle args = new Bundle();

        AngularJsFragment fragment = new AngularJsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public AngularJsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sound_board, container, false);

        return view;
    }

    private void getViews(View view) {
        mWebView = (WebView) view.findViewById(R.id.angular_js_webview);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/www/angular/index.html");
    }

}
