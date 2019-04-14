package com.fepa.meupet.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fepa.meupet.R;
import com.fepa.meupet.model.agent.pet.Pet;
import com.fepa.meupet.model.environment.constants.GeneralConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPetActivity extends AppCompatActivity {

    private String imagePath;

    private EditText edName;
    private ImageView ivPetPhoto;

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pet);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }

        this.edName = this.findViewById(R.id.etPetName);
        this.ivPetPhoto = this.findViewById(R.id.ivPetPhoto);
        this.ivPetPhoto.setImageResource(R.drawable.add_photo);

        this.database = FirebaseDatabase.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }

    public void onPetPhotoClick(View view) {
        Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(it, "Abrir com"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){
            Uri imageURI = data.getData();

            if(imageURI != null){
                Cursor cursor = null;

                try {
                    String[] proj = { MediaStore.Images.Media.DATA };
                    cursor = getApplicationContext().getContentResolver().query(imageURI, proj, null, null, null);

                    int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    imagePath = cursor.getString(index);

                    this.ivPetPhoto.setImageURI(imageURI);

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }
    }

    public void onRegisterClick(View view) {
        String petName = this.edName.getText().toString();

        if (petName != "" && petName != null){
            Pet pet = new Pet(petName);
            pet.setPhotoPath(this.imagePath);

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

}
