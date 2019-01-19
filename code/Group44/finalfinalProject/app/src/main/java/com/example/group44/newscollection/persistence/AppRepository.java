package com.example.group44.newscollection.persistence;

import android.content.Context;

import java.util.ArrayList;
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
     * 构造函数
     * constructor of repository
     * @param application: context of activity
     */
    public AppRepository(Context application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mAppDao = db.wordFrequcyDao();
    }

    //---------------------------------------（单词频率相关接口）----------------------------------//

    /**
     * 查询特定单词的频数
     *
     * @param word
     * @return frequency
     */
    public Integer getFrequency(String word) {
        return mAppDao.getFrequency(word);
    }

    /**
     * 增加某个单词的频数 + 1
     * increase specific word frequency by 1
     *
     * @param word
     */
    public void increaseFrenquency(String word) {
        updateFrequency(new WordFrequency(word, getFrequency(word) + 1));
    }

    /**
     * 减少某个单词的频数 - 1
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

    /**
     * 添加新的单词, 频数设置为1【todo:检查是否冲突】
     * @param word
     */
    public void insertNewWord(String word) {
        mAppDao.insertWF(new WordFrequency(word, 1));
    }


    /**
     * 更新特定单词的频数, 需要先确保这个单词已经在表中，使用insert插入。
     * update frequency a specific word
     * @param newWf, a new instance of word-frequency pair
     */
    public void updateFrequency(WordFrequency newWf) {
        mAppDao.updateFrequency(newWf);
    }

    /**
     * 放回所有的单词和对应频数，返回的序列按照频数升序排列
     * @return
     */
    public List<WordFrequency> getAllWFPair() {
        return mAppDao.getAllWordFrequencyPair();
    }

    /**
     * 删除特定的词
     * @return
     */
    public void deleteWFPair(String word) {
        mAppDao.deleteWord(word);
    }


    // -------------------------------（收藏新闻相关接口）---------------------------------------------//

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

    /**
     * 删除收藏的新闻
     * @param title =》 要被删除的新闻
     */
    public void deleteFavNews(String title) {
        mAppDao.deleteFavNewsByTitle(title);
    }

}

