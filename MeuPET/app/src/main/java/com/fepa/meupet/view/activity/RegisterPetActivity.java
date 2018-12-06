package com.fepa.meupet.view.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fepa.meupet.R;
import com.fepa.meupet.model.agent.pet.Pet;
import com.fepa.meupet.model.environment.constants.GeneralConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterPetActivity extends AppCompatActivity {

    private EditText edName;
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pet);

        this.edName = this.findViewById(R.id.etPetName);
        this.database = FirebaseDatabase.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }

    public void onImageButtonClick(View view) {
        this.openImagePicker();
    }

    public void onRegisterClick(View view) {
        String petname = this.edName.getText().toString();

        if (petname != "" && petname != null){
            Pet pet = new Pet(petname);

            this.savePet(pet);
        }
    }

    private void savePet(final Pet pet){
        String email = this.auth.getCurrentUser().getEmail().replace(".", "");

        String key = this.database.getReference("miauBD/person")
                .child(email)
                .getKey();

        DatabaseReference reference = this.database
                .getReference("miauBD/person/"+key);
        
        reference.push().setValue(pet).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    sendIntentResult(pet);
                else
                    Toast.makeText(RegisterPetActivity.this, R.string.pet_register_error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendIntentResult(Pet pet){
        Intent intent = new Intent();
        intent.putExtra(GeneralConfig.Pets.PET_BUNDLE, pet);
        setResult(GeneralConfig.RESULT_OK, intent);
        finish();
    }

    private void openImagePicker(){
        // TODO
    }

}
