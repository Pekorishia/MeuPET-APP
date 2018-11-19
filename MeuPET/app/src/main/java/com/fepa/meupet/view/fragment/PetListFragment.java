package com.fepa.meupet.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fepa.meupet.R;
import com.fepa.meupet.view.adapter.PetItemAdapter;

public class PetListFragment extends Fragment {
    public static final String TAG = PetListFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COLOR = "color";

    // TODO: Rename and change types of parameters
    private int color;

    private RecyclerView recyclerView;

    public PetListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            color = getArguments().getInt(ARG_COLOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pet_list, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.pet_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setBackgroundColor(getLighterColor(color));

        PetItemAdapter adapter = new PetItemAdapter(getContext());
        recyclerView.setAdapter(adapter);

        return rootView;
    }


    /**
     * Updates {@link RecyclerView} background color upon changing Bottom Navigation item.
     *
     * @param color to apply to {@link RecyclerView} background.
     */
    public void updateColor(int color) {
        recyclerView.setBackgroundColor(getLighterColor(color));
    }

    /**
     * Facade to return colors at 30% opacity.
     *
     * @param color
     * @return
     */
    private int getLighterColor(int color) {
        return Color.argb(30,
                Color.red(color),
                Color.green(color),
                Color.blue(color)
        );
    }


}
