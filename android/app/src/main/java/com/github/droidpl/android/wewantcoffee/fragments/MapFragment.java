package com.github.droidpl.android.wewantcoffee.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.droidpl.android.wewantcoffee.R;
import com.github.droidpl.android.wewantcoffee.service.Http;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MapFragment extends LocationRequestFragment implements OnMapReadyCallback {



    private GoogleMap mMap;



    public static Fragment newInstance() {
        return new MapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_maps, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        prepareApi();
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
        Toast.makeText(getActivity(), "Unable to load the location", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(final Location location) {
        Call call = Http.client().newCall(new Request.Builder()
                .url("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + location.getLatitude() + "," + location.getLongitude() + "&sensor=false")
                .method("GET", null)
                .build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                Toast.makeText(getContext(), "Error getting location name", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String title = "Unable to get name";
                if (response.isSuccessful()) {
                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        JSONArray results = obj.optJSONArray("results");

                        if (results.length() > 0) {
                            title = results.optJSONObject(0).optString("formatted_address");
                        }
                        displayInMapAsync(location, title);
                    } catch (JSONException e) {
                        displayInMapAsync(location, title);
                    }
                } else {
                    displayInMapAsync(location, title);
                }
            }
        });

    }

    private void displayInMapAsync(final Location location, final String title){
        MapFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LatLng whereAmI = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(whereAmI).title(title));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(whereAmI));
            }
        });
    }
}
