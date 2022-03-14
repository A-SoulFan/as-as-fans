package com.example.asasfans.data;

/**
 * @author akarinini
 * @description 从json中提取视频信息，构建统一的单个视频数据，目前仅在AUHot中使用
 */

public class SingleVideo {
    private String PicUrl;
    private String Title;
    private int Duration;
    private String Author;
    private int View;
    private int Like;
    private String Tname;
    private String Bvid;

    public String getBvid() {
        return Bvid;
    }

    public void setBvid(String bvid) {
        Bvid = bvid;
    }

    public SingleVideo(){

    }

    public String getTname() {
        return Tname;
    }

    public void setTname(String tname) {
        Tname = tname;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public int getView() {
        return View;
    }

    public void setView(int view) {
        View = view;
    }

    public int getLike() {
        return Like;
    }

    public void setLike(int like) {
        Like = like;
    }
}
