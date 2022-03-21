package com.example.asasfans.data;

import java.util.List;

/**
 * @author zyxdb
 * @description Gson根据json生成的数据类，根据uid获得的json，https://api.tokyo.vtbs.moe/v1/detail
 */

public class VTBDataBean {
    /**
     "mid": 672346917,
     "uuid": "d0da0629-f680-5914-aa23-aacc56bf6674",
     "uname": "向晚大魔王",
     "video": 162,
     "roomid": 22625025,
     "sign": "关注...也不是不可以啦！",
     "notice": "",
     "face": "http://i0.hdslb.com/bfs/face/566078c52b408571d8ae5e3bcdf57b2283024c27.jpg",
     "rise": 583,
     "topPhoto": "http://i1.hdslb.com/bfs/space/56303dd5fa6518721f14779c9411bb19214f25e7.png",
     "archiveView": 0,
     "follower": 591908,
     "liveStatus": false,
     "recordNum": 11936,
     "guardNum": 1300,
     "lastLive": {
     "online": 4136458,
     "time": 1647698578876},
     "guardChange": 3069,
     "guardType": [0,10,2600],
     "online": false,
     "title": "【3D】枝江一千零一夜",
     "time": 1647868110270,
     "liveStartTime": 0
     */

    private int mid;
    private String uuid;
    private String uname;
    private int video;
    private int roomid;
    private String sign;
    private String notice;
    private String face;
    private int rise;
    private String topPhoto;
    private int archiveView;
    private int follower;
    private boolean liveStatus;
    private int recordNum;
    private int guardNum;
    private LastLiveBean lastLive;
    private int guardChange;
    private List<Integer> guardType;
    private boolean online;
    private String title;
    private long time;
    private int liveStartTime;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }

    public int getRoomid() {
        return roomid;
    }

    public void setRoomid(int roomid) {
        this.roomid = roomid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public int getRise() {
        return rise;
    }

    public void setRise(int rise) {
        this.rise = rise;
    }

    public String getTopPhoto() {
        return topPhoto;
    }

    public void setTopPhoto(String topPhoto) {
        this.topPhoto = topPhoto;
    }

    public int getArchiveView() {
        return archiveView;
    }

    public void setArchiveView(int archiveView) {
        this.archiveView = archiveView;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public boolean isLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(boolean liveStatus) {
        this.liveStatus = liveStatus;
    }

    public int getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(int recordNum) {
        this.recordNum = recordNum;
    }

    public int getGuardNum() {
        return guardNum;
    }

    public void setGuardNum(int guardNum) {
        this.guardNum = guardNum;
    }

    public LastLiveBean getLastLive() {
        return lastLive;
    }

    public void setLastLive(LastLiveBean lastLive) {
        this.lastLive = lastLive;
    }

    public int getGuardChange() {
        return guardChange;
    }

    public void setGuardChange(int guardChange) {
        this.guardChange = guardChange;
    }

    public List<Integer> getGuardType() {
        return guardType;
    }

    public void setGuardType(List<Integer> guardType) {
        this.guardType = guardType;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getLiveStartTime() {
        return liveStartTime;
    }

    public void setLiveStartTime(int liveStartTime) {
        this.liveStartTime = liveStartTime;
    }

    public static class LastLiveBean {
        private int online;
        private long time;

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}
