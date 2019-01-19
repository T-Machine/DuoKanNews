package com.example.group44.newscollection.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.example.group44.newscollection.JSON.Feed;
import com.example.group44.newscollection.JSON.JsonRootBean;
import com.example.group44.newscollection.MainActivity;
import com.example.group44.newscollection.R;
import com.example.group44.newscollection.persistence.AppRepository;
import com.example.group44.newscollection.persistence.DetectWords;
import com.example.group44.newscollection.persistence.WordFrequency;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


import jackmego.com.jieba_android.JiebaSegmenter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



// 对多个URL进行判断，获取，返回类
public class MainActivityNetworkVisit {

    private AppRepository mDatasource;
    // 存放结果
    private ArrayList< List<Feed> > listOfList = new ArrayList<>();
    private ArrayList<Feed> feedList = new ArrayList<>();
    ArrayList<String> setOfUrls = new ArrayList<>();
    private static MainActivityNetworkVisit instance=null;
    private Context context;
    // 推荐
    static String recommand = "http://newsapi.sina.cn/?resource=feed&lDid=a9f1b781-e891-4198-af53-1fb74ab3ad1b&oldChwm=&upTimes=0&city=&prefetch=99&channel=news_toutiao&link=&ua=Xiaomi-MI+6__sinanews__6.8.8__android__8.0.0&deviceId=aeaaa73c147faf4e&connectionType=2&resolution=1080x1920&weiboUid=&mac=02%3A00%3A00%3A00%3A00%3A00&replacedFlag=0&osVersion=8.0.0&chwm=14010_0001&pullTimes=1&weiboSuid=&andId=301aa36754a2692e&from=6068895012&sn=8a8a0650&behavior=auto&aId=&localSign=a_22eb3a47-189e-44ac-be6d-81ef8ac635b6&deviceIdV1=aeaaa73c147faf4e&todayReqTime=0&osSdk=26&abver=1527581432688&listCount=0&accessToken=&downTimes=0&abt=313_302_297_281_277_275_269_255_253_251_249_242_237_230_228_226_217_215_207_203_191_189_187_171_153_149_143_141_139_135_128_113_111_57_45_38_21_18_16_13&lastTimestamp=0&pullDirection=down&seId=e70c98e4da&imei=868030036302089&deviceModel=Xiaomi__Xiaomi__MI+6&location=0.0%2C0.0&loadingAdTimestamp=0&urlSign=befedbd988&rand=926";
    // 国内
    static String insideCountry = "http://newsapi.sina.cn/?resource=feed&mpName=%E5%86%9B%E4%BA%8B&lDid=a9f1b781-e891-4198-af53-1fb74ab3ad1b&oldChwm=&upTimes=0&city=WMXX1325&channel=news_mil&link=&ua=Xiaomi-MI+6__sinanews__6.8.8__android__8.0.0&deviceId=aeaaa73c147faf4e&connectionType=2&resolution=1080x1920&weiboUid=&mac=02%3A00%3A00%3A00%3A00%3A00&replacedFlag=0&osVersion=8.0.0&chwm=14010_0001&pullTimes=1&weiboSuid=&andId=301aa36754a2692e&from=6068895012&sn=8a8a0650&behavior=auto&aId=01AlChOlO32q8qK9_DcXaNg1HvKsu2sA1te3zs8LZ8aJ2s_Kk.&localSign=a_141aa449-c826-47e1-905c-aad9e842a805&deviceIdV1=aeaaa73c147faf4e&todayReqTime=0&osSdk=26&abver=1527581432688&listCount=0&accessToken=&downTimes=0&abt=313_302_297_281_277_275_269_255_253_251_249_242_237_230_228_226_217_215_207_203_191_189_187_171_153_149_143_141_139_135_128_113_111_57_45_38_21_18_16_13&lastTimestamp=0&pullDirection=down&seId=4e58167e19&imei=868030036302089&deviceModel=Xiaomi__Xiaomi__MI+6&location=30.581284%2C103.984453&loadingAdTimestamp=0&urlSign=1b10f6f6d9&rand=430";
    // 国际
    static String outsideCountry = "http://newsapi.sina.cn/?resource=feed&mpName=%E5%A8%B1%E4%B9%90&lDid=a9f1b781-e891-4198-af53-1fb74ab3ad1b&oldChwm=&upTimes=0&city=WMXX1325&channel=news_ent&link=&ua=Xiaomi-MI+6__sinanews__6.8.8__android__8.0.0&deviceId=aeaaa73c147faf4e&connectionType=2&resolution=1080x1920&weiboUid=&mac=02%3A00%3A00%3A00%3A00%3A00&replacedFlag=0&osVersion=8.0.0&chwm=14010_0001&pullTimes=1&weiboSuid=&andId=301aa36754a2692e&from=6068895012&sn=8a8a0650&behavior=auto&aId=01AlChOlO32q8qK9_DcXaNg1HvKsu2sA1te3zs8LZ8aJ2s_Kk.&localSign=a_d3885852-f19a-443d-ac93-049b6caeb3c1&deviceIdV1=aeaaa73c147faf4e&todayReqTime=0&osSdk=26&abver=1527581432688&listCount=0&accessToken=&downTimes=0&abt=313_302_297_281_277_275_269_255_253_251_249_242_237_230_228_226_217_215_207_203_191_189_187_171_153_149_143_141_139_135_128_113_111_57_45_38_21_18_16_13&lastTimestamp=0&pullDirection=down&seId=c653aad2fc&imei=868030036302089&deviceModel=Xiaomi__Xiaomi__MI+6&location=30.581029%2C103.984494&loadingAdTimestamp=0&urlSign=b862a31e45&rand=267";
    // 生活
    static String life = "http://newsapi.sina.cn/?resource=feed&lDid=a9f1b781-e891-4198-af53-1fb74ab3ad1b&oldChwm=&upTimes=0&city=&prefetch=99&channel=news_toutiao&link=&ua=Xiaomi-MI+6__sinanews__6.8.8__android__8.0.0&deviceId=aeaaa73c147faf4e&connectionType=2&resolution=1080x1920&weiboUid=&mac=02%3A00%3A00%3A00%3A00%3A00&replacedFlag=0&osVersion=8.0.0&chwm=14010_0001&pullTimes=1&weiboSuid=&andId=301aa36754a2692e&from=6068895012&sn=8a8a0650&behavior=auto&aId=&localSign=a_22eb3a47-189e-44ac-be6d-81ef8ac635b6&deviceIdV1=aeaaa73c147faf4e&todayReqTime=0&osSdk=26&abver=1527581432688&listCount=0&accessToken=&downTimes=0&abt=313_302_297_281_277_275_269_255_253_251_249_242_237_230_228_226_217_215_207_203_191_189_187_171_153_149_143_141_139_135_128_113_111_57_45_38_21_18_16_13&lastTimestamp=0&pullDirection=down&seId=e70c98e4da&imei=868030036302089&deviceModel=Xiaomi__Xiaomi__MI+6&location=0.0%2C0.0&loadingAdTimestamp=0&urlSign=befedbd988&rand=926";
    // 军事
    static String army = "http://newsapi.sina.cn/?resource=feed&mpName=%E5%86%9B%E4%BA%8B&lDid=a9f1b781-e891-4198-af53-1fb74ab3ad1b&oldChwm=&upTimes=0&city=WMXX1325&channel=news_mil&link=&ua=Xiaomi-MI+6__sinanews__6.8.8__android__8.0.0&deviceId=aeaaa73c147faf4e&connectionType=2&resolution=1080x1920&weiboUid=&mac=02%3A00%3A00%3A00%3A00%3A00&replacedFlag=0&osVersion=8.0.0&chwm=14010_0001&pullTimes=1&weiboSuid=&andId=301aa36754a2692e&from=6068895012&sn=8a8a0650&behavior=auto&aId=01AlChOlO32q8qK9_DcXaNg1HvKsu2sA1te3zs8LZ8aJ2s_Kk.&localSign=a_141aa449-c826-47e1-905c-aad9e842a805&deviceIdV1=aeaaa73c147faf4e&todayReqTime=0&osSdk=26&abver=1527581432688&listCount=0&accessToken=&downTimes=0&abt=313_302_297_281_277_275_269_255_253_251_249_242_237_230_228_226_217_215_207_203_191_189_187_171_153_149_143_141_139_135_128_113_111_57_45_38_21_18_16_13&lastTimestamp=0&pullDirection=down&seId=4e58167e19&imei=868030036302089&deviceModel=Xiaomi__Xiaomi__MI+6&location=30.581284%2C103.984453&loadingAdTimestamp=0&urlSign=1b10f6f6d9&rand=430";
    // 体育
    static String sports = "http://newsapi.sina.cn/?resource=feed&lDid=a9f1b781-e891-4198-af53-1fb74ab3ad1b&oldChwm=&upTimes=0&city=&prefetch=99&channel=news_toutiao&link=&ua=Xiaomi-MI+6__sinanews__6.8.8__android__8.0.0&deviceId=aeaaa73c147faf4e&connectionType=2&resolution=1080x1920&weiboUid=&mac=02%3A00%3A00%3A00%3A00%3A00&replacedFlag=0&osVersion=8.0.0&chwm=14010_0001&pullTimes=1&weiboSuid=&andId=301aa36754a2692e&from=6068895012&sn=8a8a0650&behavior=auto&aId=&localSign=a_22eb3a47-189e-44ac-be6d-81ef8ac635b6&deviceIdV1=aeaaa73c147faf4e&todayReqTime=0&osSdk=26&abver=1527581432688&listCount=0&accessToken=&downTimes=0&abt=313_302_297_281_277_275_269_255_253_251_249_242_237_230_228_226_217_215_207_203_191_189_187_171_153_149_143_141_139_135_128_113_111_57_45_38_21_18_16_13&lastTimestamp=0&pullDirection=down&seId=e70c98e4da&imei=868030036302089&deviceModel=Xiaomi__Xiaomi__MI+6&location=0.0%2C0.0&loadingAdTimestamp=0&urlSign=befedbd988&rand=926";
    // 娱乐
    static String entertain = "http://newsapi.sina.cn/?resource=feed&mpName=%E5%A8%B1%E4%B9%90&lDid=a9f1b781-e891-4198-af53-1fb74ab3ad1b&oldChwm=&upTimes=0&city=WMXX1325&channel=news_ent&link=&ua=Xiaomi-MI+6__sinanews__6.8.8__android__8.0.0&deviceId=aeaaa73c147faf4e&connectionType=2&resolution=1080x1920&weiboUid=&mac=02%3A00%3A00%3A00%3A00%3A00&replacedFlag=0&osVersion=8.0.0&chwm=14010_0001&pullTimes=1&weiboSuid=&andId=301aa36754a2692e&from=6068895012&sn=8a8a0650&behavior=auto&aId=01AlChOlO32q8qK9_DcXaNg1HvKsu2sA1te3zs8LZ8aJ2s_Kk.&localSign=a_d3885852-f19a-443d-ac93-049b6caeb3c1&deviceIdV1=aeaaa73c147faf4e&todayReqTime=0&osSdk=26&abver=1527581432688&listCount=0&accessToken=&downTimes=0&abt=313_302_297_281_277_275_269_255_253_251_249_242_237_230_228_226_217_215_207_203_191_189_187_171_153_149_143_141_139_135_128_113_111_57_45_38_21_18_16_13&lastTimestamp=0&pullDirection=down&seId=c653aad2fc&imei=868030036302089&deviceModel=Xiaomi__Xiaomi__MI+6&location=30.581029%2C103.984494&loadingAdTimestamp=0&urlSign=b862a31e45&rand=267";
    // 科技
    static String tech = "http://newsapi.sina.cn/?resource=feed&lDid=a9f1b781-e891-4198-af53-1fb74ab3ad1b&oldChwm=&upTimes=0&city=&prefetch=99&channel=news_toutiao&link=&ua=Xiaomi-MI+6__sinanews__6.8.8__android__8.0.0&deviceId=aeaaa73c147faf4e&connectionType=2&resolution=1080x1920&weiboUid=&mac=02%3A00%3A00%3A00%3A00%3A00&replacedFlag=0&osVersion=8.0.0&chwm=14010_0001&pullTimes=1&weiboSuid=&andId=301aa36754a2692e&from=6068895012&sn=8a8a0650&behavior=auto&aId=&localSign=a_22eb3a47-189e-44ac-be6d-81ef8ac635b6&deviceIdV1=aeaaa73c147faf4e&todayReqTime=0&osSdk=26&abver=1527581432688&listCount=0&accessToken=&downTimes=0&abt=313_302_297_281_277_275_269_255_253_251_249_242_237_230_228_226_217_215_207_203_191_189_187_171_153_149_143_141_139_135_128_113_111_57_45_38_21_18_16_13&lastTimestamp=0&pullDirection=down&seId=e70c98e4da&imei=868030036302089&deviceModel=Xiaomi__Xiaomi__MI+6&location=0.0%2C0.0&loadingAdTimestamp=0&urlSign=befedbd988&rand=926";
    // 财经
    static String finance = "http://newsapi.sina.cn/?resource=feed&lDid=a9f1b781-e891-4198-af53-1fb74ab3ad1b&oldChwm=&upTimes=0&city=&prefetch=99&channel=news_toutiao&link=&ua=Xiaomi-MI+6__sinanews__6.8.8__android__8.0.0&deviceId=aeaaa73c147faf4e&connectionType=2&resolution=1080x1920&weiboUid=&mac=02%3A00%3A00%3A00%3A00%3A00&replacedFlag=0&osVersion=8.0.0&chwm=14010_0001&pullTimes=1&weiboSuid=&andId=301aa36754a2692e&from=6068895012&sn=8a8a0650&behavior=auto&aId=&localSign=a_22eb3a47-189e-44ac-be6d-81ef8ac635b6&deviceIdV1=aeaaa73c147faf4e&todayReqTime=0&osSdk=26&abver=1527581432688&listCount=0&accessToken=&downTimes=0&abt=313_302_297_281_277_275_269_255_253_251_249_242_237_230_228_226_217_215_207_203_191_189_187_171_153_149_143_141_139_135_128_113_111_57_45_38_21_18_16_13&lastTimestamp=0&pullDirection=down&seId=e70c98e4da&imei=868030036302089&deviceModel=Xiaomi__Xiaomi__MI+6&location=0.0%2C0.0&loadingAdTimestamp=0&urlSign=befedbd988&rand=926";

    // 加锁
    private volatile Lock resultLock = new ReentrantLock();

    public static MainActivityNetworkVisit getInstance(){
        if(instance == null){
            instance = new MainActivityNetworkVisit();
        }
        return instance;
    }
    private int result = 0;
    public void setUrl(String type){

        String[] key = null;
        key = type.split(",");
        for(String e : key){
            if(e.equals("1")) {
                setOfUrls.add(insideCountry);
                continue;
            }
            if(e.equals("2")) {
                setOfUrls.add(outsideCountry);
                continue;
            }
            if(e.equals("3")) {
                setOfUrls.add(life);
                continue;
            }
            if(e.equals("4")) {
                setOfUrls.add(army);
                continue;
            }
            if(e.equals("5")) {
                setOfUrls.add(sports);
                continue;
            }
            if(e.equals("6")) {
                setOfUrls.add(entertain);
                continue;
            }
            if(e.equals("7")) {
                setOfUrls.add(tech);
                continue;
            }
            if(e.equals("8")) {
                setOfUrls.add(finance);
                continue;
            }
        }

        if(setOfUrls.size() > 3){
            // 从里面随机获取三类
            Integer r1 = (int)(Math.random() * setOfUrls.size());
            Integer r2 = (int)(Math.random() * setOfUrls.size());
            while(r2 == r1){
                r2 = (int)(Math.random() * setOfUrls.size());
            }
            Integer r3 = (int)(Math.random() * setOfUrls.size());
            while(r3 == r2 || r3 == r1){
                r3 = (int)(Math.random() * setOfUrls.size());
            }

            Log.i("select type",r1.toString() + " " + r2.toString() + " " + r3.toString());
            ArrayList<String> tmp = new ArrayList<>();
            for(int i = 0; i < 3; i++){
                tmp.add(setOfUrls.get(i));
            }
            setOfUrls = tmp;
        }

        setOfUrls.add(recommand);
        getNews();
    }

    public void getNews(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(String url : setOfUrls){
                    // 获取json 同步请求
                    OkHttpClient okHttpClient = new OkHttpClient();
                    final Request request = new Request.Builder()
                            .url(url)
                            .get()//默认就是GET请求，可以不写
                            .build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("OKHTTP", "No Internet");
                            HandlerManager.getInstance().sendFailMessage();
                            return;
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.i("onresponse","getREsult");
                            if (response.isSuccessful()) {
                                Gson gson = new Gson();//创建Gson对象
                                JsonRootBean bean = gson.fromJson(response.body().string(), JsonRootBean.class);//解析
                                List<Feed> feed = bean.getData().getFeed();
                                // todo:分词处理
                                // 不能太多相关新闻
                                for(Feed f : feed){
                                    if(feedList.size() >= 2) break;
                                    if(f.getTitle().equals("")) continue;
                                    String origin = f.getTitle() + f.getLongTitle();
                                    ArrayList<String> wordList = JiebaSegmenter.getJiebaSegmenterSingleton().getDividedString(origin);

                                    // 是否出现过
                                    boolean isExisted = false;
                                    for(Feed tmp : feedList){
                                        if(tmp.getTitle().equals(f.getTitle())){
                                            isExisted = true;
                                            break;
                                        }
                                    }
                                    if(isExisted) continue;
                                    for(String str : wordList){
                                        if(!DetectWords.inValid(str)) continue;
                                        if(mDatasource.getFrequency(str) != null) {
                                            Log.d("JIEBA", "fetch" + str);
                                            if(feedList.size() >= 2)
                                                feedList.add(f);
                                            break;
                                        }
                                    }
                                }

                                listOfList.add(feed);
                                resultLock.lock();
                                if(result < setOfUrls.size() - 1){
                                    result ++;
                                } else{
                                    //不够则随机选择
                                    if(feedList.size() < 5){
                                        // 需要有推荐新闻 * 2
                                        boolean isFirst = true;
                                        boolean isSecond = true;
                                        boolean isExisted = false;
                                        while(feedList.size() < 5){
                                            Random rand = new Random();
                                            Integer randomIndex1 = rand.nextInt(listOfList.size());
                                            if(!isFirst && isSecond){
                                                randomIndex1 = listOfList.size() - 1;
                                                isSecond = false;
                                            }
                                            if(isFirst && isSecond){
                                                randomIndex1 = listOfList.size() - 1;
                                                isFirst = false;
                                            }

                                            if(listOfList.get(randomIndex1).size() == 0) break;
                                            Integer randomIndex2 = rand.nextInt(listOfList.get(randomIndex1).size());
                                            Feed tmp = listOfList.get(randomIndex1).get(randomIndex2);
                                            if(tmp.getTitle().equals("")) continue;
                                            Log.i("random", randomIndex1.toString() + " " + randomIndex2.toString() + " " + feedList.size());
                                            for(Feed e : feedList){
                                                if(e.getTitle().equals(tmp.getTitle())){
                                                    isExisted = true;
                                                    Log.i("random",tmp.getTitle() + " " + e.getTitle() + "isExisted");
                                                    break;

                                                }
                                            }
                                            // 存在去除
                                            if(isExisted){
                                                continue;
                                            }
                                            feedList.add(tmp);
                                        }
                                    }
                                    HandlerManager.getInstance().sendSuccessMessage();
                                }
                                resultLock.unlock();
                            }
                        }
                    });
                }
            }
        }).start();
    }
    public void setContext(Context context) {
        this.context = context;
        mDatasource = new AppRepository(context);
    }

    public ArrayList<Feed> getFeedList() {
        return feedList;
    }
}
