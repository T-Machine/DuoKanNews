package com.example.group44.newscollection;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    private AppRepository mDatasource;

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
                title.setTextSize(title.getTextSize()*mTextsize/3);
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
                if(isCardShow == true) {
                    return;
                }
                isCardShow = true;

                //previewCard.setVisibility(View.VISIBLE);
//                blackShodow.setVisibility(View.VISIBLE);
//                view_pager.setVisibility(View.VISIBLE);
//                hidden_card.setVisibility(View.VISIBLE);

//                view pager定位到当前item
                view_pager.setCurrentItem(position);
            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.collectRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }
}
