package com.fepa.meupet.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchMapFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {

    private GoogleMap map;

    private String[] options;

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
        switch (item.getItemId()){
            case R.id.actSMapFilter :
                SearchMapFilterDialog multiple = new SearchMapFilterDialog();
                multiple.setTargetFragment(this, GeneralConfig.Maps.SEARCH_MAP_REQUEST_CODE);
                multiple.show(getActivity().getSupportFragmentManager(),"dialogMultiple");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==  GeneralConfig.Maps.SEARCH_MAP_REQUEST_CODE
                && resultCode == GeneralConfig.RESULT_OK){

            // gets the boolean vector that marks each checked option
            boolean[] result = data.getBooleanArrayExtra(GeneralConfig.Maps.SEARCH_MAP_BUNDLE);

            for (int i = 0; i < result.length; i++) {
                if (result[i])
                    this.showMarkers(this.options[i]);
                else
                    this.removeMarkers(this.options[i]);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        this.setupMarkersSet();

        // shows both the satellite and the places name
        this.map.setMapType(googleMap.MAP_TYPE_NORMAL);

        // sets the ui interaction that the map will have
        this.map.getUiSettings().setZoomControlsEnabled(true);
        this.map.getUiSettings().setMyLocationButtonEnabled(true);
        this.map.getUiSettings().setMapToolbarEnabled(true);
        this.map.getUiSettings().setRotateGesturesEnabled(false);

        // TODO: GET THE PERSON POSITION INSTEAD OF NATAL
        // Add a marker in Natal and move the camera there
        LatLng natal = new LatLng(-5.814940, -35.222929);
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(natal, 10));
    }


    private void setupMarkersSet(){
        this.options = getActivity().getResources().getStringArray(R.array.smap_dialog_options);

        // initiates the map
        for (int i = 0; i < this.options.length; i++) {
            this.markersSet.put(this.options[i], new ArrayList<Marker>());
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
        this.reference = this.database.getReference("miauBD/searchMap").child(child);

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
