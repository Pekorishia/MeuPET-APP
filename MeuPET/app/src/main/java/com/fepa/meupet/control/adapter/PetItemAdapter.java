package com.fepa.meupet.control.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fepa.meupet.R;
import com.fepa.meupet.model.agent.animal.Animal;

import java.util.ArrayList;
import java.util.List;

public class PetItemAdapter extends RecyclerView.Adapter<PetItemAdapter.ItemViewHolder> {

    private List<Animal> animals = new ArrayList<>();

    private Activity activity;

    public PetItemAdapter(Activity activity){
        this.activity = activity;

        this.populateList();
    }

    private void populateList(){
        String[] nameArray = this.activity.getApplicationContext().getResources().getStringArray(R.array.animal_names);

        final int SIZE = nameArray.length;

        for (int i = 0; i < SIZE; i++) {
            Animal animal = new Animal(nameArray[i]);
            this.animals.add(animal);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.fragment_pet_item_list,viewGroup,false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        Animal animal = animals.get(i);

        itemViewHolder.tvName.setText(animal.getName());
    }

    @Override
    public int getItemCount() {
        return animals != null ? animals.size() : 0;
    }


    protected class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        protected TextView tvName;

        private boolean actionModeActive;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.tvPetRowItem);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            animals.remove(position);
            notifyItemRemoved(position);

            Toast.makeText(activity.getApplicationContext(), "CLICK", Toast.LENGTH_SHORT).show();

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
