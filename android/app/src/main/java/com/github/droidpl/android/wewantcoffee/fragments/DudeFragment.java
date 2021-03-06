package com.github.droidpl.android.wewantcoffee.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.github.droidpl.android.wewantcoffee.R;
import com.github.droidpl.android.wewantcoffee.callback.Callback;
import com.github.droidpl.android.wewantcoffee.model.dude.ApiResult;
import com.github.droidpl.android.wewantcoffee.model.dude.Step;
import com.github.droidpl.android.wewantcoffee.tasks.DudeTask;
import com.google.android.gms.common.ConnectionResult;

/**
 * A simple {@link Fragment} subclass.
 */
public class DudeFragment extends LocationRequestFragment {

    private WebView mWebview;
    private ProgressBar mProgress;

    public static DudeFragment newInstance() {

        Bundle args = new Bundle();

        DudeFragment fragment = new DudeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public DudeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.title_dude);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dude, container, false);

        prepareApi();

        getViews(view);

        return view;
    }

    private void getViews(View view) {
        mWebview = (WebView) view.findViewById(R.id.dude_webview);
        mProgress = (ProgressBar) view.findViewById(R.id.dude_progress);
    }

    private void getDirections(Location location) {
        new DudeTask(location, new Callback<ApiResult>() {
            @Override
            public void onSuccess(@Nullable final ApiResult data) {
                Log.i("Success", "Received data: " + data.toString());
                if (data.routes.length > 0 && data.routes[0].legs.length > 0) {
                    Step[] steps = data.routes[0].legs[0].steps;
                    StringBuilder sb = new StringBuilder();
                    sb.append("<html>");
                    sb.append("<style>");
                    sb.append("div.content {\npadding: 10;\n}");
                    sb.append("</style>");
                    sb.append("<body>");
                    for (int i = 0; i < steps.length; i++) {
                        sb.append("<div class=\"content\">");
                        sb.append(steps[i].html_instructions);
                        sb.append("</div>");
                    }
                    sb.append("</body>");
                    sb.append("</html>");
                    mWebview.loadData(sb.toString(), "text/html", "utf-8");
                }
                showDirections();
            }

            @Override
            public void onFailure(@Nullable Throwable exception) {
                Log.e("Error", "Cannot receive data: " + exception.toString());
            }
        }).execute();
    }

    private void showDirections() {
        if (mWebview.getVisibility() != View.VISIBLE) {
            mProgress.animate().alpha(0f);
            mWebview.setAlpha(0f);
            mWebview.setVisibility(View.VISIBLE);
            mWebview.animate().alpha(1f);
        } else {
            mProgress.animate().alpha(0f);
        }
    }

    @Override
    public void onLocationPermissionsPrepare() {
        requestLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        showLoading();
        getDirections(location);
    }

    private void showLoading() {
        mProgress.setAlpha(1f);
    }
}
