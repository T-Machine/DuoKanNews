package com.example.group44.newscollection.persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "word_frequency_table")
public class WordFrequency {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")
    private String mWord;

    @NonNull
    @ColumnInfo(name = "frequency")
    private int mFrequency;

    public WordFrequency(@NonNull String word, @NonNull int frequency) {
        this.mWord = word;
        this.mFrequency = frequency;
    }

    @NonNull
    public String getWord() {
        return this.mWord;
    }

    @NonNull
    public int getFrequency() {
        return this.mFrequency;
    }


}
