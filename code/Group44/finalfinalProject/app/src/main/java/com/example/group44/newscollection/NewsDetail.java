package com.example.group44.newscollection;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.group44.newscollection.JSON.Feed;
import com.lidroid.xutils.BitmapUtils;
import com.wx.goodview.GoodView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsDetail extends AppCompatActivity {
    // 滑动
    ImageView imgview;
    ConstraintLayout buttonset;
    String url = "";
    private BitmapUtils mBitmapUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Intent intent = getIntent();
        //从intent取出bundle
        Bundle bundle = intent.getBundleExtra("message");
        //获取数据
        url = bundle.getString("url");
        String mainImg = bundle.getString("img");
        ImageView imgView = findViewById(R.id.newsImage);
        if(mainImg.length() == 0){
            imgView.setImageResource(R.drawable.logo);
        } else{
            Log.i("imgsrc",mainImg);
            mBitmapUtils.display(imgView, mainImg);
        }


        // RXJava获得内容
        Observable.create(new ObservableOnSubscribe<Feed>() {
            @Override
            public void subscribe(ObservableEmitter<Feed> emitter) throws IOException {
                Document doc = Jsoup.connect(url).get();
                Log.i("html", doc.title());
                Element body = doc.body();
//                Elements p = body.select("p");
//                e.setSummary("");
//                String currentSummary = "";
//                for(Element element : p){
//                    currentSummary += element.text();
//                }
//                // 获取句子
//                for(int index = 0; index < 1; index++){
//                    Integer endSentance = currentSummary.indexOf("。");
//                    if(endSentance == -1) break;
//                    e.addSummary(currentSummary.substring(0, endSentance + 1));
//                    currentSummary = currentSummary.substring(endSentance + 1);
//                }
//                Log.i("Jsoup", e.getSummary());
//                if(e.getSummary().length() == 0){
//                    e.setSummary("找不到对应文本哦/./");
//                }
//                emitter.onNext(e);
//                emitter.onComplete();
            }
            // 需要回调主线程
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Feed>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final Feed value) {
//                        myAdapter.addItem(value);
//                        myAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Log.d(TAG, "Error exist");
                    }

                    @Override
                    public void onComplete() {
//                        Log.d(TAG, "Complete Sending Paragraph");
//                        setViewPager();
                    }
                });

        final GoodView goodView = new GoodView(this);
        ImageView imgButton = findViewById(R.id.dislikeButton);
        ImageView collectionBtn = findViewById(R.id.collectionBtn);
        collectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏功能
                if(findViewById(R.id.buttonSubset).getVisibility() == View.VISIBLE){
                    AlphaAnimation disappear = new AlphaAnimation(1, 0);
                    disappear.setDuration(100);
                    findViewById(R.id.share).startAnimation(disappear);
                    disappear.setStartOffset(100);
                    findViewById(R.id.dislikeButton).startAnimation(disappear);
                    disappear.setStartOffset(200);
                    disappear.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            findViewById(R.id.buttonSubset).setVisibility(View.GONE);
                            // 弹出对话框
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    findViewById(R.id.likeButton).startAnimation(disappear);
                } else{
                    findViewById(R.id.buttonSubset).setVisibility(View.VISIBLE);
                    AlphaAnimation appear = new AlphaAnimation(0, 1);
                    appear.setDuration(100);
                    findViewById(R.id.likeButton).startAnimation(appear);
                    appear.setStartOffset(100);
                    findViewById(R.id.dislikeButton).startAnimation(appear);
                    appear.setStartOffset(200);
                    findViewById(R.id.share).startAnimation(appear);

                }
            }
        });
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View tv = v;
                goodView.setText("不喜欢");
                goodView.show(v);
                //动作
                //动作
                AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
                disappearAnimation.setDuration(400);
                disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tv.setAlpha(1);
                        CommonDialog commonDialog = new CommonDialog(NewsDetail.this);
                        commonDialog.show();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                v.startAnimation(disappearAnimation);
            }
        });

        ImageView shareButton = findViewById(R.id.share);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // 复制到剪贴板
            public void onClick(View v) {
                final View tv = v;
                ClipboardManager myClipboard;
                myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip;
                myClip = ClipData.newPlainText("text", url);
                myClipboard.setPrimaryClip(myClip);
                goodView.setText("链接已复制");
                goodView.show(v);
                //动作
                AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
                disappearAnimation.setDuration(400);
                disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tv.setAlpha(1);
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

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                v.startAnimation(disappearAnimation);
            }
        });

        ImageView collectBtn = findViewById(R.id.likeButton);
        collectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View tv = v;
                goodView.setText("已收藏");
                goodView.show(v);
                //动作
                AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
                disappearAnimation.setDuration(400);
                disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                       tv.setAlpha(1);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                v.startAnimation(disappearAnimation);
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
