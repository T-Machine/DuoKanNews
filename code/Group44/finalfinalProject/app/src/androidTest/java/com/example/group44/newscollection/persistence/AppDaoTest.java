package com.example.group44.newscollection.persistence;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class AppDaoTest {

    private static final String TAG = "DAO_TEST";
    private AppDao mDao;
    private AppDatabase mDb;


    // create db
    private void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build();

        mDao = mDb.wordFrequcyDao();
    }

    @Before
    public void setUp() throws Exception {
        createDb();
    }

    @After
    public void tearDown() throws Exception {
        mDb.close();
    }

    @Test
    public void getAllWordFrequencyPair() {
        // 先插入之后再删除，查看不同的地方

        WordFrequency wf1 = new WordFrequency("aaa", 1);
        WordFrequency wf2 = new WordFrequency("bbb", 2);
        mDao.insertWF(wf1);
        mDao.insertWF(wf2);
        List<WordFrequency> allData = mDao.getAllWordFrequencyPair();
        assertEquals(allData.get(0).getFrequency(), wf1.getFrequency());
        assertEquals(allData.get(1).getFrequency(), wf1.getFrequency());
    }

    @Test
    public void updateFrequency() {
        WordFrequency wf1 = new WordFrequency("aaa", 1);
        mDao.insertWF(wf1);
        WordFrequency wf2 = new WordFrequency("aaa", 2);
        mDao.updateFrequency(wf2);
        assertEquals(mDao.getFrequency("aaa"), wf2.getFrequency());
    }

    @Test
    public void getFrequency() {
        WordFrequency wf1 = new WordFrequency("aaa", 1);
        mDao.insertWF(wf1);
        assertEquals(mDao.getFrequency("aaa"), wf1.getFrequency());
    }

    @Test
    public void insertFavNews() {
        /**
         * this test function tests both query and insert functionality of DAO
         */
        mDao.insertFavNews(newsGenerator(
                "title1",
                new Date(),
                "abc",
                "abc",
                "abc",
                new byte[]{1, 2}
        ));
        Log.d(TAG, "insertFavNews: size of query : " + mDao.getAllFavoriteNews().size());
        assertEquals(mDao.getAllFavoriteNews().get(0).title, "title1");
    }

    private FavoriteNews newsGenerator(String title, Date date, String source, String digest, String url, byte[] image) {
        FavoriteNews n = new FavoriteNews();
        n.date = date;
        n.title = title;
        n.source = source;
        n.digest = digest;
        n.srcUrl = url;
        n.image = image;
        return n;
    }
}