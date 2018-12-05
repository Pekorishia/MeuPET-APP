package com.fepa.meupet.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fepa.meupet.R;
import com.fepa.meupet.control.adapter.NotificationItemAdapter;
import com.fepa.meupet.model.environment.constants.GeneralConfig;
import com.fepa.meupet.model.environment.notification.Notification;

public class CalendarFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private NotificationItemAdapter adapter;

    private BroadcastReceiver receiver;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        this.listView = view.findViewById(android.R.id.list);
        this.setupListView();

        this.receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Notification notification = (Notification) intent
                        .getSerializableExtra(GeneralConfig.Notifications.NOTIFICATION_BUNDLE);

                adapter.addItem(notification);
                adapter.notifyDataSetChanged();
            }
        };

        return view;
    }


    private void setupListView(){
        this.listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        this.adapter = new NotificationItemAdapter(getContext());
        this.listView.setAdapter(adapter);

        this.listView.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GeneralConfig.Notifications.BROADCAST_NOTIFICATION);
        getActivity().registerReceiver(this.receiver, filter);
    }

    @Override
    public void onDestroy() {
        if (this.receiver != null){
            getActivity().unregisterReceiver(this.receiver);
        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
    }
}
