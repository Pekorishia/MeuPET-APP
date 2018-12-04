package com.fepa.meupet.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.fepa.meupet.R;
import com.fepa.meupet.control.adapter.PetItemAdapter;
import com.fepa.meupet.view.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private FirebaseAuth auth;

    public SettingsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflates the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        this.auth = FirebaseAuth.getInstance();

        TextView textView = view.findViewById(R.id.tvLogout);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        return view;
    }
}
