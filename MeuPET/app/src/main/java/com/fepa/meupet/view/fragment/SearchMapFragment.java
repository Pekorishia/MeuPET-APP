package com.fepa.meupet.view.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fepa.meupet.R;
import com.fepa.meupet.control.dialog.SearchMapFilterDialog;
import com.fepa.meupet.model.agent.map.Geolocation;
import com.fepa.meupet.model.environment.constants.GeneralConfig;
import com.fepa.meupet.model.environment.permissions.Permissions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SearchMapFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

    private String[] options;
    private boolean[] checkList;

    private Map<String, List<Marker>> markersSet = new HashMap<>();


    private FirebaseDatabase database;
    private DatabaseReference reference;


    public SearchMapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Permissions.validatePermissions(this.permissions, getActivity(), 1);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapSearch);
        mapFragment.getMapAsync(this);

        this.database = FirebaseDatabase.getInstance();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.search_map_action, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actSMapFilter:
                SearchMapFilterDialog multiple = new SearchMapFilterDialog();
                Bundle data = new Bundle();
                data.putBooleanArray("checklist", checkList);
                multiple.setArguments(data);
                multiple.setTargetFragment(this, GeneralConfig.Maps.SEARCH_MAP_REQUEST_CODE);
                multiple.show(getActivity().getSupportFragmentManager(), "dialogMultiple");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GeneralConfig.Maps.SEARCH_MAP_REQUEST_CODE
                && resultCode == GeneralConfig.RESULT_OK) {

            // gets the boolean array that marks each checked option
            this.checkList = data.getBooleanArrayExtra(GeneralConfig.Maps.SEARCH_MAP_BUNDLE);

            for (int i = 0; i < this.checkList.length; i++) {
                if (this.checkList[i])
                    this.showMarkers(this.options[i]);
                else
                    this.removeMarkers(this.options[i]);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissionResult : grantResults) {
            if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            0,
                            locationListener
                    );
                }
            }
        }
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.map = googleMap;

        this.locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng userPosition = new LatLng(location.getLatitude(), location.getLongitude());

                map.addMarker(new MarkerOptions().position(userPosition).title("Meu Local"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition, 12));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    6000,
                    20,
                    locationListener
            );
        }

        setupMarkersSet();

        // shows both the satellite and the places name
        map.setMapType(googleMap.MAP_TYPE_NORMAL);

        // sets the ui interaction that the map will have
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(false);
    }

    private void setupMarkersSet(){
        this.options = getActivity().getResources().getStringArray(R.array.smap_dialog_options);

        this.checkList = new boolean[this.options.length];

        // initiates the map and the boolean array
        for (int i = 0; i < this.options.length; i++) {
            this.markersSet.put(this.options[i], new ArrayList<Marker>());
            this.checkList[i] = true;
        }

        this.setMarkers(this.options[0], "vets");
        this.setMarkers(this.options[1], "restaurants");
        this.setMarkers(this.options[2], "beaches");
        this.setMarkers(this.options[3], "shoppingMalls");
        this.setMarkers(this.options[4], "hotels");
    }

    /*
     * Populates the Map with the markers and add markers at the map
     */
    private void setMarkers(final String option, String child){
        this.reference = this.database.getReference(GeneralConfig.DB_PATH_MAP).child(child);

        this.reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Geolocation geolocation;
                MarkerOptions marker;

                float color = Float.parseFloat(dataSnapshot.child("color").getValue().toString());

                // for every location
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    if (!data.getKey().equals("color")){
                            geolocation = data.getValue(Geolocation.class);
                            // creates a new marker
                            marker = new MarkerOptions();

                            marker.title(geolocation.getPlaceName())
                                    .position(new LatLng(geolocation.getLatitude(), geolocation.getLongitude()))
                                    .icon(BitmapDescriptorFactory.defaultMarker(color));


                            // shows it on the map and put the Marker created
                            // inside the markersSet
                            markersSet.get(option).add(map.addMarker(marker));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void showMarkers(String option){
        for (Marker marker : this.markersSet.get(option)){
            marker.setVisible(true);
        }
    }

    private void removeMarkers(String option){
        for (Marker marker : this.markersSet.get(option)){
            marker.setVisible(false);
        }
    }

}
