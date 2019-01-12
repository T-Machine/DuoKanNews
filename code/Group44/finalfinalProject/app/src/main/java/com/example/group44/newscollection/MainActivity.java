package com.example.group44.newscollection;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.group44.newscollection.JSON.Feed;
import com.example.group44.newscollection.JSON.JsonRootBean;
import com.example.group44.newscollection.adapter.MyRecyclerViewAdapter;
import com.example.group44.newscollection.adapter.MyViewHolder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    JsonRootBean bean;

    RecyclerView recyclerView;
    MyRecyclerViewAdapter myAdapter;
    private BitmapUtils mBitmapUtils;
    Boolean isCardShow = false;
    TextView blackShodow;
    ViewPager view_pager;
    PagerAdapter view_pager_adapter;
    private ArrayList<Feed> mNewsList;
    private List<View> pages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isCardShow = false;
        blackShodow = findViewById(R.id.blackShodow);
        view_pager = findViewById(R.id.view_pager);

        mBitmapUtils = new BitmapUtils(this);
        mNewsList = new ArrayList<>();
        myAdapter = new MyRecyclerViewAdapter<Feed>(this, R.layout.item_recommend, mNewsList) {
            @Override
            public void convert(MyViewHolder holder, Feed s) {
                ImageView img = holder.getView(R.id.iv_icon);
                TextView title = holder.getView(R.id.tv_item_title);
                TextView time = holder.getView(R.id.tv_item_date);
                title.setText(s.getTitle());
                time.setText(s.getPubDate());
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

                //view pager定位到当前item
                view_pager.setCurrentItem(position);
            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);


        // 获取json
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();//创建Gson对象
                bean = gson.fromJson(response.body().string(), JsonRootBean.class);//解析

                //----------------------
                ///回调后的操作
                //----------------------
                for(int i = 0; i < 5; i ++) {
                    myAdapter.addItem(bean.getData().getFeed().get(i));
                }
                myAdapter.notifyDataSetChanged();


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
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //----------------------------------
        //推荐内容部分
        //----------------------------------








    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
