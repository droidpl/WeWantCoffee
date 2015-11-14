package com.github.droidpl.android.wewantcoffee.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public abstract class LocationRequestFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int PERMISSION_REQUEST = 1;
    private GoogleApiClient mClient;

    public final void prepareApi(){
        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mClient.connect();
    }

    public void requestLocation(){
        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, getRequest(), this);
    }

    public LocationRequest getRequest(){
        LocationRequest request = new LocationRequest();
        request.setNumUpdates(1);
        return request;
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onLocationPermissionsPrepare();
                } else {
                    Toast.makeText(getActivity(), "No permissions to get the updates", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public final void onConnected(Bundle bundle) {
        int location = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int locationFine = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if(location == PackageManager.PERMISSION_DENIED || locationFine == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
        }else{
            onLocationPermissionsPrepare();
        }
    }

    public abstract void onLocationPermissionsPrepare();


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mClient != null) {
            mClient.disconnect();
        }
    }
}
