package com.fepa.meupet.control.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fepa.meupet.R;
import com.fepa.meupet.model.agent.pet.Pet;

public class PetItemAdapter extends BaseAdapter {

    private ArrayAdapter<Pet> petList;
    private Context context;

    private static class ViewHolder {
        public TextView text;
        public ImageView image;
    }


    public PetItemAdapter(Context context){
        this.context = context;
        this.petList = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);;

        this.populateList();
    }

    private void populateList(){
        String[] nameArray = this.context.getResources().getStringArray(R.array.animal_names);

        final int SIZE = nameArray.length;

        for (int i = 0; i < SIZE; i++) {
            Pet pet = new Pet(nameArray[i]);
            this.petList.add(pet);
        }
    }

    public void addItem(Pet pet) {
        this.petList.add(pet);
    }

    public void editItem(int position, Pet pet) {
        this.removeItem(position);
        this.petList.insert(pet, position);
    }

    public void removeItem(int position) {
        this.petList.remove(this.petList.getItem(position));
    }

    @Override
    public int getCount() {
        return (this.petList != null ? this.petList.getCount() : 0);
    }

    @Override
    public Object getItem(int i) {
        return this.petList.getItem(i);
    }

    @Override
    public long getItemId(int i) {
        return this.petList.getItemId(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            view = View.inflate(context, R.layout.fragment_pet_list_item, null);
            // configures view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = view.findViewById(R.id.tvPetRowItem);
            viewHolder.image = view.findViewById(R.id.ivPetRowItem);
            view.setTag(viewHolder);
        }

        Pet pet = petList.getItem(i);

        // fills data
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.image.setImageResource(R.mipmap.ic_launcher_round);
        holder.text.setText(pet.getName());

        return view;
    }
}
