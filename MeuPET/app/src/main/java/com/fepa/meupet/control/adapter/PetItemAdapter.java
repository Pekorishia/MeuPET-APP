package com.fepa.meupet.control.adapter;

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

    private Context context;

    public PetItemAdapter(Context context){
        this.context = context;

        this.populateList();
    }

    private void populateList(){
        String[] nameArray = context.getResources().getStringArray(R.array.animal_names);

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


    protected class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView tvName;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvPetName);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            animals.remove(position);

            Toast.makeText(context, "REMOVIDO", Toast.LENGTH_SHORT).show();
            notifyItemRemoved(position);
        }
    }
}
