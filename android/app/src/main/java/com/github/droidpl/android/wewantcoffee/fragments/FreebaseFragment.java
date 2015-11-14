package com.github.droidpl.android.wewantcoffee.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.github.droidpl.android.wewantcoffee.R;
import com.github.droidpl.android.wewantcoffee.adapters.FreebaseAdapter;
import com.github.droidpl.android.wewantcoffee.model.freebase.Result;
import com.github.droidpl.android.wewantcoffee.service.Http;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class FreebaseFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FreebaseAdapter mAdapter;

    private WebView mWebView;

    public static FreebaseFragment newInstance() {

        Bundle args = new Bundle();

        FreebaseFragment fragment = new FreebaseFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public FreebaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.title_freebase);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_freebase, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        getViews(view);

        getFreebaseData();

        return view;
    }

    private void getViews(View view) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new FreebaseAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getFreebaseData() {
        Call call = Http.client().newCall( new Request.Builder()
                .url("https://www.googleapis.com/freebase/v1/search?query=honda&indent=true&filter=(any%20type:/automotiva/model)")
                .build());
        call.enqueue(new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("Error", "Cannot receive data: " + e.toString());
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i("Success", "Received data: " + response.toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Result result = null;
                            try {
                                result = new Gson().fromJson(response.body().string(), Result.class);
                                mAdapter.setItems(result.results);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } else {
                    Log.e("Error", "Cannot receive data: " + response.toString());
                }
            }
        });
    }

}
