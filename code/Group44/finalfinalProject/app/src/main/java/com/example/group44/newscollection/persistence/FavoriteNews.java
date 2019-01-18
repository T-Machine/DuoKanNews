package com.example.group44.newscollection.persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "fav_news")
public class FavoriteNews {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "title")
    public String title;
//
//    @ColumnInfo(name = "date")
//    public Date date;
//
//    @ColumnInfo(name = "source")
//    public String source;

    @ColumnInfo(name = "digest")
    public String digest;

    @ColumnInfo(name = "url")
    public String srcUrl;

//    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
//    public byte[] image;

    @ColumnInfo(name = "image_url")
    public String imgUrl;


//    public FavoriteNews(String title, Date date, String )

}
