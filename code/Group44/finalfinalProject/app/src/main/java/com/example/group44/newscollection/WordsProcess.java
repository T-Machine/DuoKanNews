package com.example.group44.newscollection;

import android.util.Log;

import com.example.group44.newscollection.JSON.Feed;
import com.example.group44.newscollection.JSON.JsonRootBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordsProcess {
    WordsProcess(JsonRootBean bean){
        final List<Feed> feedList = bean.getData().getFeed();
        ArrayList<String_val> local_str_val = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run(){

            }
        }).start();
    }
}
