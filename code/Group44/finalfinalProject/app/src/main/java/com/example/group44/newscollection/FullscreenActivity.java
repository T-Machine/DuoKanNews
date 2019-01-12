package com.example.group44.newscollection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FullscreenActivity extends AppCompatActivity {

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 0;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */

    private FrameLayout view;
    // 当前图片
    Integer currentImg = 0;
    EditText editText;
    float x1, x2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        view = findViewById(R.id.guideView);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    //当手指按下的时候
                    x1 = event.getX();
                }
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    x2 = event.getX();
                    if(x1 - x2 > 20) {
                        if(currentImg == 0){
                            ImageView btn1 = findViewById(R.id.bottomBtn1);
                            ImageView btn2 = findViewById(R.id.bottomBtn2);
                            btn1.setImageResource(R.drawable.emptycircle);
                            btn2.setImageResource(R.drawable.fullcircle);
                            currentImg++;
                            final ConstraintLayout unset = findViewById(R.id.usernameSet);
                            AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
                            disappearAnimation.setDuration(400);
                            disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    unset.setVisibility(View.VISIBLE);
                                    // 显示用户名设置选项
                                    AlphaAnimation appear = new AlphaAnimation(0, 1);
                                    appear.setDuration(300);
                                    unset.startAnimation(appear);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            findViewById(R.id.img1).startAnimation(disappearAnimation);
                            findViewById(R.id.img1).setVisibility(View.GONE);
                            // 标签页
                        } else if(currentImg == 4){
                            ImageView btn1 = findViewById(R.id.bottomBtn1);
                            ImageView btn2 = findViewById(R.id.bottomBtn2);
                            btn1.setImageResource(R.drawable.emptycircle);
                            btn2.setImageResource(R.drawable.fullcircle);
                            currentImg = 3;
                            AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
                            disappearAnimation.setDuration(400);
                            disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    findViewById(R.id.choiceSet).setVisibility(View.VISIBLE);
                                    AlphaAnimation appear = new AlphaAnimation(0, 1);
                                    appear.setDuration(300);
                                    findViewById(R.id.choiceSet).startAnimation(appear);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            findViewById(R.id.img1).startAnimation(disappearAnimation);
                            findViewById(R.id.img1).setVisibility(View.GONE);
                        }
                        // 防止误触发
                    } else if(x2 - x1 > 400) {
                        if(currentImg == 1){
                            ImageView btn1 = findViewById(R.id.bottomBtn1);
                            ImageView btn2 = findViewById(R.id.bottomBtn2);
                            btn1.setImageResource(R.drawable.fullcircle);
                            btn2.setImageResource(R.drawable.emptycircle);
                            AlphaAnimation disappearAnimation = new AlphaAnimation(0, 1);
                            disappearAnimation.setDuration(300);
                            findViewById(R.id.img1).startAnimation(disappearAnimation);
                            findViewById(R.id.img1).setVisibility(View.VISIBLE);
                            currentImg--;
                            ConstraintLayout unset = findViewById(R.id.usernameSet);
                            unset.setVisibility(View.GONE);
                            // 标签设置页
                        } else if(currentImg == 3){
                            ImageView btn1 = findViewById(R.id.bottomBtn1);
                            ImageView btn2 = findViewById(R.id.bottomBtn2);
                            btn1.setImageResource(R.drawable.fullcircle);
                            btn2.setImageResource(R.drawable.emptycircle);
                            AlphaAnimation disappearAnimation = new AlphaAnimation(0, 1);
                            disappearAnimation.setDuration(300);
                            findViewById(R.id.img1).startAnimation(disappearAnimation);
                            findViewById(R.id.img1).setVisibility(View.VISIBLE);
                            findViewById(R.id.choiceSet).setVisibility(View.GONE);
                            currentImg = 4;
                        }
                    }
                }
                return true;
            }
        });

        editText = findViewById(R.id.username);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText.getText().length() == 0){
                    findViewById(R.id.usernameOKbutton).setVisibility(View.INVISIBLE);
                }else {
                    findViewById(R.id.usernameOKbutton).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Button userOkbtn = findViewById(R.id.usernameOKbutton);
        userOkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 成功设置用户名
                currentImg = 2;
                // 缓存用户名
                SharedPreferences shared=getSharedPreferences("Username", MODE_PRIVATE);
                SharedPreferences.Editor editor=shared.edit();
                //第一次进入跳转
                editor.putString("user", editText.getText().toString());
                Log.i("set username", editText.getText().toString());
                editor.commit();
                // 交替显示
                TextView tv = findViewById(R.id.welcomeUsername);
                tv.setText(editText.getText());
                final ConstraintLayout unset = findViewById(R.id.usernameSet);
                AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
                disappearAnimation.setDuration(1000);
                disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        unset.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                unset.startAnimation(disappearAnimation);

                final ConstraintLayout welcomeSet = findViewById(R.id.welcomeSet);
                // 显示
                AlphaAnimation appear = new AlphaAnimation(0, 1);
                appear.setDuration(1000);
                appear.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        welcomeSet.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                appear.setStartOffset(1000);
                // 保持
                AlphaAnimation keep = new AlphaAnimation(1, 1);
                keep.setDuration(1000);
                keep.setStartOffset(2000);
                // 消失
                AlphaAnimation disappear = new AlphaAnimation(1, 0);
                disappear.setDuration(1000);
                disappear.setStartOffset(3000);
                AnimationSet as = new AnimationSet(true);
                as.addAnimation(appear);
                as.addAnimation(keep);
                as.addAnimation(disappear);
                as.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        welcomeSet.setVisibility(View.GONE);
                        // 设置标签
                        ConstraintLayout cos = findViewById(R.id.choiceSet);
                        cos.setVisibility(View.VISIBLE);
                        currentImg = 3;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                welcomeSet.startAnimation(as);


            }
        });


        Button choiceOkbtn = findViewById(R.id.choiceOKButton);
        choiceOkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 成功设置用户名
                currentImg = 2;
                // 交替显示
                final ConstraintLayout choiceSet = findViewById(R.id.choiceSet);
                AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
                disappearAnimation.setDuration(1000);
                disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        choiceSet.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                choiceSet.startAnimation(disappearAnimation);

                final ConstraintLayout successSet = findViewById(R.id.successSet);
                // 显示
                AlphaAnimation appear = new AlphaAnimation(0, 1);
                appear.setDuration(1000);
                appear.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        successSet.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                appear.setStartOffset(1000);
                // 保持
                AlphaAnimation keep = new AlphaAnimation(1, 1);
                keep.setDuration(1000);
                keep.setStartOffset(2000);
                // 消失
                AlphaAnimation disappear = new AlphaAnimation(1, 0);
                disappear.setDuration(1000);
                disappear.setStartOffset(3000);
                AnimationSet as = new AnimationSet(true);
                as.addAnimation(appear);
                as.addAnimation(keep);
                as.addAnimation(disappear);
                as.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        successSet.setVisibility(View.GONE);
                        // 跳转
                        Intent intent =new Intent(FullscreenActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                successSet.startAnimation(as);
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(0);
    }


    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

}
