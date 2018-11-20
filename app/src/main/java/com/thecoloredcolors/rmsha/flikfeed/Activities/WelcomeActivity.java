package com.thecoloredcolors.rmsha.flikfeed.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.thecoloredcolors.rmsha.flikfeed.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void OpenSignupView(View view) {
        Intent intent=new Intent(WelcomeActivity.this,SignupActivity.class);
        startActivity(intent);
    }

    public void OpenLoginView(View view) {
        Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
        startActivity(intent);
    }

}
