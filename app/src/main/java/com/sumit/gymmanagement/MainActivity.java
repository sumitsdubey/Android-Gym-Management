package com.sumit.gymmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    AppCompatImageView iv_logo;
    LinearLayout ll_name;
    Animation logo_anim, gym_name_anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_logo = findViewById(R.id.iv_logo);
        ll_name = findViewById(R.id.ll_name);

    logo_anim = AnimationUtils.loadAnimation(this, R.anim.splash_anim);

        gym_name_anim = AnimationUtils.loadAnimation(this, R.anim.gym_name_anim);

    iv_logo.startAnimation(logo_anim);

    ll_name.startAnimation(gym_name_anim);

    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(getApplicationContext(), LoginEmailActivity.class);
            startActivity(i);
            finish();
        }
    },3000);

    }


}