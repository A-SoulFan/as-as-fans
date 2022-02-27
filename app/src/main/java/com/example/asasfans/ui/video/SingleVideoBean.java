package com.example.asasfans.ui.video;

import java.util.List;

/**
 * @author akarinini
 * @description Gson根据json生成的数据类，单个视频根据bvid获得的json，https://api.bilibili.com/x/web-interface/view?bvid=
 */

public class SingleVideoBean {
    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"bvid":"BV1oL411K79F","aid":466728127,"videos":1,"tid":17,"tname":"单机游戏","copyright":1,"pic":"http://i2.hdslb.com/bfs/archive/f5b75ce77e44af18b63175e51c4e2196b2c96a94.jpg","title":"P4G实况P1\u2014\u2014惊愕：有为青年转学第一天遇到命案","pubdate":1645402151,"ctime":1645402151,"desc":"游戏名persona 4 golden\n直播留档，打算全程把主线故事看完读完，因为B站已经取消分P了，所以得用合集了","desc_v2":[{"raw_text":"游戏名persona 4 golden\n直播留档，打算全程把主线故事看完读完，因为B站已经取消分P了，所以得用合集了","type":1,"biz_id":0}],"state":0,"duration":5802,"rights":{"bp":0,"elec":0,"download":1,"movie":0,"pay":0,"hd5":0,"no_reprint":1,"autoplay":1,"ugc_pay":0,"is_cooperation":0,"ugc_pay_preview":0,"no_background":0,"clean_mode":0,"is_stein_gate":0,"is_360":0,"no_share":0},"owner":{"mid":13046,"name":"少年Pi","face":"http://i2.hdslb.com/bfs/face/6e3b84c1fe71caf523ed87d264f9026013af1c2c.jpg"},"stat":{"aid":466728127,"view":30249,"danmaku":1109,"reply":277,"favorite":955,"coin":2661,"share":50,"now_rank":0,"his_rank":0,"like":3501,"dislike":0,"evaluation":"","argue_msg":""},"dynamic":"","cid":513557884,"dimension":{"width":1920,"height":1080,"rotate":0},"season_id":210709,"no_cache":false,"pages":[{"cid":513557884,"page":1,"from":"vupload","part":"2022-02-21 00-36-18","duration":5802,"vid":"","weblink":"","dimension":{"width":1920,"height":1080,"rotate":0},"first_frame":"http://i1.hdslb.com/bfs/storyff/n220221a2h1qa0t9px5hibtuzh9zmgoy_firsti.jpg"}],"subtitle":{"allow_submit":false,"list":[]},"ugc_season":{"id":210709,"title":"P4S","cover":"https://archive.biliimg.com/bfs/archive/3170440633159185188b7aa7fd962a21eff75c14.jpg","mid":13046,"intro":"","sign_state":0,"attribute":28,"sections":[{"season_id":210709,"id":242855,"title":"正片","type":1,"episodes":[{"season_id":210709,"section_id":242855,"id":2497626,"aid":466728127,"cid":513557884,"title":"P4S实况P1\u2014\u2014惊愕：有为青年转学第一天遇到命案","attribute":0,"arc":{"aid":466728127,"videos":0,"type_id":0,"type_name":"","copyright":0,"pic":"http://i2.hdslb.com/bfs/archive/f5b75ce77e44af18b63175e51c4e2196b2c96a94.jpg","title":"P4S实况P1\u2014\u2014惊愕：有为青年转学第一天遇到命案","pubdate":1645402151,"ctime":1645402151,"desc":"","state":0,"duration":5802,"rights":{"bp":0,"elec":0,"download":0,"movie":0,"pay":0,"hd5":0,"no_reprint":0,"autoplay":0,"ugc_pay":0,"is_cooperation":0,"ugc_pay_preview":0},"author":{"mid":0,"name":"","face":""},"stat":{"aid":466728127,"view":30249,"danmaku":1109,"reply":277,"fav":955,"coin":2661,"share":50,"now_rank":0,"his_rank":0,"like":3501,"dislike":0,"evaluation":"","argue_msg":""},"dynamic":"","dimension":{"width":0,"height":0,"rotate":0},"desc_v2":null},"page":{"cid":513557884,"page":1,"from":"vupload","part":"2022-02-21 00-36-18","duration":5802,"vid":"","weblink":"","dimension":{"width":1920,"height":1080,"rotate":0}},"bvid":"BV1oL411K79F"}]}],"stat":{"season_id":210709,"view":30249,"danmaku":1109,"reply":277,"fav":955,"coin":2661,"share":50,"now_rank":0,"his_rank":0,"like":3501},"ep_count":1,"season_type":1},"is_season_display":true,"user_garb":{"url_image_ani_cut":"http://i0.hdslb.com/bfs/garb/item/f9a3f4aadb1cf268fc411c7b4cc99d07df3e778a.bin"},"honor_reply":{}}
     */

    private int code;
    private String message;
    private int ttl;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bvid : BV1oL411K79F
         * aid : 466728127
         * videos : 1
         * tid : 17
         * tname : 单机游戏
         * copyright : 1
         * pic : http://i2.hdslb.com/bfs/archive/f5b75ce77e44af18b63175e51c4e2196b2c96a94.jpg
         * title : P4G实况P1——惊愕：有为青年转学第一天遇到命案
         * pubdate : 1645402151
         * ctime : 1645402151
         * desc : 游戏名persona 4 golden
         直播留档，打算全程把主线故事看完读完，因为B站已经取消分P了，所以得用合集了
         * desc_v2 : [{"raw_text":"游戏名persona 4 golden\n直播留档，打算全程把主线故事看完读完，因为B站已经取消分P了，所以得用合集了","type":1,"biz_id":0}]
         * state : 0
         * duration : 5802
         * rights : {"bp":0,"elec":0,"download":1,"movie":0,"pay":0,"hd5":0,"no_reprint":1,"autoplay":1,"ugc_pay":0,"is_cooperation":0,"ugc_pay_preview":0,"no_background":0,"clean_mode":0,"is_stein_gate":0,"is_360":0,"no_share":0}
         * owner : {"mid":13046,"name":"少年Pi","face":"http://i2.hdslb.com/bfs/face/6e3b84c1fe71caf523ed87d264f9026013af1c2c.jpg"}
         * stat : {"aid":466728127,"view":30249,"danmaku":1109,"reply":277,"favorite":955,"coin":2661,"share":50,"now_rank":0,"his_rank":0,"like":3501,"dislike":0,"evaluation":"","argue_msg":""}
         * dynamic :
         * cid : 513557884
         * dimension : {"width":1920,"height":1080,"rotate":0}
         * season_id : 210709
         * no_cache : false
         * pages : [{"cid":513557884,"page":1,"from":"vupload","part":"2022-02-21 00-36-18","duration":5802,"vid":"","weblink":"","dimension":{"width":1920,"height":1080,"rotate":0},"first_frame":"http://i1.hdslb.com/bfs/storyff/n220221a2h1qa0t9px5hibtuzh9zmgoy_firsti.jpg"}]
         * subtitle : {"allow_submit":false,"list":[]}
         * ugc_season : {"id":210709,"title":"P4S","cover":"https://archive.biliimg.com/bfs/archive/3170440633159185188b7aa7fd962a21eff75c14.jpg","mid":13046,"intro":"","sign_state":0,"attribute":28,"sections":[{"season_id":210709,"id":242855,"title":"正片","type":1,"episodes":[{"season_id":210709,"section_id":242855,"id":2497626,"aid":466728127,"cid":513557884,"title":"P4S实况P1\u2014\u2014惊愕：有为青年转学第一天遇到命案","attribute":0,"arc":{"aid":466728127,"videos":0,"type_id":0,"type_name":"","copyright":0,"pic":"http://i2.hdslb.com/bfs/archive/f5b75ce77e44af18b63175e51c4e2196b2c96a94.jpg","title":"P4S实况P1\u2014\u2014惊愕：有为青年转学第一天遇到命案","pubdate":1645402151,"ctime":1645402151,"desc":"","state":0,"duration":5802,"rights":{"bp":0,"elec":0,"download":0,"movie":0,"pay":0,"hd5":0,"no_reprint":0,"autoplay":0,"ugc_pay":0,"is_cooperation":0,"ugc_pay_preview":0},"author":{"mid":0,"name":"","face":""},"stat":{"aid":466728127,"view":30249,"danmaku":1109,"reply":277,"fav":955,"coin":2661,"share":50,"now_rank":0,"his_rank":0,"like":3501,"dislike":0,"evaluation":"","argue_msg":""},"dynamic":"","dimension":{"width":0,"height":0,"rotate":0},"desc_v2":null},"page":{"cid":513557884,"page":1,"from":"vupload","part":"2022-02-21 00-36-18","duration":5802,"vid":"","weblink":"","dimension":{"width":1920,"height":1080,"rotate":0}},"bvid":"BV1oL411K79F"}]}],"stat":{"season_id":210709,"view":30249,"danmaku":1109,"reply":277,"fav":955,"coin":2661,"share":50,"now_rank":0,"his_rank":0,"like":3501},"ep_count":1,"season_type":1}
         * is_season_display : true
         * user_garb : {"url_image_ani_cut":"http://i0.hdslb.com/bfs/garb/item/f9a3f4aadb1cf268fc411c7b4cc99d07df3e778a.bin"}
         * honor_reply : {}
         */

        private String bvid;
        private int aid;
        private int videos;
        private int tid;
        private String tname;
        private int copyright;
        private String pic;
        private String title;
        private int pubdate;
        private int ctime;
        private String desc;
        private int state;
        private int duration;
        private RightsBean rights;
        private OwnerBean owner;
        private StatBean stat;
        private String dynamic;
        private int cid;
        private DimensionBean dimension;
        private int season_id;
        private boolean no_cache;
        private SubtitleBean subtitle;
        private UgcSeasonBean ugc_season;
        private boolean is_season_display;
        private UserGarbBean user_garb;
        private HonorReplyBean honor_reply;
        private List<DescV2Bean> desc_v2;
        private List<PagesBean> pages;

        public String getBvid() {
            return bvid;
        }

        public void setBvid(String bvid) {
            this.bvid = bvid;
        }

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public int getVideos() {
            return videos;
        }

        public void setVideos(int videos) {
            this.videos = videos;
        }

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public int getCopyright() {
            return copyright;
        }

        public void setCopyright(int copyright) {
            this.copyright = copyright;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getPubdate() {
            return pubdate;
        }

        public void setPubdate(int pubdate) {
            this.pubdate = pubdate;
        }

        public int getCtime() {
            return ctime;
        }

        public void setCtime(int ctime) {
            this.ctime = ctime;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public RightsBean getRights() {
            return rights;
        }

        public void setRights(RightsBean rights) {
            this.rights = rights;
        }

        public OwnerBean getOwner() {
            return owner;
        }

        public void setOwner(OwnerBean owner) {
            this.owner = owner;
        }

        public StatBean getStat() {
            return stat;
        }

        public void setStat(StatBean stat) {
            this.stat = stat;
        }

        public String getDynamic() {
            return dynamic;
        }

        public void setDynamic(String dynamic) {
            this.dynamic = dynamic;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public DimensionBean getDimension() {
            return dimension;
        }

        public void setDimension(DimensionBean dimension) {
            this.dimension = dimension;
        }

        public int getSeason_id() {
            return season_id;
        }

        public void setSeason_id(int season_id) {
            this.season_id = season_id;
        }

        public boolean isNo_cache() {
            return no_cache;
        }

        public void setNo_cache(boolean no_cache) {
            this.no_cache = no_cache;
        }

        public SubtitleBean getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(SubtitleBean subtitle) {
            this.subtitle = subtitle;
        }

        public UgcSeasonBean getUgc_season() {
            return ugc_season;
        }

        public void setUgc_season(UgcSeasonBean ugc_season) {
            this.ugc_season = ugc_season;
        }

        public boolean isIs_season_display() {
            return is_season_display;
        }

        public void setIs_season_display(boolean is_season_display) {
            this.is_season_display = is_season_display;
        }

        public UserGarbBean getUser_garb() {
            return user_garb;
        }

        public void setUser_garb(UserGarbBean user_garb) {
            this.user_garb = user_garb;
        }

        public HonorReplyBean getHonor_reply() {
            return honor_reply;
        }

        public void setHonor_reply(HonorReplyBean honor_reply) {
            this.honor_reply = honor_reply;
        }

        public List<DescV2Bean> getDesc_v2() {
            return desc_v2;
        }

        public void setDesc_v2(List<DescV2Bean> desc_v2) {
            this.desc_v2 = desc_v2;
        }

        public List<PagesBean> getPages() {
            return pages;
        }

        public void setPages(List<PagesBean> pages) {
            this.pages = pages;
        }

        public static class RightsBean {
            /**
             * bp : 0
             * elec : 0
             * download : 1
             * movie : 0
             * pay : 0
             * hd5 : 0
             * no_reprint : 1
             * autoplay : 1
             * ugc_pay : 0
             * is_cooperation : 0
             * ugc_pay_preview : 0
             * no_background : 0
             * clean_mode : 0
             * is_stein_gate : 0
             * is_360 : 0
             * no_share : 0
             */

            private int bp;
            private int elec;
            private int download;
            private int movie;
            private int pay;
            private int hd5;
            private int no_reprint;
            private int autoplay;
            private int ugc_pay;
            private int is_cooperation;
            private int ugc_pay_preview;
            private int no_background;
            private int clean_mode;
            private int is_stein_gate;
            private int is_360;
            private int no_share;

            public int getBp() {
                return bp;
            }

            public void setBp(int bp) {
                this.bp = bp;
            }

            public int getElec() {
                return elec;
            }

            public void setElec(int elec) {
                this.elec = elec;
            }

            public int getDownload() {
                return download;
            }

            public void setDownload(int download) {
                this.download = download;
            }

            public int getMovie() {
                return movie;
            }

            public void setMovie(int movie) {
                this.movie = movie;
            }

            public int getPay() {
                return pay;
            }

            public void setPay(int pay) {
                this.pay = pay;
            }

            public int getHd5() {
                return hd5;
            }

            public void setHd5(int hd5) {
                this.hd5 = hd5;
            }

            public int getNo_reprint() {
                return no_reprint;
            }

            public void setNo_reprint(int no_reprint) {
                this.no_reprint = no_reprint;
            }

            public int getAutoplay() {
                return autoplay;
            }

            public void setAutoplay(int autoplay) {
                this.autoplay = autoplay;
            }

            public int getUgc_pay() {
                return ugc_pay;
            }

            public void setUgc_pay(int ugc_pay) {
                this.ugc_pay = ugc_pay;
            }

            public int getIs_cooperation() {
                return is_cooperation;
            }

            public void setIs_cooperation(int is_cooperation) {
                this.is_cooperation = is_cooperation;
            }

            public int getUgc_pay_preview() {
                return ugc_pay_preview;
            }

            public void setUgc_pay_preview(int ugc_pay_preview) {
                this.ugc_pay_preview = ugc_pay_preview;
            }

            public int getNo_background() {
                return no_background;
            }

            public void setNo_background(int no_background) {
                this.no_background = no_background;
            }

            public int getClean_mode() {
                return clean_mode;
            }

            public void setClean_mode(int clean_mode) {
                this.clean_mode = clean_mode;
            }

            public int getIs_stein_gate() {
                return is_stein_gate;
            }

            public void setIs_stein_gate(int is_stein_gate) {
                this.is_stein_gate = is_stein_gate;
            }

            public int getIs_360() {
                return is_360;
            }

            public void setIs_360(int is_360) {
                this.is_360 = is_360;
            }

            public int getNo_share() {
                return no_share;
            }

            public void setNo_share(int no_share) {
                this.no_share = no_share;
            }
        }

        public static class OwnerBean {
            /**
             * mid : 13046
             * name : 少年Pi
             * face : http://i2.hdslb.com/bfs/face/6e3b84c1fe71caf523ed87d264f9026013af1c2c.jpg
             */

            private int mid;
            private String name;
            private String face;

            public int getMid() {
                return mid;
            }

            public void setMid(int mid) {
                this.mid = mid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getFace() {
                return face;
            }

            public void setFace(String face) {
                this.face = face;
            }
        }

        public static class StatBean {
            /**
             * aid : 466728127
             * view : 30249
             * danmaku : 1109
             * reply : 277
             * favorite : 955
             * coin : 2661
             * share : 50
             * now_rank : 0
             * his_rank : 0
             * like : 3501
             * dislike : 0
             * evaluation :
             * argue_msg :
             */

            private int aid;
            private int view;
            private int danmaku;
            private int reply;
            private int favorite;
            private int coin;
            private int share;
            private int now_rank;
            private int his_rank;
            private int like;
            private int dislike;
            private String evaluation;
            private String argue_msg;

            public int getAid() {
                return aid;
            }

            public void setAid(int aid) {
                this.aid = aid;
            }

            public int getView() {
                return view;
            }

            public void setView(int view) {
                this.view = view;
            }

            public int getDanmaku() {
                return danmaku;
            }

            public void setDanmaku(int danmaku) {
                this.danmaku = danmaku;
            }

            public int getReply() {
                return reply;
            }

            public void setReply(int reply) {
                this.reply = reply;
            }

            public int getFavorite() {
                return favorite;
            }

            public void setFavorite(int favorite) {
                this.favorite = favorite;
            }

            public int getCoin() {
                return coin;
            }

            public void setCoin(int coin) {
                this.coin = coin;
            }

            public int getShare() {
                return share;
            }

            public void setShare(int share) {
                this.share = share;
            }

            public int getNow_rank() {
                return now_rank;
            }

            public void setNow_rank(int now_rank) {
                this.now_rank = now_rank;
            }

            public int getHis_rank() {
                return his_rank;
            }

            public void setHis_rank(int his_rank) {
                this.his_rank = his_rank;
            }

            public int getLike() {
                return like;
            }

            public void setLike(int like) {
                this.like = like;
            }

            public int getDislike() {
                return dislike;
            }

            public void setDislike(int dislike) {
                this.dislike = dislike;
            }

            public String getEvaluation() {
                return evaluation;
            }

            public void setEvaluation(String evaluation) {
                this.evaluation = evaluation;
            }

            public String getArgue_msg() {
                return argue_msg;
            }

            public void setArgue_msg(String argue_msg) {
                this.argue_msg = argue_msg;
            }
        }

        public static class DimensionBean {
            /**
             * width : 1920
             * height : 1080
             * rotate : 0
             */

            private int width;
            private int height;
            private int rotate;

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getRotate() {
                return rotate;
            }

            public void setRotate(int rotate) {
                this.rotate = rotate;
            }
        }

        public static class SubtitleBean {
            /**
             * allow_submit : false
             * list : []
             */

            private boolean allow_submit;
            private List<?> list;

            public boolean isAllow_submit() {
                return allow_submit;
            }

            public void setAllow_submit(boolean allow_submit) {
                this.allow_submit = allow_submit;
            }

            public List<?> getList() {
                return list;
            }

            public void setList(List<?> list) {
                this.list = list;
            }
        }

        public static class UgcSeasonBean {
            /**
             * id : 210709
             * title : P4S
             * cover : https://archive.biliimg.com/bfs/archive/3170440633159185188b7aa7fd962a21eff75c14.jpg
             * mid : 13046
             * intro :
             * sign_state : 0
             * attribute : 28
             * sections : [{"season_id":210709,"id":242855,"title":"正片","type":1,"episodes":[{"season_id":210709,"section_id":242855,"id":2497626,"aid":466728127,"cid":513557884,"title":"P4S实况P1\u2014\u2014惊愕：有为青年转学第一天遇到命案","attribute":0,"arc":{"aid":466728127,"videos":0,"type_id":0,"type_name":"","copyright":0,"pic":"http://i2.hdslb.com/bfs/archive/f5b75ce77e44af18b63175e51c4e2196b2c96a94.jpg","title":"P4S实况P1\u2014\u2014惊愕：有为青年转学第一天遇到命案","pubdate":1645402151,"ctime":1645402151,"desc":"","state":0,"duration":5802,"rights":{"bp":0,"elec":0,"download":0,"movie":0,"pay":0,"hd5":0,"no_reprint":0,"autoplay":0,"ugc_pay":0,"is_cooperation":0,"ugc_pay_preview":0},"author":{"mid":0,"name":"","face":""},"stat":{"aid":466728127,"view":30249,"danmaku":1109,"reply":277,"fav":955,"coin":2661,"share":50,"now_rank":0,"his_rank":0,"like":3501,"dislike":0,"evaluation":"","argue_msg":""},"dynamic":"","dimension":{"width":0,"height":0,"rotate":0},"desc_v2":null},"page":{"cid":513557884,"page":1,"from":"vupload","part":"2022-02-21 00-36-18","duration":5802,"vid":"","weblink":"","dimension":{"width":1920,"height":1080,"rotate":0}},"bvid":"BV1oL411K79F"}]}]
             * stat : {"season_id":210709,"view":30249,"danmaku":1109,"reply":277,"fav":955,"coin":2661,"share":50,"now_rank":0,"his_rank":0,"like":3501}
             * ep_count : 1
             * season_type : 1
             */

            private int id;
            private String title;
            private String cover;
            private int mid;
            private String intro;
            private int sign_state;
            private int attribute;
            private StatBeanX stat;
            private int ep_count;
            private int season_type;
            private List<SectionsBean> sections;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public int getMid() {
                return mid;
            }

            public void setMid(int mid) {
                this.mid = mid;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public int getSign_state() {
                return sign_state;
            }

            public void setSign_state(int sign_state) {
                this.sign_state = sign_state;
            }

            public int getAttribute() {
                return attribute;
            }

            public void setAttribute(int attribute) {
                this.attribute = attribute;
            }

            public StatBeanX getStat() {
                return stat;
            }

            public void setStat(StatBeanX stat) {
                this.stat = stat;
            }

            public int getEp_count() {
                return ep_count;
            }

            public void setEp_count(int ep_count) {
                this.ep_count = ep_count;
            }

            public int getSeason_type() {
                return season_type;
            }

            public void setSeason_type(int season_type) {
                this.season_type = season_type;
            }

            public List<SectionsBean> getSections() {
                return sections;
            }

            public void setSections(List<SectionsBean> sections) {
                this.sections = sections;
            }

            public static class StatBeanX {
                /**
                 * season_id : 210709
                 * view : 30249
                 * danmaku : 1109
                 * reply : 277
                 * fav : 955
                 * coin : 2661
                 * share : 50
                 * now_rank : 0
                 * his_rank : 0
                 * like : 3501
                 */

                private int season_id;
                private int view;
                private int danmaku;
                private int reply;
                private int fav;
                private int coin;
                private int share;
                private int now_rank;
                private int his_rank;
                private int like;

                public int getSeason_id() {
                    return season_id;
                }

                public void setSeason_id(int season_id) {
                    this.season_id = season_id;
                }

                public int getView() {
                    return view;
                }

                public void setView(int view) {
                    this.view = view;
                }

                public int getDanmaku() {
                    return danmaku;
                }

                public void setDanmaku(int danmaku) {
                    this.danmaku = danmaku;
                }

                public int getReply() {
                    return reply;
                }

                public void setReply(int reply) {
                    this.reply = reply;
                }

                public int getFav() {
                    return fav;
                }

                public void setFav(int fav) {
                    this.fav = fav;
                }

                public int getCoin() {
                    return coin;
                }

                public void setCoin(int coin) {
                    this.coin = coin;
                }

                public int getShare() {
                    return share;
                }

                public void setShare(int share) {
                    this.share = share;
                }

                public int getNow_rank() {
                    return now_rank;
                }

                public void setNow_rank(int now_rank) {
                    this.now_rank = now_rank;
                }

                public int getHis_rank() {
                    return his_rank;
                }

                public void setHis_rank(int his_rank) {
                    this.his_rank = his_rank;
                }

                public int getLike() {
                    return like;
                }

                public void setLike(int like) {
                    this.like = like;
                }
            }

            public static class SectionsBean {
                /**
                 * season_id : 210709
                 * id : 242855
                 * title : 正片
                 * type : 1
                 * episodes : [{"season_id":210709,"section_id":242855,"id":2497626,"aid":466728127,"cid":513557884,"title":"P4S实况P1\u2014\u2014惊愕：有为青年转学第一天遇到命案","attribute":0,"arc":{"aid":466728127,"videos":0,"type_id":0,"type_name":"","copyright":0,"pic":"http://i2.hdslb.com/bfs/archive/f5b75ce77e44af18b63175e51c4e2196b2c96a94.jpg","title":"P4S实况P1\u2014\u2014惊愕：有为青年转学第一天遇到命案","pubdate":1645402151,"ctime":1645402151,"desc":"","state":0,"duration":5802,"rights":{"bp":0,"elec":0,"download":0,"movie":0,"pay":0,"hd5":0,"no_reprint":0,"autoplay":0,"ugc_pay":0,"is_cooperation":0,"ugc_pay_preview":0},"author":{"mid":0,"name":"","face":""},"stat":{"aid":466728127,"view":30249,"danmaku":1109,"reply":277,"fav":955,"coin":2661,"share":50,"now_rank":0,"his_rank":0,"like":3501,"dislike":0,"evaluation":"","argue_msg":""},"dynamic":"","dimension":{"width":0,"height":0,"rotate":0},"desc_v2":null},"page":{"cid":513557884,"page":1,"from":"vupload","part":"2022-02-21 00-36-18","duration":5802,"vid":"","weblink":"","dimension":{"width":1920,"height":1080,"rotate":0}},"bvid":"BV1oL411K79F"}]
                 */

                private int season_id;
                private int id;
                private String title;
                private int type;
                private List<EpisodesBean> episodes;

                public int getSeason_id() {
                    return season_id;
                }

                public void setSeason_id(int season_id) {
                    this.season_id = season_id;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public List<EpisodesBean> getEpisodes() {
                    return episodes;
                }

                public void setEpisodes(List<EpisodesBean> episodes) {
                    this.episodes = episodes;
                }

                public static class EpisodesBean {
                    /**
                     * season_id : 210709
                     * section_id : 242855
                     * id : 2497626
                     * aid : 466728127
                     * cid : 513557884
                     * title : P4S实况P1——惊愕：有为青年转学第一天遇到命案
                     * attribute : 0
                     * arc : {"aid":466728127,"videos":0,"type_id":0,"type_name":"","copyright":0,"pic":"http://i2.hdslb.com/bfs/archive/f5b75ce77e44af18b63175e51c4e2196b2c96a94.jpg","title":"P4S实况P1\u2014\u2014惊愕：有为青年转学第一天遇到命案","pubdate":1645402151,"ctime":1645402151,"desc":"","state":0,"duration":5802,"rights":{"bp":0,"elec":0,"download":0,"movie":0,"pay":0,"hd5":0,"no_reprint":0,"autoplay":0,"ugc_pay":0,"is_cooperation":0,"ugc_pay_preview":0},"author":{"mid":0,"name":"","face":""},"stat":{"aid":466728127,"view":30249,"danmaku":1109,"reply":277,"fav":955,"coin":2661,"share":50,"now_rank":0,"his_rank":0,"like":3501,"dislike":0,"evaluation":"","argue_msg":""},"dynamic":"","dimension":{"width":0,"height":0,"rotate":0},"desc_v2":null}
                     * page : {"cid":513557884,"page":1,"from":"vupload","part":"2022-02-21 00-36-18","duration":5802,"vid":"","weblink":"","dimension":{"width":1920,"height":1080,"rotate":0}}
                     * bvid : BV1oL411K79F
                     */

                    private int season_id;
                    private int section_id;
                    private int id;
                    private int aid;
                    private int cid;
                    private String title;
                    private int attribute;
                    private ArcBean arc;
                    private PageBean page;
                    private String bvid;

                    public int getSeason_id() {
                        return season_id;
                    }

                    public void setSeason_id(int season_id) {
                        this.season_id = season_id;
                    }

                    public int getSection_id() {
                        return section_id;
                    }

                    public void setSection_id(int section_id) {
                        this.section_id = section_id;
                    }

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public int getAid() {
                        return aid;
                    }

                    public void setAid(int aid) {
                        this.aid = aid;
                    }

                    public int getCid() {
                        return cid;
                    }

                    public void setCid(int cid) {
                        this.cid = cid;
                    }

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public int getAttribute() {
                        return attribute;
                    }

                    public void setAttribute(int attribute) {
                        this.attribute = attribute;
                    }

                    public ArcBean getArc() {
                        return arc;
                    }

                    public void setArc(ArcBean arc) {
                        this.arc = arc;
                    }

                    public PageBean getPage() {
                        return page;
                    }

                    public void setPage(PageBean page) {
                        this.page = page;
                    }

                    public String getBvid() {
                        return bvid;
                    }

                    public void setBvid(String bvid) {
                        this.bvid = bvid;
                    }

                    public static class ArcBean {
                        /**
                         * aid : 466728127
                         * videos : 0
                         * type_id : 0
                         * type_name :
                         * copyright : 0
                         * pic : http://i2.hdslb.com/bfs/archive/f5b75ce77e44af18b63175e51c4e2196b2c96a94.jpg
                         * title : P4S实况P1——惊愕：有为青年转学第一天遇到命案
                         * pubdate : 1645402151
                         * ctime : 1645402151
                         * desc :
                         * state : 0
                         * duration : 5802
                         * rights : {"bp":0,"elec":0,"download":0,"movie":0,"pay":0,"hd5":0,"no_reprint":0,"autoplay":0,"ugc_pay":0,"is_cooperation":0,"ugc_pay_preview":0}
                         * author : {"mid":0,"name":"","face":""}
                         * stat : {"aid":466728127,"view":30249,"danmaku":1109,"reply":277,"fav":955,"coin":2661,"share":50,"now_rank":0,"his_rank":0,"like":3501,"dislike":0,"evaluation":"","argue_msg":""}
                         * dynamic :
                         * dimension : {"width":0,"height":0,"rotate":0}
                         * desc_v2 : null
                         */

                        private int aid;
                        private int videos;
                        private int type_id;
                        private String type_name;
                        private int copyright;
                        private String pic;
                        private String title;
                        private int pubdate;
                        private int ctime;
                        private String desc;
                        private int state;
                        private int duration;
                        private RightsBeanX rights;
                        private AuthorBean author;
                        private StatBeanXX stat;
                        private String dynamic;
                        private DimensionBeanX dimension;
                        private Object desc_v2;

                        public int getAid() {
                            return aid;
                        }

                        public void setAid(int aid) {
                            this.aid = aid;
                        }

                        public int getVideos() {
                            return videos;
                        }

                        public void setVideos(int videos) {
                            this.videos = videos;
                        }

                        public int getType_id() {
                            return type_id;
                        }

                        public void setType_id(int type_id) {
                            this.type_id = type_id;
                        }

                        public String getType_name() {
                            return type_name;
                        }

                        public void setType_name(String type_name) {
                            this.type_name = type_name;
                        }

                        public int getCopyright() {
                            return copyright;
                        }

                        public void setCopyright(int copyright) {
                            this.copyright = copyright;
                        }

                        public String getPic() {
                            return pic;
                        }

                        public void setPic(String pic) {
                            this.pic = pic;
                        }

                        public String getTitle() {
                            return title;
                        }

                        public void setTitle(String title) {
                            this.title = title;
                        }

                        public int getPubdate() {
                            return pubdate;
                        }

                        public void setPubdate(int pubdate) {
                            this.pubdate = pubdate;
                        }

                        public int getCtime() {
                            return ctime;
                        }

                        public void setCtime(int ctime) {
                            this.ctime = ctime;
                        }

                        public String getDesc() {
                            return desc;
                        }

                        public void setDesc(String desc) {
                            this.desc = desc;
                        }

                        public int getState() {
                            return state;
                        }

                        public void setState(int state) {
                            this.state = state;
                        }

                        public int getDuration() {
                            return duration;
                        }

                        public void setDuration(int duration) {
                            this.duration = duration;
                        }

                        public RightsBeanX getRights() {
                            return rights;
                        }

                        public void setRights(RightsBeanX rights) {
                            this.rights = rights;
                        }

                        public AuthorBean getAuthor() {
                            return author;
                        }

                        public void setAuthor(AuthorBean author) {
                            this.author = author;
                        }

                        public StatBeanXX getStat() {
                            return stat;
                        }

                        public void setStat(StatBeanXX stat) {
                            this.stat = stat;
                        }

                        public String getDynamic() {
                            return dynamic;
                        }

                        public void setDynamic(String dynamic) {
                            this.dynamic = dynamic;
                        }

                        public DimensionBeanX getDimension() {
                            return dimension;
                        }

                        public void setDimension(DimensionBeanX dimension) {
                            this.dimension = dimension;
                        }

                        public Object getDesc_v2() {
                            return desc_v2;
                        }

                        public void setDesc_v2(Object desc_v2) {
                            this.desc_v2 = desc_v2;
                        }

                        public static class RightsBeanX {
                            /**
                             * bp : 0
                             * elec : 0
                             * download : 0
                             * movie : 0
                             * pay : 0
                             * hd5 : 0
                             * no_reprint : 0
                             * autoplay : 0
                             * ugc_pay : 0
                             * is_cooperation : 0
                             * ugc_pay_preview : 0
                             */

                            private int bp;
                            private int elec;
                            private int download;
                            private int movie;
                            private int pay;
                            private int hd5;
                            private int no_reprint;
                            private int autoplay;
                            private int ugc_pay;
                            private int is_cooperation;
                            private int ugc_pay_preview;

                            public int getBp() {
                                return bp;
                            }

                            public void setBp(int bp) {
                                this.bp = bp;
                            }

                            public int getElec() {
                                return elec;
                            }

                            public void setElec(int elec) {
                                this.elec = elec;
                            }

                            public int getDownload() {
                                return download;
                            }

                            public void setDownload(int download) {
                                this.download = download;
                            }

                            public int getMovie() {
                                return movie;
                            }

                            public void setMovie(int movie) {
                                this.movie = movie;
                            }

                            public int getPay() {
                                return pay;
                            }

                            public void setPay(int pay) {
                                this.pay = pay;
                            }

                            public int getHd5() {
                                return hd5;
                            }

                            public void setHd5(int hd5) {
                                this.hd5 = hd5;
                            }

                            public int getNo_reprint() {
                                return no_reprint;
                            }

                            public void setNo_reprint(int no_reprint) {
                                this.no_reprint = no_reprint;
                            }

                            public int getAutoplay() {
                                return autoplay;
                            }

                            public void setAutoplay(int autoplay) {
                                this.autoplay = autoplay;
                            }

                            public int getUgc_pay() {
                                return ugc_pay;
                            }

                            public void setUgc_pay(int ugc_pay) {
                                this.ugc_pay = ugc_pay;
                            }

                            public int getIs_cooperation() {
                                return is_cooperation;
                            }

                            public void setIs_cooperation(int is_cooperation) {
                                this.is_cooperation = is_cooperation;
                            }

                            public int getUgc_pay_preview() {
                                return ugc_pay_preview;
                            }

                            public void setUgc_pay_preview(int ugc_pay_preview) {
                                this.ugc_pay_preview = ugc_pay_preview;
                            }
                        }

                        public static class AuthorBean {
                            /**
                             * mid : 0
                             * name :
                             * face :
                             */

                            private int mid;
                            private String name;
                            private String face;

                            public int getMid() {
                                return mid;
                            }

                            public void setMid(int mid) {
                                this.mid = mid;
                            }

                            public String getName() {
                                return name;
                            }

                            public void setName(String name) {
                                this.name = name;
                            }

                            public String getFace() {
                                return face;
                            }

                            public void setFace(String face) {
                                this.face = face;
                            }
                        }

                        public static class StatBeanXX {
                            /**
                             * aid : 466728127
                             * view : 30249
                             * danmaku : 1109
                             * reply : 277
                             * fav : 955
                             * coin : 2661
                             * share : 50
                             * now_rank : 0
                             * his_rank : 0
                             * like : 3501
                             * dislike : 0
                             * evaluation :
                             * argue_msg :
                             */

                            private int aid;
                            private int view;
                            private int danmaku;
                            private int reply;
                            private int fav;
                            private int coin;
                            private int share;
                            private int now_rank;
                            private int his_rank;
                            private int like;
                            private int dislike;
                            private String evaluation;
                            private String argue_msg;

                            public int getAid() {
                                return aid;
                            }

                            public void setAid(int aid) {
                                this.aid = aid;
                            }

                            public int getView() {
                                return view;
                            }

                            public void setView(int view) {
                                this.view = view;
                            }

                            public int getDanmaku() {
                                return danmaku;
                            }

                            public void setDanmaku(int danmaku) {
                                this.danmaku = danmaku;
                            }

                            public int getReply() {
                                return reply;
                            }

                            public void setReply(int reply) {
                                this.reply = reply;
                            }

                            public int getFav() {
                                return fav;
                            }

                            public void setFav(int fav) {
                                this.fav = fav;
                            }

                            public int getCoin() {
                                return coin;
                            }

                            public void setCoin(int coin) {
                                this.coin = coin;
                            }

                            public int getShare() {
                                return share;
                            }

                            public void setShare(int share) {
                                this.share = share;
                            }

                            public int getNow_rank() {
                                return now_rank;
                            }

                            public void setNow_rank(int now_rank) {
                                this.now_rank = now_rank;
                            }

                            public int getHis_rank() {
                                return his_rank;
                            }

                            public void setHis_rank(int his_rank) {
                                this.his_rank = his_rank;
                            }

                            public int getLike() {
                                return like;
                            }

                            public void setLike(int like) {
                                this.like = like;
                            }

                            public int getDislike() {
                                return dislike;
                            }

                            public void setDislike(int dislike) {
                                this.dislike = dislike;
                            }

                            public String getEvaluation() {
                                return evaluation;
                            }

                            public void setEvaluation(String evaluation) {
                                this.evaluation = evaluation;
                            }

                            public String getArgue_msg() {
                                return argue_msg;
                            }

                            public void setArgue_msg(String argue_msg) {
                                this.argue_msg = argue_msg;
                            }
                        }

                        public static class DimensionBeanX {
                            /**
                             * width : 0
                             * height : 0
                             * rotate : 0
                             */

                            private int width;
                            private int height;
                            private int rotate;

                            public int getWidth() {
                                return width;
                            }

                            public void setWidth(int width) {
                                this.width = width;
                            }

                            public int getHeight() {
                                return height;
                            }

                            public void setHeight(int height) {
                                this.height = height;
                            }

                            public int getRotate() {
                                return rotate;
                            }

                            public void setRotate(int rotate) {
                                this.rotate = rotate;
                            }
                        }
                    }

                    public static class PageBean {
                        /**
                         * cid : 513557884
                         * page : 1
                         * from : vupload
                         * part : 2022-02-21 00-36-18
                         * duration : 5802
                         * vid :
                         * weblink :
                         * dimension : {"width":1920,"height":1080,"rotate":0}
                         */

                        private int cid;
                        private int page;
                        private String from;
                        private String part;
                        private int duration;
                        private String vid;
                        private String weblink;
                        private DimensionBeanXX dimension;

                        public int getCid() {
                            return cid;
                        }

                        public void setCid(int cid) {
                            this.cid = cid;
                        }

                        public int getPage() {
                            return page;
                        }

                        public void setPage(int page) {
                            this.page = page;
                        }

                        public String getFrom() {
                            return from;
                        }

                        public void setFrom(String from) {
                            this.from = from;
                        }

                        public String getPart() {
                            return part;
                        }

                        public void setPart(String part) {
                            this.part = part;
                        }

                        public int getDuration() {
                            return duration;
                        }

                        public void setDuration(int duration) {
                            this.duration = duration;
                        }

                        public String getVid() {
                            return vid;
                        }

                        public void setVid(String vid) {
                            this.vid = vid;
                        }

                        public String getWeblink() {
                            return weblink;
                        }

                        public void setWeblink(String weblink) {
                            this.weblink = weblink;
                        }

                        public DimensionBeanXX getDimension() {
                            return dimension;
                        }

                        public void setDimension(DimensionBeanXX dimension) {
                            this.dimension = dimension;
                        }

                        public static class DimensionBeanXX {
                            /**
                             * width : 1920
                             * height : 1080
                             * rotate : 0
                             */

                            private int width;
                            private int height;
                            private int rotate;

                            public int getWidth() {
                                return width;
                            }

                            public void setWidth(int width) {
                                this.width = width;
                            }

                            public int getHeight() {
                                return height;
                            }

                            public void setHeight(int height) {
                                this.height = height;
                            }

                            public int getRotate() {
                                return rotate;
                            }

                            public void setRotate(int rotate) {
                                this.rotate = rotate;
                            }
                        }
                    }
                }
            }
        }

        public static class UserGarbBean {
        }

        public static class HonorReplyBean {
        }

        public static class DescV2Bean {
            /**
             * raw_text : 游戏名persona 4 golden
             直播留档，打算全程把主线故事看完读完，因为B站已经取消分P了，所以得用合集了
             * type : 1
             * biz_id : 0
             */

            private String raw_text;
            private int type;
            private int biz_id;

            public String getRaw_text() {
                return raw_text;
            }

            public void setRaw_text(String raw_text) {
                this.raw_text = raw_text;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getBiz_id() {
                return biz_id;
            }

            public void setBiz_id(int biz_id) {
                this.biz_id = biz_id;
            }
        }

        public static class PagesBean {
            /**
             * cid : 513557884
             * page : 1
             * from : vupload
             * part : 2022-02-21 00-36-18
             * duration : 5802
             * vid :
             * weblink :
             * dimension : {"width":1920,"height":1080,"rotate":0}
             * first_frame : http://i1.hdslb.com/bfs/storyff/n220221a2h1qa0t9px5hibtuzh9zmgoy_firsti.jpg
             */

            private int cid;
            private int page;
            private String from;
            private String part;
            private int duration;
            private String vid;
            private String weblink;
            private DimensionBeanXXX dimension;
            private String first_frame;

            public int getCid() {
                return cid;
            }

            public void setCid(int cid) {
                this.cid = cid;
            }

            public int getPage() {
                return page;
            }

            public void setPage(int page) {
                this.page = page;
            }

            public String getFrom() {
                return from;
            }

            public void setFrom(String from) {
                this.from = from;
            }

            public String getPart() {
                return part;
            }

            public void setPart(String part) {
                this.part = part;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public String getVid() {
                return vid;
            }

            public void setVid(String vid) {
                this.vid = vid;
            }

            public String getWeblink() {
                return weblink;
            }

            public void setWeblink(String weblink) {
                this.weblink = weblink;
            }

            public DimensionBeanXXX getDimension() {
                return dimension;
            }

            public void setDimension(DimensionBeanXXX dimension) {
                this.dimension = dimension;
            }

            public String getFirst_frame() {
                return first_frame;
            }

            public void setFirst_frame(String first_frame) {
                this.first_frame = first_frame;
            }

            public static class DimensionBeanXXX {
                /**
                 * width : 1920
                 * height : 1080
                 * rotate : 0
                 */

                private int width;
                private int height;
                private int rotate;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getRotate() {
                    return rotate;
                }

                public void setRotate(int rotate) {
                    this.rotate = rotate;
                }
            }
        }
    }
}
