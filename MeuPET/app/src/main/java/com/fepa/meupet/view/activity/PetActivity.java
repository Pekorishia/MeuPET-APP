package com.fepa.meupet.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fepa.meupet.R;
import com.fepa.meupet.control.adapter.NotificationItemAdapter;
import com.fepa.meupet.control.dialog.AddNotificationDialog;
import com.fepa.meupet.control.dialog.EditPetInfoDialog;
import com.fepa.meupet.model.agent.pet.Pet;
import com.fepa.meupet.model.environment.constants.GeneralConfig;
import com.fepa.meupet.model.environment.notification.Notification;
import com.fepa.meupet.model.environment.permissions.Permissions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PetActivity extends AppCompatActivity implements EditPetInfoDialog.EditPetInfoDialogListener, AdapterView.OnItemClickListener, OnMapReadyCallback{

    private Pet pet;
    private GoogleMap map;
    private ListView listView;
    private BroadcastReceiver receiver;
    private NotificationItemAdapter adapter;

    private TextView tvPetAge;
    private TextView tvPetSex;
    private TextView tvPetBreed;
    private TextView tvPetHeight;
    private TextView tvPetWeight;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.fPetMap);
        mapFragment.getMapAsync(this);

        this.tvPetAge = this.findViewById(R.id.tvPetAge);
        this.tvPetSex = this.findViewById(R.id.tvPetSex);
        this.tvPetBreed = this.findViewById(R.id.tvPetBreed);
        this.tvPetHeight = this.findViewById(R.id.tvPetHeight);
        this.tvPetWeight = this.findViewById(R.id.tvPetWeight);

        this.database = FirebaseDatabase.getInstance();
        this.auth = FirebaseAuth.getInstance();

        ImageView ivPet = this.findViewById(R.id.ivPet);
        TextView tvPetName = this.findViewById(R.id.tvPetName);
        ImageView ivAddNotification = this.findViewById(R.id.ivAddNotification);
        ImageView ivEditPetInfo = this.findViewById(R.id.ivEditPetInfo);

        // receives the intent from PetListFragment
        Intent it = getIntent();

        // gets the Pet from the bundle
        this.pet = (Pet) it.getSerializableExtra(GeneralConfig.Pets.PET_BUNDLE);

        // if the bundle wasn't null
        if (this.pet != null){
            tvPetName.setText(this.pet.getName());

            if (this.pet.getPhotoPath() == null || this.pet.getPhotoPath().equals(""))
                ivPet.setImageResource(R.mipmap.ic_launcher_round);
            else
                ivPet.setImageBitmap(BitmapFactory.decodeFile(pet.getPhotoPath()));

            // updates the info fields
            this.updatePetInfo();

            // changes the title of the activity
            actionBar.setTitle(pet.getName());
        }

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

        ivAddNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNotificationDialog notificationDialog = new AddNotificationDialog();
                notificationDialog.show(getSupportFragmentManager(),"notificationDialog");
            }
        });

        ivEditPetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditPetInfoDialog editPetInfoDialog = new EditPetInfoDialog();
                Bundle data = new Bundle();
                data.putSerializable("pet", pet);
                editPetInfoDialog.setArguments(data);
                editPetInfoDialog.show(getSupportFragmentManager(),"EditPetDialog");
            }
        });
    }

    private void setupListView(){

        this.listView = this.findViewById(android.R.id.list);
        this.listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        this.adapter = new NotificationItemAdapter(this);
        this.listView.setAdapter(adapter);

        this.listView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GeneralConfig.Notifications.BROADCAST_NOTIFICATION);
        registerReceiver(this.receiver, filter);
    }

    @Override
    protected void onDestroy() {
        if (this.receiver != null){
            unregisterReceiver(this.receiver);
        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void onFinishEditPetDialog(Pet _pet) {
        this.pet = _pet;
        this.updatePetDB();
        this.updatePetInfo();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        // shows both the satellite and the places name
        this.map.setMapType(googleMap.MAP_TYPE_NORMAL);

        // sets the ui interaction that the map will have
        this.map.getUiSettings().setZoomControlsEnabled(true);
        this.map.getUiSettings().setMyLocationButtonEnabled(true);
        this.map.getUiSettings().setMapToolbarEnabled(true);
        this.map.getUiSettings().setRotateGesturesEnabled(false);

        // automatically updates the pet location
        this.updatePetLocation();
    }

    private void updatePetLocation(){
        String email = this.auth.getCurrentUser().getEmail().replace(".", "");

        this.reference = this.database
                .getReference(GeneralConfig.DB_PATH_COLLAR);

        this.childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String location = dataSnapshot.child("location").getValue().toString();

                if (!location.equals("")){
                    // creates a new marker
                    MarkerOptions marker = new MarkerOptions();

                    // split the received string into latitude and longitude values
                    String[] locationValues = location.split(",");
                    LatLng petLocation = new LatLng(Double.valueOf(locationValues[0]), Double.valueOf(locationValues[1]));

                    // puts the marker on the pet location
                    marker.title(pet.getName())
                            .position(petLocation)
                            .icon(BitmapDescriptorFactory.defaultMarker(30));


                    // shows it on the map
                    map.addMarker(marker);

                    // moves the camera there
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(petLocation, 12));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };

        this.reference.orderByKey().limitToLast(5).addChildEventListener(this.childEventListener);
    }

    private void updatePetDB(){
        String email = this.auth.getCurrentUser().getEmail().replace(".", "");

        String key = this.database.getReference(GeneralConfig.DB_PATH_PERSON)
                .child(email)
                .getKey();

        this.reference = this.database
                .getReference(GeneralConfig.DB_PATH_PERSON+key);

        this.reference.orderByChild("name")
                .equalTo(this.pet.getName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child: dataSnapshot.getChildren())
                        {
                            String key = child.getKey().toString();
                            Map<String, Object> update = new HashMap<>();
                            update.put(key, pet);
                            reference.updateChildren(update);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void updatePetInfo(){
        this.tvPetAge.setText(this.pet.getAge() + " " + getString(R.string.pet_info_age));
        this.tvPetSex.setText(this.pet.getSex());
        this.tvPetBreed.setText(this.pet.getBreed());
        this.tvPetHeight.setText(this.pet.getHeight() + " " + getString(R.string.pet_info_height));
        this.tvPetWeight.setText(this.pet.getWeight() + " " + getString(R.string.pet_info_weight));
    }
}
