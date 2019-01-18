package com.example.group44.newscollection.utils;

import com.example.group44.newscollection.persistence.FavoriteNews;

import java.util.Date;

public class UtilsFunction {
    public static FavoriteNews newsGenerator(String title, String digest, String url, String imgUrl) {
        FavoriteNews n = new FavoriteNews();
        n.title = title;
        n.digest = digest;
        n.srcUrl = url;
        n.imgUrl = imgUrl;
        return n;
    }
}
