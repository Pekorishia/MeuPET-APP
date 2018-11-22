package com.fepa.meupet.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.fepa.meupet.R;
import com.fepa.meupet.model.environment.constants.GeneralConfig;

public class SplashActivity extends AppCompatActivity {

    private ImageView ivSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivSplash = this.findViewById(R.id.ivSplash);

        // after a specified time start this handler thread
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // goes to the welcome slider
                Intent loginIntent = new Intent(getApplicationContext(), SliderIntroActivity.class);
                startActivity(loginIntent);

                // makes the activity transition smooth
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                finish();
            }
        }, GeneralConfig.SPLASH_TIME);
    }
}
