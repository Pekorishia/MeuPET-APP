package com.fepa.meupet.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fepa.meupet.R;
import com.fepa.meupet.control.adapter.CustomPagerAdapter;
import com.fepa.meupet.control.general.CustomSwipePager;
import com.fepa.meupet.model.environment.constants.GeneralConfig;
import com.fepa.meupet.model.environment.enums.SwipeDirection;


public class PetFragment extends Fragment {

    private CustomSwipePager viewPager;
    private CustomPagerAdapter adapter;

    public PetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet, container, false);

        this.viewPager = view.findViewById(R.id.viewPetPager);
        this.adapter = new CustomPagerAdapter(getActivity().getSupportFragmentManager());

        this.setupViewPagerAndPagerAdapter();

        return view;
    }

    public void setupViewPagerAndPagerAdapter() {
        this.viewPager = viewPager;

        // disables swiping
        this.viewPager.setAllowedSwipeDirection(SwipeDirection.none);

        // manages pagerAdapter
        this.setupPagerAdapter();

        // attaches the pageAdapter as the viewPager adapter
        this.viewPager.setAdapter(this.adapter);

        viewPager.setCurrentItem(GeneralConfig.START_PET_TAB);
    }

    private void setupPagerAdapter(){
        this.adapter.addFragments(new PetListFragment());
        this.adapter.addFragments(new EmptyPetFragment());
    }
}
