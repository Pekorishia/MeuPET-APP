package com.fepa.meupet.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PetActivity extends AppCompatActivity implements EditPetInfoDialog.EditPetInfoDialogListener, AdapterView.OnItemClickListener, OnMapReadyCallback{

    private Pet pet;
    private GoogleMap map;
    private ListView listView;
    private ActionBar actionBar;
    private LineChart lcActivitylvl;
    private LineChart lcEatingHabits;
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

        SupportMapFragment mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.fPetMap);
        mapFragment.getMapAsync(this);

        // initiates local variables
        this.startVariables();

        // handles the pet information
        this.petInfoHandler();

        // handles the activity level chart
        this.chartHandler(this.lcActivitylvl, GeneralConfig.Pets.ACTIVITY_LVL_CHART);

        // handles the eating habits chart
        this.chartHandler(this.lcEatingHabits, GeneralConfig.Pets.EATING_HABITS_CHART);

        // handles notification
        this.notificationHandler();

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

    private void startVariables(){
        this.actionBar = getSupportActionBar();
        this.actionBar.setDisplayHomeAsUpEnabled(true);

        this.tvPetAge = this.findViewById(R.id.tvPetAge);
        this.tvPetSex = this.findViewById(R.id.tvPetSex);
        this.tvPetBreed = this.findViewById(R.id.tvPetBreed);
        this.tvPetHeight = this.findViewById(R.id.tvPetHeight);
        this.tvPetWeight = this.findViewById(R.id.tvPetWeight);
        this.lcActivitylvl = this.findViewById(R.id.lcActivityLevel);
        this.lcEatingHabits = this.findViewById(R.id.lcEatingHabits);

        this.database = FirebaseDatabase.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }

    private void notificationHandler(){

        ImageView ivAddNotification = this.findViewById(R.id.ivAddNotification);

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
    }

    private void setupListView(){

        this.listView = this.findViewById(android.R.id.list);
        this.listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        this.adapter = new NotificationItemAdapter(this);
        this.listView.setAdapter(adapter);

        this.listView.setOnItemClickListener(this);
    }

    private void petInfoHandler(){
        ImageView ivPet = this.findViewById(R.id.ivPet);
        TextView tvPetName = this.findViewById(R.id.tvPetName);
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

    private void chartHandler(LineChart chart, int chartType){

        //sets all charts parameters modifiers
        this.setChartParameters(chart);

        this.setXAxis(chart);

        if (chartType == GeneralConfig.Pets.ACTIVITY_LVL_CHART){
            this.setActivityLevelData(chart);
            this.setYAxis(chart, 60);
        }
        else if (chartType == GeneralConfig.Pets.EATING_HABITS_CHART){
            this.setEatingHabitsData(chart);
            this.setYAxis(chart, 500);
        }
    }

    private void setChartParameters(LineChart chart){
        // removes description text
        chart.getDescription().setEnabled(false);

        chart.setDragDecelerationFrictionCoef(0.9f);

        // enables touch, scaling and dragging
        chart.setPinchZoom(true);
        chart.setDragEnabled(true);
        chart.setTouchEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);
    }

    private void setXAxis(LineChart chart){
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.rgb(0, 0, 0));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one hour
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.ENGLISH);
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });
    }

    private void setYAxis(LineChart chart, int maxValue){
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0);
        leftAxis.setAxisMaximum(maxValue);
        leftAxis.setTextColor(Color.rgb(0, 0, 0));

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void setActivityLevelData(final LineChart chart) {


        // gets all dates and activity levels
        // create a LinkedHashMap < string dates, activity levels>
        // gets the first data string to be the start time
        // counts the number of highs for every hour and create a LinkedHashMap <init date for every hour, number of highs>
        // populate the dataset with the last map

        String input = " 07 Apr 2019 21:51:31 ";
        Date date = null;
        try {
            date = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH).parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milliseconds = TimeUnit.MILLISECONDS.toHours(date.getTime());

        // now in hours
//        long now = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());

        ArrayList<Entry> values = new ArrayList<>();

        // count = hours
        float to = (float) milliseconds + 24;

        // increment by 1 hour
        for (float x = milliseconds; x < to; x++) {

            float y = (float) (Math.random() * 60) + 0;
            values.add(new Entry(x, y)); // add one entry per hour
        }

        // create a dataset and give it a type
        LineDataSet dataSet = new LineDataSet(values, getString(R.string.pet_activity_lvl_dataSet));
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setColor(ColorTemplate.getHoloBlue());
        dataSet.setLineWidth(1.5f);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(true);
        dataSet.setDrawCircleHole(false);
        dataSet.setCircleColor(Color.rgb(160,0,170));

        // sets the filled area
        dataSet.setDrawFilled(true);
        dataSet.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return chart.getAxisLeft().getAxisMinimum();
            }
        });

        // sets color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_activity);
            dataSet.setFillDrawable(drawable);
        } else {
            dataSet.setFillColor(Color.BLACK);
        }

        // creates a data object with the data sets
        LineData data = new LineData(dataSet);

        // plots the chart
        chart.setData(data);
    }

    private void setEatingHabitsData(final LineChart chart) {

        String input = " 07 Apr 2019 21:51:31 ";
        Date date = null;
        try {
            date = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH).parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milliseconds = TimeUnit.MILLISECONDS.toHours(date.getTime());

        // now in hours
//        long now = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());

        ArrayList<Entry> values = new ArrayList<>();

        // count = hours
        float to = (float) milliseconds + 24;
        int z = 500;

        // increment by 1 hour
        for (float x = milliseconds; x < to; x++) {

            z -= 20;
            values.add(new Entry(x, z)); // add one entry per hour
        }

        // create a dataset and give it a type
        LineDataSet dataSet = new LineDataSet(values, getString(R.string.pet_eating_habit_dataSet));
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
//        dataSet.setColor(ColorTemplate.getHoloBlue());
        dataSet.setColors(Color.rgb(0,0,0), Color.argb(77, 0,0,0));
        dataSet.setLineWidth(1.5f);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(true);
        dataSet.setDrawCircleHole(false);
        dataSet.setCircleColor(Color.rgb(0,0,0));

        // sets the filled area
        dataSet.setDrawFilled(true);
        dataSet.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return chart.getAxisLeft().getAxisMinimum();
            }
        });

        // sets color of filled area
        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_habits);
            dataSet.setFillDrawable(drawable);
        } else {
            dataSet.setFillColor(Color.BLACK);
        }

        // create a data object with the data sets
        LineData data = new LineData(dataSet);

        // set data
        chart.setData(data);
        chart.invalidate();
    }

}
