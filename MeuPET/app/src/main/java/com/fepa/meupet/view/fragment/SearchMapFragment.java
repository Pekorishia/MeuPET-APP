package com.fepa.meupet.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fepa.meupet.R;
import com.fepa.meupet.control.general.Geolocation;
import com.fepa.meupet.model.environment.constants.GeneralConfig;
import com.fepa.meupet.view.activity.MapsActivity;

import java.util.HashMap;
import java.util.Map;


public class SearchMapFragment extends ListFragment {

    // maps <place name, location>
    private Map<String, Geolocation> locations = new HashMap<>();
    private ArrayAdapter<String> adapter;

    public SearchMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);

        this.setupLocations();

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String placeName = this.adapter.getItem(position);
        Geolocation location = this.locations.get(placeName);

        Intent intent = new Intent(getContext(), MapsActivity.class);
        intent.putExtra(GeneralConfig.PLACE_NAME_KEY, placeName);
        intent.putExtra(GeneralConfig.PLACE_LOCATION_KEY, location);

        startActivity(intent);
    }

    // TODO: MAKE IT DYNAMICALLY
    private void setupLocations(){
        locations.put("Natal",new Geolocation(-5.7792569,-35.200916));
        locations.put("João Pessoa",new Geolocation(-7.1194958,-34.8450118));
        locations.put("Salvador",new Geolocation(-12.9722184,-38.5014136));
        locations.put("Recife",new Geolocation(-8.0475622,-34.8769643));
        locations.put("Teresina",new Geolocation(-5.0920108,-42.8037597));
        locations.put("Aracajú",new Geolocation(-10.9472468,-37.7089492));
        locations.put("Maceió",new Geolocation(-9.6498587,-35.7089492));
        locations.put("São Luís",new Geolocation(-2.5391099,-44.2829046));
        locations.put("Fortaleza",new Geolocation(-3.7319029,-38.5267393));

        adapter.addAll(this.locations.keySet());
    }
}
