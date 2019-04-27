package com.fepa.meupet.control.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.fepa.meupet.R;
import com.fepa.meupet.model.environment.constants.GeneralConfig;
import com.fepa.meupet.model.environment.notification.Notification;

public class AddNotificationDialog extends DialogFragment {

    private String name;
    private EditText etNotDate;
    private EditText etNotTime;
    private EditText etNotContent;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_notification,null);


        this.name = getArguments().getString("petName");

        this.etNotDate = view.findViewById(R.id.etNotificationDate);
        this.etNotTime = view.findViewById(R.id.etNotificationTime);
        this.etNotContent = view.findViewById(R.id.etNotificationName);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.notification_dialog_title);
        builder.setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String content = etNotContent.getText().toString();
                String date = etNotDate.getText().toString();
                String time = etNotTime.getText().toString();

                Notification notification = new Notification(name, date, time, content);

                // sends the notification through broadcast
                Intent intent = new Intent(GeneralConfig.Notifications.BROADCAST_NOTIFICATION);
                intent.putExtra(GeneralConfig.Notifications.NOTIFICATION_BUNDLE, notification);
                getActivity().sendBroadcast(intent);
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
}
