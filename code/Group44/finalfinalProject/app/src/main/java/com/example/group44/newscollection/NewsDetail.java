package com.example.group44.newscollection;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.wx.goodview.GoodView;

public class NewsDetail extends AppCompatActivity {
    // 滑动
    ImageView imgview;
    ConstraintLayout buttonset;
    String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        final GoodView goodView = new GoodView(this);
        ImageView imgButton = findViewById(R.id.dislikeButton);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodView.setText("不喜欢");
                goodView.show(v);
                //动作
                AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
                disappearAnimation.setDuration(300);
                AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
                appearAnimation.setDuration(300);
                AnimationSet as = new AnimationSet(true);
                as.addAnimation(disappearAnimation);
                as.addAnimation(appearAnimation);
                v.startAnimation(as);
            }
        });

        ImageView shareButton = findViewById(R.id.share);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // 复制到剪贴板
            public void onClick(View v) {
                ClipboardManager myClipboard;
                myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip;
                myClip = ClipData.newPlainText("text", url);
                myClipboard.setPrimaryClip(myClip);
                goodView.setText("链接已复制");
                goodView.show(v);
                AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
                disappearAnimation.setDuration(300);
                AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
                appearAnimation.setDuration(300);
                AnimationSet as = new AnimationSet(true);
                as.addAnimation(disappearAnimation);
                as.addAnimation(appearAnimation);
                v.startAnimation(as);
                // 跳转微信
                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cmp);
                    startActivity(intent);

                } catch (ActivityNotFoundException e) {
                    // TODO: handle exception
                    Toast toast = Toast.makeText(NewsDetail.this, "没有安装微信，不进行跳转",Toast.LENGTH_LONG);
                    toast.show();

                }
            }
        });
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        final ScrollView scrl = findViewById(R.id.scroll);
        TextView tv = findViewById(R.id.paragraph);
        buttonset = findViewById(R.id.buttonSet);
        tv.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "                \"\" +\n                \"\" +\n                \"\" +\n" +
                "a");
        imgview = findViewById(R.id.newsImage);
        scrl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int scrollY=view.getScrollY();
                int height=view.getHeight();
                int scrollViewMeasuredHeight=scrl.getChildAt(0).getMeasuredHeight();
                if(scrollY - scrollViewMeasuredHeight < 100){
                    float alpha = scrollY / (float)100.0;
                    if(alpha > (float)1.0) alpha = (float)0.0;
                    else alpha = (float)1.0 - alpha;
                    if(alpha < 0.1) alpha = 0;
                    if(alpha < 0.5) buttonset.setVisibility(View.GONE);
                    else buttonset.setVisibility(View.VISIBLE);
                    imgview.setAlpha(alpha);
                }
                return false;
            }
        });
    }
}
