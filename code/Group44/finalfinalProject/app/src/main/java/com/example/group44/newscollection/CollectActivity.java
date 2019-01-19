package com.example.group44.newscollection;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.group44.newscollection.JSON.Feed;
import com.example.group44.newscollection.adapter.MyRecyclerViewAdapter;
import com.example.group44.newscollection.adapter.MyViewHolder;
import com.example.group44.newscollection.persistence.AppRepository;
import com.example.group44.newscollection.persistence.FavoriteNews;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CollectActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyRecyclerViewAdapter myAdapter;
    private BitmapUtils mBitmapUtils;
    Boolean isCardShow = false;
    TextView blackShodow;
    ImageView hidden_card;
    ViewPager view_pager;

    private static String TAG = "COLLECT_ACTIVITY";
    private AppRepository mDatasource;


    private Boolean oldManModel;
    private float mTextsize;
    private ArrayList<FavoriteNews> mNewsList;
    private List<View> pages;
    // 加载框
    //LoadingDialog ld;
    float x1,x2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        // 加载框---------------
        //ld = new LoadingDialog(CollectActivity.this);
        //ld.show();
        //findViewById(R.id.drawer_layout).setVisibility(View.INVISIBLE);
        //------------------
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mTextsize = bundle.getFloat("size");
        oldManModel = bundle.getBoolean("oldManModel");

        // setup database
        mDatasource = new AppRepository(this);
        mBitmapUtils = new BitmapUtils(this);

        mNewsList = new ArrayList<>();
        List<FavoriteNews> temp = mDatasource.queryAllFavNews();

        for (int i = 0; i < temp.size(); i++) {
            mNewsList.add(temp.get(i));
        }

        ConstraintLayout vw = findViewById(R.id.collectLayout);
        vw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    //当手指按下的时候
                    x1 = event.getX();
                }
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    x2 = event.getX();
                    if(x2 - x1 > 400) {
                        Toast.makeText(CollectActivity.this, "回家", Toast.LENGTH_SHORT).show();
                        CollectActivity.super.onBackPressed();
                    }
                }
                return true;
            }
        });

        myAdapter = new MyRecyclerViewAdapter<FavoriteNews>(this, R.layout.item_recommend, mNewsList) {
            @Override
            public void convert(MyViewHolder holder, FavoriteNews s) {
                ImageView img = holder.getView(R.id.iv_icon);
                TextView title = holder.getView(R.id.tv_item_title);

                //title.setTextSize(title.getTextSize()*mTextsize/3);
                if(oldManModel == false) {
                    title.setTextSize(18);
                }
                else {
                    title.setTextSize(22);
                }

                TextView time = holder.getView(R.id.tv_item_date);
                if(s.title != null){
                    title.setText(s.title);
                }
                /*if(s.getPubDate() != -1){
                    Integer i = s.getPubDate();
                    Date pubDate = new Date(i);
                    time.setText(pubDate.toString());
                }*/
                mBitmapUtils.display(img, s.imgUrl);
            }
        };
        myAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                // 处理单击事件
                final FavoriteNews item = (FavoriteNews) myAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("url", item.srcUrl);
                bundle.putString("imgUrl",item.imgUrl);
                //bundle.putString("source",item.digest);
                bundle.putString("title", item.title);
//                    bundle.putString("pubDate");
                bundle.putString("digest", item.digest);
                bundle.putFloat("size", 1);
                bundle.putBoolean("oldManModel", oldManModel);
                Intent intent = new Intent(CollectActivity.this, NewsDetail.class);
                intent.putExtra("message",bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {
                // 处理长按事件
                final int index = position;
                final FavoriteNews item = (FavoriteNews) myAdapter.getItem(position);
                final Dialog dialog1 = new Dialog(CollectActivity.this);
                View contentView = LayoutInflater.from(CollectActivity.this).inflate(
                        R.layout.dialog_delete, null);
                TextView title = contentView.findViewById(R.id.title);
                title.setText(item.title);
                Button Cancel = contentView.findViewById(R.id.CancelButton);
                Button OK = contentView.findViewById(R.id.OkButton);
                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();
                    }
                });
                OK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO: 删除收藏项

                        String title = mNewsList.get(index).title;
                        // 删除数据库中的对象
                        mDatasource.deleteFavNews(title);
                        // 删除内存中的对象
                        mNewsList.remove(index);
                        myAdapter.notifyDataSetChanged();
                        Log.d(TAG, "onLongClick: delete favorite news " + title);

                        dialog1.dismiss();
                    }
                });
                dialog1.setContentView(contentView);
                dialog1.setCanceledOnTouchOutside(true);
                dialog1.show();
            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.collectRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }
}
