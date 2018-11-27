package com.fepa.meupet.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.Toast;

import com.fepa.meupet.R;
import com.fepa.meupet.control.adapter.PetItemAdapter;
import com.fepa.meupet.model.agent.pet.Pet;
import com.fepa.meupet.model.environment.constants.GeneralConfig;
import com.fepa.meupet.view.activity.RegisterPetActivity;

public class PetListFragment extends ListFragment implements ActionMode.Callback {

    private PetItemAdapter adapter;
    private ActionMode actionMode;
    private ListView listView;

    private int itemSelected = -1;

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

        // Set the emptyView to the ListView
        listView.setEmptyView(getActivity().findViewById(R.id.emptyElement));

        return view;
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
                startActivityForResult(intent,GeneralConfig.PET_ADD_REQUEST_CODE);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GeneralConfig.PET_ADD_REQUEST_CODE && resultCode == GeneralConfig.RESULT_OK){
            Pet pet = (Pet) data.getSerializableExtra(GeneralConfig.PET_BUNDLE);

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

                // removes item
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

        // if no item was selected
        if (this.itemSelected < 0){

            // selects item
            this.itemSelected = position;

            // updates item color
            this.setItemColor(this.itemSelected, GeneralConfig.ITEM_SELECTED_COLOR);

            // starts the action mode
            getActivity().startActionMode(this);

        } else if (this.itemSelected == position){

            // updates item color
            this.setItemColor(this.itemSelected, Color.TRANSPARENT);

            // deselects item
            this.itemSelected = -1;
            this.actionMode.finish();
        }
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
}
