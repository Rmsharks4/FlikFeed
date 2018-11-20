package com.thecoloredcolors.rmsha.flikfeed.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.*;
import android.widget.*;

import com.thecoloredcolors.rmsha.flikfeed.R;

public class SplashActivity extends AppCompatActivity {

    public static String LOGGED_IN = "UserID";
    public static String LOGIN = "Login";
    private boolean launch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imageView = (ImageView) findViewById(R.id.splash_img);
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        imageView.startAnimation(startAnimation);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                    launchNextScreen();
            }
        }, 3000);
        if(getSharedPreferences(LOGIN,Context.MODE_PRIVATE).getLong(LOGGED_IN,0)!=0) {
            launch = true;
        }
    }

    public void launchNextScreen() {
        if(launch) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        launchNextScreen();
        return super.onTouchEvent(event);
    }

}
