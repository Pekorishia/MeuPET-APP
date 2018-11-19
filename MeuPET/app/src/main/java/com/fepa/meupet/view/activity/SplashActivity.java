package com.fepa.meupet.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.fepa.meupet.R;
import com.fepa.meupet.model.environment.constants.MeuPETConfig;

public class SplashActivity extends AppCompatActivity {

    private ImageView ivSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivSplash = this.findViewById(R.id.ivSplash);

        // creates a fade in animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // puts the animation inside all splash views
        ivSplash.startAnimation(animation);

        // after a specified time start this handler thread
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO: calls home if the user is already created

                // goes to loginActivity
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);

                // makes the activity transition smooth
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                finish();
            }
        }, MeuPETConfig.SPLASH_TIME);
    }
}
