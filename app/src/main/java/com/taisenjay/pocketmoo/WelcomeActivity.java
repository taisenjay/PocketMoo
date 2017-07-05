package com.taisenjay.pocketmoo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {


    private TextView tvPocket;
    private TextView tvMoo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
    }

    private void initView() {
        tvPocket = (TextView) findViewById(R.id.tv_pocket);
        tvMoo = (TextView) findViewById(R.id.tv_moo);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.welcome_alpha);

        tvPocket.setAnimation(animation);
        tvMoo.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                intoMain();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void intoMain() {
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.in_anim, R.anim.out_anim);
        finish();
    }



}
