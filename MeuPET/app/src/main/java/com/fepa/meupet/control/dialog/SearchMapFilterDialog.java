package com.fepa.meupet.control.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.fepa.meupet.R;
import com.fepa.meupet.model.environment.constants.GeneralConfig;


public class SearchMapFilterDialog extends DialogFragment implements DialogInterface.OnMultiChoiceClickListener{

    private boolean[] checkList;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final String[] options = getActivity().getResources().getStringArray(R.array.smap_dialog_options);

        this.setupCheckList(options);

        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.smap_dialog_title))
                .setMultiChoiceItems(R.array.smap_dialog_options, this.checkList,this)
                .setPositiveButton(R.string.smap_dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.putExtra(GeneralConfig.Maps.SEARCH_MAP_BUNDLE, checkList);

                        getTargetFragment().onActivityResult(getTargetRequestCode(), GeneralConfig.RESULT_OK, intent);
                    }
                })
                .setNegativeButton(R.string.smap_dialog_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
        this.checkList[i] = isChecked;
    }

    private void setupCheckList(String[] options){

        // setups to all values start as false
        this.checkList = new boolean[options.length];
    }

}