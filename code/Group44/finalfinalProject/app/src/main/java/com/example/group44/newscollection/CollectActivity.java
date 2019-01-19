package com.example.group44.newscollection;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
    LoadingDialog ld;

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
                String title = mNewsList.get(position).title;
                // 删除数据库中的对象
                mDatasource.deleteFavNews(title);
                // 删除内存中的对象
                mNewsList.remove(position);
                myAdapter.notifyDataSetChanged();
                Log.d(TAG, "onLongClick: delete favorite news " + title);
            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.collectRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }
}
