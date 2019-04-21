package com.fepa.meupet.control.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fepa.meupet.R;
import com.fepa.meupet.model.agent.pet.Pet;
import com.fepa.meupet.model.environment.constants.GeneralConfig;
import com.fepa.meupet.model.environment.notification.Notification;

public class EditPetInfoDialog extends DialogFragment {

    private Pet pet;

    private EditPetInfoDialogListener listener;

    private EditText etEditAge;
    private EditText etEditBreed;
    private EditText etEditHeight;
    private EditText etEditWeight;

    private RadioGroup rgEditSex;
    private RadioButton rbEditSex;
    private RadioButton rbEditFemale;
    private RadioButton rbEditMale;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the EditNameDialogListener so we can send events to the host
            listener = (EditPetInfoDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement EditPetInfoDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_pet,null);

        pet = (Pet) getArguments().getSerializable("pet");

        this.etEditAge = view.findViewById(R.id.etDialogAge);
        this.etEditBreed = view.findViewById(R.id.etDialogBreed);
        this.etEditHeight = view.findViewById(R.id.etDialogHeight);
        this.etEditWeight = view.findViewById(R.id.etDialogWeight);
        this.rgEditSex = view.findViewById(R.id.rgDialogSex);
        this.rbEditFemale = view.findViewById(R.id.rbDialogFemale);
        this.rbEditMale = view.findViewById(R.id.rbDialogMale);

        this.completeFields();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.notification_dialog_title);
        builder.setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!etEditAge.getText().toString().equals(""))
                    pet.setAge(etEditAge.getText().toString());
                if (!etEditBreed.getText().toString().equals(""))
                    pet.setBreed(etEditBreed.getText().toString());
                if (!etEditHeight.getText().toString().equals(""))
                    pet.setHeight(etEditHeight.getText().toString());
                if (!etEditWeight.getText().toString().equals(""))
                    pet.setWeight(etEditWeight.getText().toString());

                // get selected radio button from radioGroup
                int selectedId = rgEditSex.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                rbEditSex = (RadioButton) view.findViewById(selectedId);
                String sex_name = rbEditSex.getText().toString();

                if(sex_name.equals(getString(R.string.dialog_pet_female)))
                    pet.setSex(getString(R.string.dialog_pet_female));
                else
                    pet.setSex(getString(R.string.dialog_pet_male));

                // Return input text to activity
                listener.onFinishEditPetDialog(pet);
                //dismiss();
            }
        });

        builder.setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    private void completeFields(){
        if (this.pet.getBreed()!= null && !this.pet.getBreed().equals(""))
            this.etEditBreed.setText(this.pet.getBreed());

        if (this.pet.getSex().equals(getString(R.string.dialog_pet_female)))
            this.rbEditFemale.setChecked(true);
        else
            this.rbEditMale.setChecked(true);

    }


    public interface EditPetInfoDialogListener {
        void onFinishEditPetDialog(Pet _pet);
    }
}
