package com.example.group44.newscollection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

public class HelloActivity extends AppCompatActivity {

    private ConstraintLayout view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        view = (ConstraintLayout)findViewById(R.id.helloView);

        // 渐变动画
        AlphaAnimation animAlpha = new AlphaAnimation(0,1);
        animAlpha.setFillAfter(true);
        animAlpha.setDuration(1000);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(animAlpha);
        view.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {

                SharedPreferences shared=getSharedPreferences("judgeIsFirst", MODE_PRIVATE);
                SharedPreferences.Editor editor=shared.edit();
                if(shared.getBoolean("isFirst", true)){
                    //第一次进入跳转
                    Intent intent = new Intent(HelloActivity.this, FullscreenActivity.class);
                    startActivity(intent);
                    finish();
                    editor.putBoolean("isFirst", false);
                    editor.commit();
                }else{
                    Intent intent = new Intent(HelloActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
