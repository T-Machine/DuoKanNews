package com.example.group44.newscollection.persistence;

import android.content.Context;

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
    public AppRepository(Context application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mAppDao = db.wordFrequcyDao();
    }

    /**
     * query frequency of a specific word
     * @param word
     * @return frequency
     */
    public int getFrequency(String word) {

        return mAppDao.getFrequency(word);
    }

    /**
     * update frequency a specific word
     * @param newWf, a new instance of word-frequency pair
     */
    public void updateFrequency(WordFrequency newWf) {
        mAppDao.updateFrequency(newWf);
    }

    /**
     * increase specific word frequency by 1
     * @param word
     */
    public void increaseFrenquency(String word) {
        updateFrequency(new WordFrequency(word, getFrequency(word) + 1));
    }

    /**
     * increase specific word frequency by 1
     * @param word
     * @return false if frequency already has been zero
     */
    public boolean decreaseFrenquency(String word) {
        if(getFrequency(word) == 0) {
            return false;
        }
        updateFrequency(new WordFrequency(word, getFrequency(word) - 1));
        return true;
    }

    public void insertNewWord(String word) {
        mAppDao.insertWF(new WordFrequency(word, 1));
    }


    /**
     * insert a user's favorite news into database
     * @param newFavNews: instance of news
     */
    public void insertNewFavNews(FavoriteNews newFavNews) {
        mAppDao.insertFavNews(newFavNews);
    }

    /**
     * return all favorite news of the user
     * @return List of FavoriteNews
     */
    public List<FavoriteNews> queryAllFavNews() {
        return mAppDao.getAllFavoriteNews();
    }

    /**
     * query favorite news by title
     * @param title
     * @return
     */
    public List<FavoriteNews> queryNewsByTitle(String title) {
        return mAppDao.getFavoriteNewsByTitle(title);
    }

}

