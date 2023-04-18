package com.cubes.miletic.events.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.cubes.miletic.events.R;
import com.cubes.miletic.events.ui.login_registration.IntroActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

public class SplashscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        loadData();

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private void loadData(){

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(SplashscreenActivity.this, MainActivity.class));
        }
        else{
            startActivity(new Intent(SplashscreenActivity.this, IntroActivity.class));
        }

        finish();

        //don't need this
//        ImageView imageView = findViewById(R.id.imageView);
//        imageView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashscreenActivity.this, IntroActivity.class));
//                finish();
//            }
//        }, 2000);
    }
}