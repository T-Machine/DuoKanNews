package com.example.group44.newscollection;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.group44.newscollection.JSON.Feed;
import com.example.group44.newscollection.JSON.JsonRootBean;
import com.example.group44.newscollection.adapter.MyRecyclerViewAdapter;
import com.example.group44.newscollection.adapter.MyViewHolder;
import com.example.group44.newscollection.adapter.ViewAdapter;
import com.example.group44.newscollection.transformer.GalleryTransformer;
import com.example.group44.newscollection.utils.HandlerManager;
import com.example.group44.newscollection.utils.MainActivityNetworkVisit;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "OKHTTP CLIENT ";
    static String url = "http://newsapi.sina.cn/?resource=feed&lDid=a9f1b781-e891-4198-af53-1fb74ab3ad1b&oldChwm=&upTimes=0&city=&prefetch=99&channel=news_toutiao&link=&ua=Xiaomi-MI+6__sinanews__6.8.8__android__8.0.0&deviceId=aeaaa73c147faf4e&connectionType=2&resolution=1080x1920&weiboUid=&mac=02%3A00%3A00%3A00%3A00%3A00&replacedFlag=0&osVersion=8.0.0&chwm=14010_0001&pullTimes=1&weiboSuid=&andId=301aa36754a2692e&from=6068895012&sn=8a8a0650&behavior=auto&aId=&localSign=a_22eb3a47-189e-44ac-be6d-81ef8ac635b6&deviceIdV1=aeaaa73c147faf4e&todayReqTime=0&osSdk=26&abver=1527581432688&listCount=0&accessToken=&downTimes=0&abt=313_302_297_281_277_275_269_255_253_251_249_242_237_230_228_226_217_215_207_203_191_189_187_171_153_149_143_141_139_135_128_113_111_57_45_38_21_18_16_13&lastTimestamp=0&pullDirection=down&seId=e70c98e4da&imei=868030036302089&deviceModel=Xiaomi__Xiaomi__MI+6&location=0.0%2C0.0&loadingAdTimestamp=0&urlSign=befedbd988&rand=926";
    // 解析json的结果
//    JsonRootBean bean;
    RecyclerView recyclerView;
    MyRecyclerViewAdapter myAdapter;
    private BitmapUtils mBitmapUtils;
    Boolean isCardShow = false;
    TextView blackShodow;
    ImageView hidden_card;
    ViewPager view_pager;
    PagerAdapter view_pager_adapter;
    private ArrayList<Feed> mNewsList;
    private List<View> pages;
    RefreshLayout mRefreshLayout;             //下拉刷新
    ArrayList<Feed> feedList;

    //设置的状态
    Boolean isBroad = false;
    //public static final String PREFERENCE_NAME = "SaveSetting";


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 获得数据
            if(msg.what == 200){
                feedList = MainActivityNetworkVisit.getInstance().getFeedList();
                processData();
                MainActivityNetworkVisit.getInstance().getMost();

                if(isBroad) {
                    sendBroadcast();
                }
            } else{
                // todo:无网络访问处理.
                final Dialog dialog = new Dialog(MainActivity.this);
                View contentView = LayoutInflater.from(MainActivity.this).inflate(
                        R.layout.dialog_recommend, null);
                dialog.setContentView(contentView);
                dialog.setCanceledOnTouchOutside(true);
                Button OK = contentView.findViewById(R.id.OkButton);
                OK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        finish();
                    }
                });
                dialog.show();
            }
        }
    };

    //字体大小
    float mTextSize = (float) 1.0;
    Boolean oldManModel = false;
    // 加载框
    LoadingDialog ld;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ----网络访问-------
        // 网络访问相关
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String type = shared.getString("collection","1,2,3,4,5,6,7,8");
        MainActivityNetworkVisit.getInstance().setContext(MainActivity.this);
        MainActivityNetworkVisit.getInstance().setUrl(type);
        Log.i("send url to visit util", type);
        //-------------------
        HandlerManager.getInstance().setHandler(handler);
        // 推送通知相关
        final SharedPreferences sharedPreferences = getSharedPreferences ( "Setting", Context.MODE_PRIVATE );

        // 加载框---------------
        ld = new LoadingDialog(MainActivity.this);
        ld.show();
        findViewById(R.id.drawer_layout).setVisibility(View.INVISIBLE);
        //------------------

        // 判断网络状态-------
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if(info == null || !info.isConnected()) {
            final Dialog dialog = new Dialog(this);
            View contentView = LayoutInflater.from(this).inflate(
                    R.layout.dialog_recommend, null);
            dialog.setContentView(contentView);
            dialog.setCanceledOnTouchOutside(true);
            Button OK = contentView.findViewById(R.id.OkButton);
            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        //------------------

        // 获得用户名
        //侧滑 功能
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //获取NavigationView上的组件
        View v = navigationView.getHeaderView(0);
        TextView tvu = v.findViewById(R.id.gotUsername);
        final ImageView editView = v.findViewById(R.id.editBtn);
        editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View tv = v;

                AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
                disappearAnimation.setDuration(400);
                disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        UserDialog ud = new UserDialog(MainActivity.this, new editView() {
                            @Override
                            public void edit(String str) {
                                final NavigationView navigationView = findViewById(R.id.nav_view);
                                //获取NavigationView上的组件
                                View v = navigationView.getHeaderView(0);
                                TextView tvu = v.findViewById(R.id.gotUsername);
                                tvu.setText(str);
                                // 缓存用户名
                                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                SharedPreferences.Editor editor=shared.edit();
                                //第一次进入跳转
                                editor.putString("user", tvu.getText().toString());
                                Log.i("set username", tvu.getText().toString());
                                editor.commit();
                            }
                        });
                        ud.show();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                v.startAnimation(disappearAnimation);
            }
        });
        ImageView iv = v.findViewById(R.id.hostImg);
        final Menu view = navigationView.getMenu();

        view.getItem(0).setCheckable(false);
        view.getItem(1).setCheckable(false);
        view.getItem(2).setCheckable(false);
        view.getItem(3).setCheckable(false);

        //set the menu listener
        view.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        view.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //view.getItem(0).setChecked(true);
                //view.getItem(1).setChecked(false);
                Intent intent1 = new Intent(MainActivity.this, CollectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putFloat("size", mTextSize);
                intent1.putExtras(bundle);
                startActivity(intent1);
                //view.getItem(0).setChecked(true);
                //view.getItem(1).setChecked(false);
                return false;
            }
        });

        view.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(view.getItem(2).getTitle().toString().equals("开启推送模式")) {
                    view.getItem(2).setTitle("关闭推送模式");
                    Toast.makeText(MainActivity.this, "我想静静!", Toast.LENGTH_SHORT).show();
                    /*
                    * 推送部分
                    * */
                    editor.putBoolean("isBroad", true);
                    editor.commit();
                    sendBroadcast();
                }
                else {
                    view.getItem(2).setTitle("开启推送模式");
                    Toast.makeText(MainActivity.this, "推送走起!", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("isBroad", false);
                    editor.commit();
                }
                return false;
            }
        });

        view.getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(oldManModel == false) {
                    oldManModel = true;
                    view.getItem(3).setTitle("关闭老人模式");
                    Toast.makeText(MainActivity.this, "大字体，看得爽!", Toast.LENGTH_SHORT).show();
                    isBroad = true;
                }
                else {
                    oldManModel = false;
                    view.getItem(3).setTitle("开启老人模式");
                    Toast.makeText(MainActivity.this, "小字体，更精致!", Toast.LENGTH_SHORT).show();
                    isBroad = false;
                }
                for (int i = 0; i < pages.size(); i++) {
                    View one = pages.get(i);
                    TextView title = one.findViewById(R.id.previewTitle);
                    TextView content = one.findViewById(R.id.previewContent);
                    //修改字体大小
                    if(oldManModel == false) {
                        title.setTextSize(20);
                        content.setTextSize(16);
                    }
                    else {
                        title.setTextSize(25);
                        content.setTextSize(20);
                    }
                }
                myAdapter.notifyDataSetChanged();
                return false;

                //Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                //调整字体大小
//                if (mTextSize > 1) {
//                    mTextSize = (float) 1/(float) 1.5;
//                    view.getItem(3).setTitle("开启老人模式");
//                }
//                else {
//                    mTextSize = (float) 1.5;
//                    view.getItem(3).setTitle("关闭老人模式");
//                }
//                myAdapter.notifyDataSetChanged();
//                for (int i = 0; i < pages.size(); i++) {
//                    View one = pages.get(i);
//                    TextView title = one.findViewById(R.id.previewTitle);
//                    TextView content = one.findViewById(R.id.previewContent);
//                    //修改字体大小
//                    title.setTextSize(title.getTextSize()*mTextSize/3);
//                    content.setTextSize(content.getTextSize()*mTextSize/3);
//                }
//                return false;
            }
        });


        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, 0);
            }
        });
        String un = shared.getString("user","Admin");
        Log.i("username", un);
        CharSequence cs = un;
        tvu.setText(cs);
        isCardShow = false;
        blackShodow = findViewById(R.id.blackShodow);
        view_pager = findViewById(R.id.view_pager);
        hidden_card = findViewById(R.id.hidden_card);
        mRefreshLayout = findViewById(R.id.refreshLayout);

        mBitmapUtils = new BitmapUtils(this);
        mNewsList = new ArrayList<>();
        myAdapter = new MyRecyclerViewAdapter<Feed>(this, R.layout.item_recommend, mNewsList) {
            @Override
            public void convert(MyViewHolder holder, Feed s) {
                ImageView img = holder.getView(R.id.iv_icon);
                TextView title = holder.getView(R.id.tv_item_title);
                //Toast.makeText(MainActivity.this, String.valueOf(title.getTextSize()), Toast.LENGTH_SHORT).show();
                //title.setTextSize(title.getTextSize()*mTextSize/3);
                //修改字体大小
                if(oldManModel == false) {
                    title.setTextSize(18);
                }
                else {
                    title.setTextSize(22);
                }

                TextView time = holder.getView(R.id.tv_item_date);
                if(s.getTitle() != null){
                    title.setText(s.getTitle());
                }
                if(s.getPubDate() != -1){
                    Integer i = s.getPubDate();
                    Date pubDate = new Date(i);
                    String dateString = pubDate.toString();
                    Integer endIndex = dateString.indexOf("GMT");
                    dateString = dateString.substring(0, endIndex);
                    time.setText(dateString);
                }
                mBitmapUtils.display(img, s.getKpic());
            }
        };
        myAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                // 处理单击事件
                if(isCardShow == true) {
                    return;
                }
                isCardShow = true;

                //previewCard.setVisibility(View.VISIBLE);
                blackShodow.setVisibility(View.VISIBLE);
                view_pager.setVisibility(View.VISIBLE);
                hidden_card.setVisibility(View.VISIBLE);

                //view pager定位到当前item
                view_pager.setCurrentItem(position);
            }

            @Override
            public void onLongClick(int position) {
                // 处理长按事件

            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

        //下拉刷新
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                myAdapter.clearAll();
                myAdapter.notifyDataSetChanged();
                MainActivityNetworkVisit.getInstance().getNewsAgain();
            }
        });

        //隐藏按钮
        hidden_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCardShow = false;
                blackShodow.setVisibility(View.GONE);
                view_pager.setVisibility(View.GONE);
                hidden_card.setVisibility(View.GONE);
            }
        });


        // 悬浮按钮
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, NewsDetail.class);
                startActivity(intent);
                finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //----------------------------------
        //推荐内容部分
        //----------------------------------

        isBroad = sharedPreferences.getBoolean("isBroad", false);
        if(isBroad) {
            view.getItem(2).setTitle("关闭推送模式");
            sendBroadcast();
        }

    }

    @Override
    public void onBackPressed() {
        if(isCardShow == true){
            isCardShow = false;
            blackShodow.setVisibility(View.GONE);
            view_pager.setVisibility(View.GONE);
            hidden_card.setVisibility(View.GONE);
            return;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            // 得到图片的全路径
            Uri uri = data.getData();
            //侧滑 功能
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            //获取NavigationView上的组件
            View v = navigationView.getHeaderView(0);
            // 图片解析的配置
            BitmapFactory.Options options = new BitmapFactory.Options();

            // 不去真正解析图片，只是获取图片的宽高
            options.inJustDecodeBounds = true;
            ImageView hostImg = v.findViewById(R.id.hostImg);
            hostImg.setImageURI(uri);

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    // get data from the list
    void setViewPager() {
        pages = getPages();
        view_pager_adapter = new ViewAdapter(pages);
        view_pager.setAdapter(view_pager_adapter);
        view_pager.setPageMargin(10);
        view_pager.setPageTransformer(true, new GalleryTransformer());
    }

    private List<View> getPages() {
        LayoutInflater inflater = LayoutInflater.from(this);
        List<View> pages = new ArrayList<>();

        for(int i = 0; i < myAdapter.size(); i ++) {
            final Feed item = (Feed) myAdapter.getItem(i);
            View card_view = inflater.inflate(R.layout.item_big_card, null);

            TextView read = card_view.findViewById(R.id.readMore);
            TextView title = card_view.findViewById(R.id.previewTitle);
            TextView content = card_view.findViewById(R.id.previewContent);
//            title.setTextSize(title.getTextSize()*mTextSize/3);
//            content.setTextSize(content.getTextSize()*mTextSize/3);

            //修改字体大小
            if(oldManModel == false) {
                title.setTextSize(20);
                content.setTextSize(16);
            }
            else {
                title.setTextSize(25);
                content.setTextSize(20);
            }

            ImageView img = card_view.findViewById(R.id.iv_icon);
            content.setText(item.getSummary());
            title.setText(item.getTitle());
            mBitmapUtils.display(img, item.getKpic());
            read.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", item.getLink());
                    bundle.putString("imgUrl",item.getKpic());
                    bundle.putString("source",item.getSource());
                    bundle.putString("title", item.getTitle());
//                    bundle.putString("pubDate");
                    bundle.putString("digest", item.getSummary());
                    bundle.putFloat("size", mTextSize);
                    bundle.putBoolean("oldManModel", oldManModel);
                    Intent intent = new Intent(MainActivity.this, NewsDetail.class);
                    intent.putExtra("message",bundle);
                    startActivity(intent);
//                    finish();
                }
            });

            pages.add(card_view);
        }

        return pages;
    }
    // 对获得的新闻进行显示
    private void processData(){
        Observable.create(new ObservableOnSubscribe<Feed>() {
            @Override
            public void subscribe(ObservableEmitter<Feed> emitter) throws IOException {

                Integer sum = feedList.size();
                // 对于获得的列表进行处理
                Log.i("got news number", sum.toString());
                for(Feed e : feedList){
                    Log.i("got news", e.getTitle());
                    Document doc = Jsoup.connect(e.getLink()).get();
                    Log.i("html", doc.title());
                    Element body = doc.body();
                    Elements p = body.select("p");
                    e.setSummary("");
                    String currentSummary = "";
                    for(Element element : p){
                        currentSummary += element.text();
                    }
                    if(currentSummary.length() > 60){
                        currentSummary = currentSummary.substring(0, 60);
                        currentSummary += "...";
                    }
                    e.setSummary(currentSummary);
                    Log.i("Jsoup", e.getSummary());
                    if(e.getSummary().length() == 0){
                        e.setSummary("找不到对应文本哦/./");
                    }
                    emitter.onNext(e);
                }
                emitter.onComplete();
                ld.dismiss();
                mRefreshLayout.finishRefresh();
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
                        myAdapter.addItem(value);
                        Log.d("尝试一下",value.toString());
                        myAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Error exist");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Complete Sending Paragraph");
                        setViewPager();
                        findViewById(R.id.drawer_layout).setVisibility(View.VISIBLE);
                    }
                });
    }

    public interface editView {
        void edit(String str);
    }

    private void sendBroadcast() {
        if(feedList != null) {
            Bundle bundle1 = new Bundle();
            bundle1.putString("url", feedList.get(0).getLink());
            bundle1.putString("imgUrl",feedList.get(0).getKpic());
            bundle1.putString("source",feedList.get(0).getSource());
            bundle1.putString("title", feedList.get(0).getTitle());
//                    bundle.putString("pubDate");
            bundle1.putString("digest", feedList.get(0).getSummary());
            bundle1.putFloat("size", mTextSize);
            Intent intentBroadcast = new Intent("recommend");
            intentBroadcast.putExtras(bundle1);
            sendBroadcast(intentBroadcast);
        }
    }
}
