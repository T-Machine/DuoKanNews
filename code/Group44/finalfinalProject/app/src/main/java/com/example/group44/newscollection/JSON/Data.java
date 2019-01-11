/**
  * Copyright 2019 bejson.com 
  */
package com.example.group44.newscollection.JSON;
import java.util.List;

/**
 * Auto-generated: 2019-01-11 20:55:24
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Data {

    private int isIntro;
    private String feedDownType;
    private int feedLastIndex;
    private int filterRepet;
    private int bufferSize;
    private int bufferUpTimes;
    private int insertToIndex;
    private String downText;
    private List<Feed> feed;
    private Ad ad;
    private SearchBar searchBar;
    private long lastTimestamp;
    public void setIsIntro(int isIntro) {
         this.isIntro = isIntro;
     }
     public int getIsIntro() {
         return isIntro;
     }

    public void setFeedDownType(String feedDownType) {
         this.feedDownType = feedDownType;
     }
     public String getFeedDownType() {
         return feedDownType;
     }

    public void setFeedLastIndex(int feedLastIndex) {
         this.feedLastIndex = feedLastIndex;
     }
     public int getFeedLastIndex() {
         return feedLastIndex;
     }

    public void setFilterRepet(int filterRepet) {
         this.filterRepet = filterRepet;
     }
     public int getFilterRepet() {
         return filterRepet;
     }

    public void setBufferSize(int bufferSize) {
         this.bufferSize = bufferSize;
     }
     public int getBufferSize() {
         return bufferSize;
     }

    public void setBufferUpTimes(int bufferUpTimes) {
         this.bufferUpTimes = bufferUpTimes;
     }
     public int getBufferUpTimes() {
         return bufferUpTimes;
     }

    public void setInsertToIndex(int insertToIndex) {
         this.insertToIndex = insertToIndex;
     }
     public int getInsertToIndex() {
         return insertToIndex;
     }

    public void setDownText(String downText) {
         this.downText = downText;
     }
     public String getDownText() {
         return downText;
     }

    public void setFeed(List<Feed> feed) {
         this.feed = feed;
     }
     public List<Feed> getFeed() {
         return feed;
     }

    public void setAd(Ad ad) {
         this.ad = ad;
     }
     public Ad getAd() {
         return ad;
     }

    public void setSearchBar(SearchBar searchBar) {
         this.searchBar = searchBar;
     }
     public SearchBar getSearchBar() {
         return searchBar;
     }

    public void setLastTimestamp(long lastTimestamp) {
         this.lastTimestamp = lastTimestamp;
     }
     public long getLastTimestamp() {
         return lastTimestamp;
     }

}