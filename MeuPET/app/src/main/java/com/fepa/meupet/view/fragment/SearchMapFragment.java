package com.fepa.meupet.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
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
import com.fepa.meupet.control.general.Geolocation;
import com.fepa.meupet.model.environment.constants.GeneralConfig;
import com.fepa.meupet.view.activity.RegisterPetActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchMapFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {

    private GoogleMap map;

    private String[] options;

    private Map<String, List<MarkerOptions>> markersSet = new HashMap<>();

    private Geolocation[] vetLocations =
            {
                    new Geolocation("Hospital Veterinário De Natal", -5.818388, -35.201147),
                    new Geolocation("Animal Center Hospital e Pet Shop", -5.867451, -35.196872),
                    new Geolocation("Harmony Vet",-5.857853, -35.202404)
            };
    private Geolocation[] restaurantsLocations =
            {
                    new Geolocation("Casanova Ecobar", -5.836671, -35.212228),
                    new Geolocation("Curva do Vento", -5.881958, -35.177671),
                    new Geolocation("Bocaditos",-5.877862, -35.178058)
            };

    private Geolocation[] beachesLocations =
            {
                    new Geolocation("Praia de Ponta Negra", -5.873671, -35.176585),
                    new Geolocation("Praia do Meio", -5.778451, -35.193472),
                    new Geolocation("Praia dos Artistas",-5.781828, -35.192804)
            };

    private Geolocation[] shoppingLcations =
            {
                    new Geolocation("Praia Shopping", -5.865860, -35.185632),
                    new Geolocation("Natal Shopping", -5.841939, -35.211663),
                    new Geolocation("Seaway Center",-5.859918, -35.194103)
            };

    private Geolocation[] hotelLcations =
            {
                    new Geolocation("Golden Tulip Natal Ponta Negra", -5.875551, -35.178614),
                    new Geolocation("Comfort Hotel & Suites Natal", -5.879475, -35.176069),
                    new Geolocation("Chalet Suisse Hotel", -5.882357, -35.173833)
            };

    private float[] colors =
            {
                    BitmapDescriptorFactory.HUE_BLUE,
                    BitmapDescriptorFactory.HUE_ORANGE,
                    BitmapDescriptorFactory.HUE_AZURE,
                    BitmapDescriptorFactory.HUE_VIOLET,
                    BitmapDescriptorFactory.HUE_MAGENTA
            };


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

        this.options = getActivity().getResources().getStringArray(R.array.smap_dialog_options);

        View view = inflater.inflate(R.layout.fragment_search_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapSearch);
        mapFragment.getMapAsync(this);
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
                multiple.setTargetFragment(this, GeneralConfig.SEARCH_MAP_REQUEST_CODE);
                multiple.show(getActivity().getSupportFragmentManager(),"dialogMultiple");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==  GeneralConfig.SEARCH_MAP_REQUEST_CODE
                && resultCode == GeneralConfig.RESULT_OK){

            boolean[] result = data.getBooleanArrayExtra(GeneralConfig.SEARCH_MAP_BUNDLE);

            for (int i = 0; i < result.length; i++) {
                if (result[i]){
                    List<MarkerOptions> markers = this.getMarkers(i);

                    for (int j = 0; j < markers.size(); j++) {
                        this.map.addMarker(markers.get(j));
                    }
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        // shows both the satellite and the places name
        this.map.setMapType(googleMap.MAP_TYPE_NORMAL);

        // shows the zoom buttons on the corner of the map
        this.map.getUiSettings().setZoomControlsEnabled(true);
        this.map.getUiSettings().setMyLocationButtonEnabled(true);
        this.map.getUiSettings().setMapToolbarEnabled(true);
        this.map.getUiSettings().setRotateGesturesEnabled(false);

        // Add a marker in Sydney and move the camera
        LatLng natal = new LatLng(-5.814940, -35.222929);
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(natal, 12));
    }


    private List<MarkerOptions> getMarkers(int index){
        List<MarkerOptions> markers = new ArrayList<>();

        // TODO : GET DYNAMICALLY
        if(options[index].equals("Veterinarios")){
            for (int i = 0; i < this.vetLocations.length; i++) {
                MarkerOptions marker = new MarkerOptions();
                marker.title(this.vetLocations[i].getPlaceName())
                        .position(new LatLng(this.vetLocations[i].getLatitude(), this.vetLocations[i].getLongitude()))
                        .icon(BitmapDescriptorFactory.defaultMarker(colors[index]));

                markers.add(marker);
            }
        } else if(options[index].equals("Restaurantes")){
            for (int i = 0; i < this.restaurantsLocations.length; i++) {
                MarkerOptions marker = new MarkerOptions();
                marker.title(this.restaurantsLocations[i].getPlaceName())
                        .position(new LatLng(this.restaurantsLocations[i].getLatitude(), this.restaurantsLocations[i].getLongitude()))
                        .icon(BitmapDescriptorFactory.defaultMarker(colors[index]));

                markers.add(marker);
            }
        } else if(options[index].equals("Praias")){
            for (int i = 0; i < this.beachesLocations.length; i++) {
                MarkerOptions marker = new MarkerOptions();
                marker.title(this.beachesLocations[i].getPlaceName())
                        .position(new LatLng(this.beachesLocations[i].getLatitude(), this.beachesLocations[i].getLongitude()))
                        .icon(BitmapDescriptorFactory.defaultMarker(colors[index]));

                markers.add(marker);
            }
        } else if(options[index].equals("Shoppings")){
            for (int i = 0; i < this.shoppingLcations.length; i++) {
                MarkerOptions marker = new MarkerOptions();
                marker.title(this.shoppingLcations[i].getPlaceName())
                        .position(new LatLng(this.shoppingLcations[i].getLatitude(), this.shoppingLcations[i].getLongitude()))
                        .icon(BitmapDescriptorFactory.defaultMarker(colors[index]));

                markers.add(marker);
            }
        } else if(options[index].equals("Hotéis")){
            for (int i = 0; i < this.hotelLcations.length; i++) {
                MarkerOptions marker = new MarkerOptions();
                marker.title(this.hotelLcations[i].getPlaceName())
                        .position(new LatLng(this.hotelLcations[i].getLatitude(), this.hotelLcations[i].getLongitude()))
                        .icon(BitmapDescriptorFactory.defaultMarker(colors[index]));

                markers.add(marker);
            }
        }

        return markers;
    }
}
