package com.example.group44.newscollection;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.example.group44.newscollection.JSON.Feed;
import com.example.group44.newscollection.persistence.AppDatabase;
import com.example.group44.newscollection.persistence.DetectWords;
import com.example.group44.newscollection.persistence.FavoriteNews;
import com.example.group44.newscollection.persistence.AppRepository;
import com.example.group44.newscollection.persistence.FavoriteNews;
import com.example.group44.newscollection.persistence.WordFrequency;
import com.example.group44.newscollection.utils.UtilsFunction;
import com.lidroid.xutils.BitmapUtils;
import com.wx.goodview.GoodView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jackmego.com.jieba_android.JiebaSegmenter;

public class NewsDetail extends AppCompatActivity {

    class DetailItem {
        private String mText;
        private String mTitle;
        private List<Bitmap> mImgs;
        private List<String> mImgsURL;

        // todo: video

        DetailItem() {
            mText = "";
            mImgs = new ArrayList<>();
            mImgsURL = new ArrayList<>();
        }

        public String getText() {
            return mText;
        }

        public void setText(String text) {
            mText = text;
        }


        public void insertImg(Bitmap item) {
            mImgs.add(item);
        }

        public Bitmap getImg(int pos) {
            return mImgs.get(pos);
        }

        public int getImgNum() {
            return mImgs.size();
        }

        public void insertImgURL(String item) {
            mImgsURL.add(item);
        }

        public String getImgURL(int pos) {
            return mImgsURL.get(pos);
        }

        public int getImgURLNum() {
            return mImgsURL.size();
        }


        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String title) {
            mTitle = title;
        }
    }

    // 滑动
    ImageView imgview;
    ConstraintLayout buttonset;
    String url = "";
    private BitmapUtils mBitmapUtils;
    private static String TAG = "DETAIL_ITEM";

    // view
    private TextView mContentText;
    private TextView mTitle;
    private VideoView mVideoView;

    // data persistence
    private AppRepository mDatasource;
    private FavoriteNews mFavNewsCandidate;

    // 不喜欢的原因对话框
    CommonDialog commonDialog;
    boolean isWordNull = false;
    ArrayList<String> favWords = new ArrayList<>();

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final GoodView goodView = new GoodView(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Intent intent = getIntent();
        //从intent取出bundle
        Bundle bundle = intent.getBundleExtra("message");
        commonDialog = new CommonDialog(NewsDetail.this);
        mFavNewsCandidate = UtilsFunction.newsGenerator(
                bundle.getString("title"),
                bundle.getString("digest"),
                bundle.getString("url"),
                bundle.getString("imgUrl")
        );
        mContentText = findViewById(R.id.paragraph);
        mTitle = findViewById(R.id.title);

        // setup database
        mDatasource = new AppRepository(this);

        //获取数据
        url = bundle.getString("url");
        Log.d(TAG, "onCreate: url => " + url);
        TextView src = findViewById(R.id.source);
        src.setText(bundle.getString("source"));
        mBitmapUtils = new BitmapUtils(this);

        // RXJava获得内容
        Observable.create(new ObservableOnSubscribe<DetailItem>() {
            @Override
            public void subscribe(ObservableEmitter<DetailItem> emitter) throws IOException {
                Document doc = Jsoup.connect(url).get();
                DetailItem res = new DetailItem();
                Element body = doc.body();


                // 判断是否存在视频并增加跳转
                Elements videoElements = body.select(".art_video");
                if(videoElements.size() != 0){
                    Log.i("figure.art_video",url);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ImageView imgButton = findViewById(R.id.videoBtn);
                            imgButton.setVisibility(View.VISIBLE);
                            imgButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                // 复制到剪贴板
                                public void onClick(View v) {
                                    final View tv = v;

                                    goodView.setText("看视频");
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
                                                Uri uri = Uri.parse(url);
                                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                startActivity(intent);

                                            } catch (ActivityNotFoundException e) {
                                                // TODO: handle exception
                                                Toast toast = Toast.makeText(NewsDetail.this, "没有进行浏览器跳转",Toast.LENGTH_LONG);
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
                        }
                    });
                }


                // 进行文字解析
                Elements p = body.select("p");

                StringBuilder buf = new StringBuilder();
                for(Element e : p) {
                    String s = e.text();
                    // 去除干扰
                    if(s.indexOf("图为") != -1) continue;
                    buf.append(s);
                }
                res.setText(buf.toString());
                Log.d(TAG, "subscribe: " + res.getText());

                // parsing problem here
                Elements imgsURL = body.select("img");
                Log.d(TAG, "subscribe: num of fetched images" + imgsURL.size());

                for( Element e : imgsURL) {
                    // todo
                    String url = e.attr("abs:src");
                    if(url.endsWith("png") || url.endsWith("jpg")) {
                        res.insertImgURL(url);
                        Log.d(TAG, "subscribe: " + url);
                    }
                }
                if(p != null) {
                    res.setText(p.text());
                }
                res.setTitle(doc.title());
                Log.d(TAG, "subscribe: setup done");
                emitter.onNext(res);
                emitter.onComplete();
            }
            // 需要回调主线程
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DetailItem>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final DetailItem value) {
                        // update UI here
                        Log.d(TAG, "onNext: " + value.getText());

                        if(value.getImgURLNum() > 1) {
                            ImageView imgView = findViewById(R.id.newsImage);
                            Log.d(TAG, "onNext: setup image done");
                            mBitmapUtils.display(imgView, value.getImgURL(value.getImgURLNum() - 2));
                        } else {
                            Log.d(TAG, "onNext: No imgs here");
                        }

                        String origin = value.getText();
                        // 分句
                        ArrayList<String> subSentances = new ArrayList<>();
                        while(origin.indexOf("。") != -1){
                            subSentances.add(origin.substring(0, origin.indexOf("。") + 1));
                            origin = origin.substring(origin.indexOf("。") + 1);
                        }

                        // 分段
                        String complete = "        ";
                        if(subSentances.size() > 14){
                            Integer subParagraph = subSentances.size() / 7;
                            for(int i = 0; i < subSentances.size(); ++i){
                                complete += subSentances.get(i);
                                if(i % subParagraph == 0 && i != 0){
                                    complete += "\n\n       ";
                                }
                            }
                        }else{
                            for(String e : subSentances){
                                complete += e;
                            }
                        }
                        mContentText.setText(complete);
                        String title = value.getTitle();
                        if(title.indexOf("_手机新浪网") != -1){
                            title = title.substring(0,title.indexOf("_手机新浪网"));
                        }
                        if(title.indexOf("_新浪视频") != -1){
                            title = title.substring(0,title.indexOf("_新浪视频"));
                        }
                        mTitle.setText(title);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, " error occur : " + e.getMessage());
                    }

                    // 进行分词处理
                    @Override
                    public void onComplete() {
                        TextView tv = findViewById(R.id.paragraph);
                        if(tv.getText().toString().equals("")) return;
                        final String analizedString = tv.getText().toString();
                        // 多线程分词
                        new Thread(){
                            // 排序
                            class SortByFrequency implements Comparator {
                                public int compare(Object o1, Object o2) {
                                    String_val s1 = (String_val) o1;
                                    String_val s2 = (String_val) o2;
                                    if (s1.getVal() < s2.getVal())
                                        return 1;
                                    else if(s1.getVal() == s2.getVal()) return 0;
                                    return -1;
                                }
                            }

                            @Override
                            public void run(){
                                ArrayList<String_val> local_str_val = new ArrayList<>();
                                ArrayList<String> wordList = JiebaSegmenter.getJiebaSegmenterSingleton().getDividedString(analizedString);
                                for(String str : wordList){
                                    if(str.length() <= 2) continue;
                                    // 筛选
                                    if(!DetectWords.inValid(str)) continue;
                                    boolean flag = false;
                                    for(String_val e : local_str_val){
                                        if(e.getChara().equals(str)){
                                            local_str_val.get(local_str_val.indexOf(e)).add();
                                            flag = true;
                                            break;
                                        }
                                    }
                                    if(!flag){
                                        String_val tmp = new String_val(str, 1);
                                        local_str_val.add(tmp);
                                    }
                                }
                                if(local_str_val.size() == 0) {
                                    isWordNull = true;
                                    return;
                                }
                                // 排序
                                Collections.sort(local_str_val, new SortByFrequency());
                                for(int i = 0; i < local_str_val.size() && i < 4; i++){
                                    String_val e = local_str_val.get(i);
                                    favWords.add(e.getChara());
                                    Log.i("analyze", e.getChara() + " " + e.getVal().toString());
                                    // todo:永久化保存数据。如果存在数据则加上对应的val值，如果没有则进行保存。
                                }
                            }
                        }.start();
                    }
                });

        ImageView imgButton = findViewById(R.id.dislikeButton);
        ImageView collectionBtn = findViewById(R.id.collectionBtn);
        collectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏功能,
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

                AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
                disappearAnimation.setDuration(400);
                disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tv.setAlpha(1);
                        if(isWordNull){
                            Toast.makeText(NewsDetail.this, "抱歉，无法获得该文章的分词!", Toast.LENGTH_SHORT).show();
                        }else{
                            commonDialog.getMessage(favWords);
                            commonDialog.show();
                        }
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
                // save all date into

                if(mDatasource.queryNewsByTitle(mFavNewsCandidate.title).size() == 0) {
                    // 未被收藏的新闻
                    mDatasource.insertNewFavNews(mFavNewsCandidate);
                    Log.d(TAG, "onClick: save favnews into database : " + mFavNewsCandidate.title);
                } else {
                    Log.d(TAG, "onClick: already insert" + mFavNewsCandidate.title);
                    Toast.makeText(NewsDetail.this, "已被收藏，不要重复收藏", Toast.LENGTH_SHORT).show();
                }
                
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
        tv.setText("");

        imgview = findViewById(R.id.newsImage);

        // 设置主图比例
        WindowManager wm = (WindowManager) getBaseContext()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        int screenWidth = outMetrics.widthPixels;
        imgview.setMaxWidth(screenWidth);
        imgview.setMaxHeight(screenWidth);
//        if(imgview.getMaxHeight() > 230){
//            imgview.setMaxHeight(230);
//        }

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
