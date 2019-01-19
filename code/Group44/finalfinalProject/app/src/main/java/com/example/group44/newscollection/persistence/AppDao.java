package com.example.group44.newscollection.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface AppDao {

    // return all word frequency pair from database
    @Query("SELECT * from word_frequency_table ORDER BY frequency ASC")
    List<WordFrequency> getAllWordFrequencyPair();

    // wf for word frequency pair
    @Insert
    void insertWF(WordFrequency wf);

    @Update
    void updateFrequency(WordFrequency newWF);

    @Query("DELETE FROM word_frequency_table")
    void deleteAll();

    @Query("DELETE FROM word_frequency_table WHERE word == :word")
    void deleteWord(String word);

    @Query("SELECT frequency FROM word_frequency_table WHERE word == :queryWord")
    Integer getFrequency(String queryWord);

    // conflict error
    @Insert
    void insertFavNews(FavoriteNews news);

    @Query("SELECT * from  fav_news ORDER BY id ASC")
    List<FavoriteNews> getAllFavoriteNews();

    @Delete
    void deleteFavNews(FavoriteNews... news);


    @Query("SELECT * from fav_news WHERE title == :title ORDER BY id ASC")
    List<FavoriteNews> getFavoriteNewsByTitle(String title);

}
