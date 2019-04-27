package com.fepa.meupet.control.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fepa.meupet.R;
import com.fepa.meupet.model.agent.pet.Pet;

public class AddFeedingTimeDialog extends DialogFragment {

    private AddFeedingDialogListener listener;

    private EditText etAmount;
    private EditText etTime;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the EditNameDialogListener so we can send events to the host
            listener = (AddFeedingDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement EditPetInfoDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_feeding_time,null);

        this.etAmount = view.findViewById(R.id.etFeedingAmount);
        this.etTime = view.findViewById(R.id.etFeedingTime);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_feeding_title);
        builder.setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!etAmount.getText().toString().equals("") &&
                        !etTime.getText().toString().equals("")){
                    // Return input text to activity
                    listener.onFinishFeedingDialog(Integer.parseInt(etAmount.getText().toString()),
                                                    etTime.getText().toString());
                }
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


    public interface AddFeedingDialogListener {
        void onFinishFeedingDialog(int amount, String time);
    }
}
