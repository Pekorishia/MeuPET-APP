package com.fepa.meupet.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fepa.meupet.R;
import com.fepa.meupet.model.agent.pet.Pet;
import com.fepa.meupet.model.environment.constants.GeneralConfig;

public class RegisterPetActivity extends AppCompatActivity {

    private EditText edName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pet);

        this.edName = this.findViewById(R.id.etPetName);
    }

    public void onImageButtonClick(View view) {
        this.openImagePicker();
    }

    public void onRegisterClick(View view) {
        String petname = this.edName.getText().toString();

        if (petname != "" && petname != null){
            Pet pet = new Pet(petname);

            Intent intent = new Intent();
            intent.putExtra(GeneralConfig.Pets.PET_BUNDLE, pet);
            setResult(GeneralConfig.RESULT_OK, intent);
            finish();
        }
    }

    private void openImagePicker(){
        // TODO
    }

}
