package com.group44.newsCollection;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.group44.newsCollection.utils.PrefUtils;

public class SplashActivity extends AppCompatActivity {

    private ConstraintLayout rlRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        rlRoot = (ConstraintLayout)findViewById(R.id.rl_root);

        // 渐变动画
        AlphaAnimation animAlpha = new AlphaAnimation(0,1);
        animAlpha.setDuration(1000);
        animAlpha.setFillAfter(true);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(animAlpha);
        rlRoot.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                // 判断是否是第一次登录，如果是进入引导界面，否则进入主界面
                Intent intent;
                boolean isFirstEnter = PrefUtils.getBoolean(SplashActivity.this,"is_first_enter",true);
                if (isFirstEnter){
                    //新手引导
                    intent = new Intent(getApplicationContext(), GuideActivity.class);
                }else {
                    // 主页面
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                }
                startActivity(intent);
                finish();//结束当前页面
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
