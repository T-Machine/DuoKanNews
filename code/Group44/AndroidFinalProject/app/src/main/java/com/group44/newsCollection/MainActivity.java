package com.group44.newsCollection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.group44.newsCollection.fragment.ContentFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 主页面
 */
public class MainActivity extends SlidingFragmentActivity {
    private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    private static final String TAG_CONTENT = "TAG_CONTENT";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();

        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏触摸
        slidingMenu.setBehindOffset(450);//屏幕预留100像素宽度

        // init sliding menu function
        Switch settingNightMode = slidingMenu.findViewById(R.id.lm_s_night_mode);
        LinearLayout allNewsGroup = slidingMenu.findViewById(R.id.lm_g_all_news);
        LinearLayout recommendNews = slidingMenu.findViewById(R.id.lm_g_recommend_news);
        LinearLayout settingTextSize = slidingMenu.findViewById(R.id.lm_g_text_size);


        // 页面跳转逻辑
        allNewsGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to all news panel
                Toast.makeText(MainActivity.this, "all news", Toast.LENGTH_SHORT).show();
            }
        });

        recommendNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to recommend news panel
                Toast.makeText(MainActivity.this, "推荐", Toast.LENGTH_SHORT).show();

            }
        });

        settingTextSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change font size
                Toast.makeText(MainActivity.this, "更改字体设置", Toast.LENGTH_SHORT).show();
            }
        });

        initFragment();
    }

    /**
     * 初始化Fragment
     */
    private void initFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();//开启事务
        // 参1：帧布局容器的id；参2：要替换的fragment；参3：要打上的标签的标签名
        transaction.replace(R.id.fl_main,new ContentFragment(),TAG_CONTENT);
        transaction.commitNow();//提交事务
    }


    /**
     * 获取内容Fragment对象
     * @return
     */
    public ContentFragment getContentFragment(){
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment contentFragment = (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
        return contentFragment;
    }
}
