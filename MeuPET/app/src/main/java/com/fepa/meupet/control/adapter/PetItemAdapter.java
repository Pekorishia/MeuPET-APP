package com.fepa.meupet.control.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fepa.meupet.R;
import com.fepa.meupet.model.agent.pet.Pet;

import java.util.List;

public class PetItemAdapter extends BaseAdapter {

    private List<Pet> petList;
    private Context context;


    public PetItemAdapter(Context context, List<Pet> petList){
        this.context = context;
        this.petList = petList;
    }

    @Override
    public int getCount() {
        return (petList != null ? petList.size() : 0);
    }

    @Override
    public Object getItem(int i) {
        return petList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            view = View.inflate(context, R.layout.fragment_pet_item, null);
        }

        Pet pet = petList.get(i);

        ImageView ivImage = view.findViewById(R.id.ivPetRowItem);
        TextView tvName = view.findViewById(R.id.tvPetRowItem);

        ivImage.setImageResource(R.mipmap.ic_launcher);
        tvName.setText(pet.getName());

        return view;
    }
}
