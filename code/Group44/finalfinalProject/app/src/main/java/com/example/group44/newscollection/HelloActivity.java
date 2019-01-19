package com.example.group44.newscollection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import com.example.group44.newscollection.utils.HandlerManager;
import com.example.group44.newscollection.utils.MainActivityNetworkVisit;

import jackmego.com.jieba_android.JiebaSegmenter;

public class HelloActivity extends AppCompatActivity {

    private ConstraintLayout view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        view = (ConstraintLayout)findViewById(R.id.helloView);
        // 结巴init
        Log.i("activity", "inHello");
        JiebaSegmenter.init(getApplicationContext());
        
        // 渐变动画
        AlphaAnimation animAlpha = new AlphaAnimation(0,1);
        animAlpha.setFillAfter(true);
        animAlpha.setDuration(1500);
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

                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(HelloActivity.this);
                if(shared.getBoolean("isFirst", true)){
                    //第一次进入跳转
                    Intent intent = new Intent(HelloActivity.this, FullscreenActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(HelloActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    // 不允许返回
    @Override
    public void onBackPressed() {
    }
}
