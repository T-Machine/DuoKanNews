package com.example.group44.newscollection.persistence;

import android.app.Application;

/**
 * manage data resource of data in this app, including word frequency table
 * and user's favorite news collection
 */
public class AppRepository {
    private WordFrequencyDao mWFDao;



    AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mWFDao = db.wordDao();
    }



    // query data frequency of specific word

    //

}

