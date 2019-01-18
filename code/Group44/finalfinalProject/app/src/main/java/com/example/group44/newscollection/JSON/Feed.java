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
public class Feed {

    private int pos;
    private String newsId;
    private String title;
    private String longTitle;
    private String source = "";
    private String link;
    private String pic;
    private String kpic;
    private String intro;
    private int pubDate = -1;
    private String category;
    private int actionType;
    private int layoutStyle;
    private String showTag;
    private int type;
    private String pdpsId;
    private String uuid;
    private CommentCountInfo commentCountInfo;
    private boolean articlePreload;
    private int relaPos;
    private String adid;
    private String pdps_id;
    private List<String> view;
    private List<String> click;
    private String schemeLink;
    private String packageName;
    private int needRemove;
    private String recommendInfo;
    // add-------
    private String summary;
    public void addSummary(String added){this.summary += added;}
    public void setSummary(String input){this.summary = input;}
    public String getSummary(){return summary;}
    private Integer id;
    public void setID (Integer id){this.id = id;}

    public Integer getId() {
        return id;
    }

    private Integer totalNum;

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    //-----------------
    public void setPos(int pos) {
         this.pos = pos;
     }
     public int getPos() {
         return pos;
     }

    public void setNewsId(String newsId) {
         this.newsId = newsId;
     }
     public String getNewsId() {
         return newsId;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setLongTitle(String longTitle) {
         this.longTitle = longTitle;
     }
     public String getLongTitle() {
         return longTitle;
     }

    public void setSource(String source) {
         this.source = source;
     }
     public String getSource() {
         return source;
     }

    public void setLink(String link) {
         this.link = link;
     }
     public String getLink() {
         return link;
     }

    public void setPic(String pic) {
         this.pic = pic;
     }
     public String getPic() {
         return pic;
     }

    public void setKpic(String kpic) {
         this.kpic = kpic;
     }
     public String getKpic() {
         return kpic;
     }

    public void setIntro(String intro) {
         this.intro = intro;
     }
     public String getIntro() {
         return intro;
     }

    public void setPubDate(int pubDate) {
         this.pubDate = pubDate;
     }
     public int getPubDate() {
         return pubDate;
     }

    public void setCategory(String category) {
         this.category = category;
     }
     public String getCategory() {
         return category;
     }

    public void setActionType(int actionType) {
         this.actionType = actionType;
     }
     public int getActionType() {
         return actionType;
     }

    public void setLayoutStyle(int layoutStyle) {
         this.layoutStyle = layoutStyle;
     }
     public int getLayoutStyle() {
         return layoutStyle;
     }

    public void setShowTag(String showTag) {
         this.showTag = showTag;
     }
     public String getShowTag() {
         return showTag;
     }

    public void setType(int type) {
         this.type = type;
     }
     public int getType() {
         return type;
     }

    public void setPdpsId(String pdpsId) {
         this.pdpsId = pdpsId;
     }
     public String getPdpsId() {
         return pdpsId;
     }

    public void setUuid(String uuid) {
         this.uuid = uuid;
     }
     public String getUuid() {
         return uuid;
     }

    public void setCommentCountInfo(CommentCountInfo commentCountInfo) {
         this.commentCountInfo = commentCountInfo;
     }
     public CommentCountInfo getCommentCountInfo() {
         return commentCountInfo;
     }

    public void setArticlePreload(boolean articlePreload) {
         this.articlePreload = articlePreload;
     }
     public boolean getArticlePreload() {
         return articlePreload;
     }

    public void setRelaPos(int relaPos) {
         this.relaPos = relaPos;
     }
     public int getRelaPos() {
         return relaPos;
     }

    public void setAdid(String adid) {
         this.adid = adid;
     }
     public String getAdid() {
         return adid;
     }

    public void setPdps_id(String pdps_id) {
         this.pdps_id = pdps_id;
     }
     public String getPdps_id() {
         return pdps_id;
     }

    public void setView(List<String> view) {
         this.view = view;
     }
     public List<String> getView() {
         return view;
     }

    public void setClick(List<String> click) {
         this.click = click;
     }
     public List<String> getClick() {
         return click;
     }

    public void setSchemeLink(String schemeLink) {
         this.schemeLink = schemeLink;
     }
     public String getSchemeLink() {
         return schemeLink;
     }

    public void setPackageName(String packageName) {
         this.packageName = packageName;
     }
     public String getPackageName() {
         return packageName;
     }

    public void setNeedRemove(int needRemove) {
         this.needRemove = needRemove;
     }
     public int getNeedRemove() {
         return needRemove;
     }

    public void setRecommendInfo(String recommendInfo) {
         this.recommendInfo = recommendInfo;
     }
     public String getRecommendInfo() {
         return recommendInfo;
     }

}