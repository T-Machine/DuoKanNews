package com.example.group44.newscollection.persistence;

import android.app.Application;

import java.util.List;

/**
 * Wrapper of DAO, handler of multiple data source
 * manage data resource of data in this app, including word frequency table
 * and user's favorite news collection, if user's favorite news are not presented in
 * local database, fetch data from web service.
 *
 *
 * todo: all function in this class is sync task (may be block the main thread) just for "convenience", which should be
 * todo: replaced with async task later according to best practices.
 */
public class AppRepository {
    private AppDao mAppDao;

    /**
     * constructor of repository
     * @param application: context of activity
     */
    AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mAppDao = db.wordFrequcyDao();
    }

    /**
     * query frequency of a specific word
     * @param word
     * @return frequency
     */
    int getFrequency(String word) {

        return mAppDao.getFrequency(word);
    }

    /**
     * update frequency a specific word
     * @param newWf, a new instance of word-frequency pair
     */
    void updateFrequency(WordFrequency newWf) {
        mAppDao.updateFrequency(newWf);
    }

    /**
     * insert a user's favorite news into database
     * @param newFavNews: instance of news
     */
    void insertNewFavNews(FavoriteNews newFavNews) {
        mAppDao.insertFavNews(newFavNews);
    }

    /**
     * return all favorite news of the user
     * @return List of FavoriteNews
     */
    List<FavoriteNews> queryAllFavNews() {
        return mAppDao.getAllFavoriteNews();
    }


}

