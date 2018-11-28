package com.fepa.meupet.view.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fepa.meupet.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchMapFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap map;

    public SearchMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapSearch);
        mapFragment.getMapAsync(this);
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
//        this.map = googleMap;
//
//        Toast.makeText(getContext(), "ENTREI", Toast.LENGTH_SHORT).show();
//
//        // Add a marker in Sydney and move the camera
//        LatLng place = new LatLng(-34, 151);
//
//        this.map.addMarker(new MarkerOptions().position(place).title("Sidney"));
//        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(place,12));

        map = googleMap;

        // shows both the satellite and the places name
        map.setMapType(googleMap.MAP_TYPE_HYBRID);

        // shows the zoom buttons on the corner of the map
        map.getUiSettings().setZoomControlsEnabled(true);

        map.setOnMapClickListener(this);
        map.setOnMapLongClickListener(this);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    @Override
    public void onMapClick(LatLng latLng) {
        String text = String.format("(%.4f, %4f)", latLng.latitude, latLng.longitude);

        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Toast.makeText(getContext(), latLng.toString(), Toast.LENGTH_SHORT).show();
    }
}
