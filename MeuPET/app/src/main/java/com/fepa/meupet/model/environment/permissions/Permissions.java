package com.fepa.meupet.model.environment.permissions;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

    public static boolean validatePermissions(String[] permissions, Activity activity, int requestCode){

        if (Build.VERSION.SDK_INT >= 23 ){

            List<String> perm_list = new ArrayList<>();

            for ( String permission : permissions ){
                Boolean havePermission = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
                if ( !havePermission )
                    perm_list.add(permission);
            }


            if ( perm_list.isEmpty() )
                return true;
            String[] newPermissions = new String[ perm_list.size() ];
            perm_list.toArray( newPermissions );


            ActivityCompat.requestPermissions(activity, newPermissions, requestCode );
        }
        return true;
    }
}
