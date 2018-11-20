package com.fepa.meupet.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fepa.meupet.R;
import com.fepa.meupet.control.adapter.PetItemAdapter;
import com.fepa.meupet.model.agent.pet.Pet;

import java.util.ArrayList;
import java.util.List;

public class PetListFragment extends ListFragment {

    private List<Pet> petList;
    private ListView listView;
    private PetItemAdapter adapter;

    public PetListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflates the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pet_list, container, false);

        this.listView = view.findViewById(android.R.id.list);

        this.populateList();

        this.adapter = new PetItemAdapter(getContext(), petList);
        listView.setAdapter(adapter);

        return view;
    }


    private void populateList(){
        petList = new ArrayList<>();

        String[] nameArray = getContext().getResources().getStringArray(R.array.animal_names);

        final int SIZE = nameArray.length;

        for (int i = 0; i < SIZE; i++) {
            Pet pet = new Pet(nameArray[i]);
            this.petList.add(pet);
        }
    }
}
