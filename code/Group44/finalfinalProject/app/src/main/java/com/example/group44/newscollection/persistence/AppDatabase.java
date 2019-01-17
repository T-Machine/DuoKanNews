package com.example.group44.newscollection.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;


@Database(entities = {WordFrequency.class, FavoriteNews.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract AppDao wordFrequcyDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile AppDatabase INSTANCE;

    // init the database with
    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            // used for single instance
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            // for convenience, allow query data in main thread,
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
