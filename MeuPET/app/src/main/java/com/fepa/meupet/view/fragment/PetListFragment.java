package com.fepa.meupet.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fepa.meupet.R;
import com.fepa.meupet.control.adapter.PetItemAdapter;
import com.fepa.meupet.model.agent.pet.Pet;
import com.fepa.meupet.model.environment.constants.GeneralConfig;
import com.fepa.meupet.view.activity.PetActivity;
import com.fepa.meupet.view.activity.RegisterPetActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PetListFragment extends ListFragment implements ActionMode.Callback, AdapterView.OnItemLongClickListener {

    private PetItemAdapter adapter;
    private ActionMode actionMode;
    private ListView listView;

    private int itemSelected = -1;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ChildEventListener childEventListener;

    public PetListFragment() {
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
        // inflates the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pet_list, container, false);

        this.listView = view.findViewById(android.R.id.list);
        this.listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        this.adapter = new PetItemAdapter(getContext());
        this.listView.setAdapter(adapter);

        this.listView.setOnItemLongClickListener(this);

        this.database = FirebaseDatabase.getInstance();
        this.auth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.adapter.clearAdapter();
        this.populateListFromDB();
    }

    private void populateListFromDB(){
        String email = this.auth.getCurrentUser().getEmail().replace(".", "");

        String key = this.database.getReference("miauBD/person")
                .child(email)
                .getKey();

        this.reference = this.database
                .getReference("miauBD/person/"+key);

        this.childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String chave = dataSnapshot.getKey();
                Pet pet = dataSnapshot.getValue(Pet.class);

                adapter.addItem(pet);
                adapter.notifyDataSetChanged();

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

        this.reference.addChildEventListener(this.childEventListener);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.pet_main_action, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.actPetAdd :
                Intent intent = new Intent(getContext(), RegisterPetActivity.class);
                startActivityForResult(intent,GeneralConfig.Pets.PET_ADD_REQUEST_CODE);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GeneralConfig.Pets.PET_ADD_REQUEST_CODE && resultCode == GeneralConfig.RESULT_OK){
            Pet pet = (Pet) data.getSerializableExtra(GeneralConfig.Pets.PET_BUNDLE);

            this.adapter.addItem(pet);
            this.adapter.notifyDataSetChanged();

            Toast.makeText(getContext(), "Pet Adicionado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.pet_click_action, menu);
        this.actionMode = actionMode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        menu.findItem(R.id.actPetEdit).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.actPetDel).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        // if no item is selected returns
        if (this.itemSelected < 0){
            return false;
        }

        switch (menuItem.getItemId()){
            case R.id.actPetEdit :
                Toast.makeText(getContext(), "Editing item", Toast.LENGTH_SHORT).show();

                // updates item color
                this.setItemColor(this.itemSelected, Color.TRANSPARENT);
                break;

            case R.id.actPetDel :
                // updates item color
                this.setItemColor(this.itemSelected, Color.TRANSPARENT);

                this.adapter.removeItem(this.itemSelected);
                break;

            default:
                return false;
        }
        this.actionMode.finish();
        this.adapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        final int LISTVIEW_SIZE = this.listView.getChildCount();

        // resets every item to not selected state color
        for (int i = 0; i < LISTVIEW_SIZE; i++) {
            this.listView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }

        // resets the item selected position
        this.itemSelected = -1;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(getContext(), PetActivity.class);
        Pet pet = (Pet) this.adapter.getItem(position);
        intent.putExtra(GeneralConfig.Pets.PET_BUNDLE, pet);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        // if no item was selected
        if (this.itemSelected < 0){

            // selects item
            this.itemSelected = i;

            // updates item color
            this.setItemColor(this.itemSelected, GeneralConfig.ITEM_SELECTED_COLOR);

            // starts the action mode
            getActivity().startActionMode(this);

        } else if (this.itemSelected == i){

            // updates item color
            this.setItemColor(this.itemSelected, Color.TRANSPARENT);

            // deselects item
            this.itemSelected = -1;
            this.actionMode.finish();
        }
        return true;
    }

    private void setItemColor(int position, int color){
        int wantedPosition = this.getListViewItemPosition(position);
        if (wantedPosition == -1){
            return;
        }
        this.listView.getChildAt(wantedPosition).setBackgroundColor(color);

    }

    private int getListViewItemPosition(int wantedPosition){
        int firstPosition = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount();

        int relativePosition = wantedPosition - firstPosition;

        if (relativePosition < 0 || relativePosition >= listView.getChildCount()) {
            return -1;
        }
        return relativePosition;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (this.childEventListener != null){
            this.reference.removeEventListener(this.childEventListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (this.childEventListener != null){
            this.reference.removeEventListener(this.childEventListener);
        }
    }
}
