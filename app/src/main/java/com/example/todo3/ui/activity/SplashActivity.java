package com.example.todo3.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.todo3.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIMEOUT = 4000;   //Splash screen will be shown for 4 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
        ImageView imageView = findViewById(R.id.splashScreenImageView);
        imageView.setBackgroundResource(R.drawable.todo_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(splashIntent);
                finish();
            }
        },SPLASH_TIMEOUT);
    }
}
