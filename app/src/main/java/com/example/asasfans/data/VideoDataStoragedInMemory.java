package com.example.asasfans.data;

public class VideoDataStoragedInMemory {
    private String PicUrl;
    private String Title;
    private int Duration;
    private String Author;
    private int View;
    private int Like;
    private String Tname;
    private String Bvid;
    private Boolean firstLoad;

    public VideoDataStoragedInMemory(String picUrl, String title, int duration, String author, int view, int like, String tname, String bvid, Boolean firstLoad) {
        PicUrl = picUrl;
        Title = title;
        Duration = duration;
        Author = author;
        View = view;
        Like = like;
        Tname = tname;
        Bvid = bvid;
        this.firstLoad = firstLoad;
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

    public String getTname() {
        return Tname;
    }

    public void setTname(String tname) {
        Tname = tname;
    }

    public String getBvid() {
        return Bvid;
    }

    public void setBvid(String bvid) {
        Bvid = bvid;
    }

    public Boolean getFirstLoad() {
        return firstLoad;
    }

    public void setFirstLoad(Boolean firstLoad) {
        this.firstLoad = firstLoad;
    }
}
