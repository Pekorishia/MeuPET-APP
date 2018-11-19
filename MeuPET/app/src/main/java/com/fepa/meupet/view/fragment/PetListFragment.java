package com.fepa.meupet.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fepa.meupet.R;
import com.fepa.meupet.control.adapter.PetItemAdapter;

public class PetListFragment extends Fragment {

    private RecyclerView recyclerView;

    public PetListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            color = getArguments().getInt(ARG_COLOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflates the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pet_list, container, false);

        this.recyclerView = view.findViewById(R.id.pet_list_recycler_view);

        this.recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)
        );

        PetItemAdapter adapter = new PetItemAdapter(getContext());
        this.recyclerView.setAdapter(adapter);

        return view;
    }

}
