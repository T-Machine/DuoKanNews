package com.example.group44.newscollection;

import android.content.Intent;
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
//                // 判断是否是第一次登录，如果是进入引导界面，否则进入主界面
                Intent intent;
//                boolean isFirstEnter = PrefUtils.getBoolean(SplashActivity.this,"is_first_enter",true);
//                if (isFirstEnter){
//                    //新手引导
//                    intent = new Intent(getApplicationContext(), GuideActivity.class);
//                }else {
//                    // 主页面
                   intent = new Intent(getApplicationContext(), FullscreenActivity.class);
//                }
                startActivity(intent);
                finish();//结束当前页面
            }
        });
    }
}
