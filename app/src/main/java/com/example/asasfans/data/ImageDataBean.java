package com.example.asasfans.data;

import java.util.List;

/**
 * @author: akari
 * @date: 2022/3/8
 * @description $
 */
public class ImageDataBean {

    /**
     * dy_id : 634858202928775172
     * pic_url : [{"img_src":"https://i0.hdslb.com/bfs/album/c3fb2e2b34d752803ee9f7115fa5e9ad5d9a2f91.jpg","img_size":790,"img_tags":null,"img_width":1080,"img_height":1920},{"img_src":"https://i0.hdslb.com/bfs/album/c6b4493317ab00bd346ea12efae151445b44496b.jpg","img_size":947.6699829101562,"img_tags":null,"img_width":1080,"img_height":1920}]
     * uid : 33631964
     * name : Diana牌白颜料
     * face : http://i2.hdslb.com/bfs/face/adb8e03a32cd3f88fe2a2555065b545750c081ed.jpg
     */

    private String dy_id;
    private long uid;
    private String name;
    private String face;
    private List<PicUrlBean> pic_url;

    public String getDy_id() {
        return dy_id;
    }

    public void setDy_id(String dy_id) {
        this.dy_id = dy_id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
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

    public List<PicUrlBean> getPic_url() {
        return pic_url;
    }

    public void setPic_url(List<PicUrlBean> pic_url) {
        this.pic_url = pic_url;
    }

    public static class PicUrlBean {
        /**
         * img_src : https://i0.hdslb.com/bfs/album/c3fb2e2b34d752803ee9f7115fa5e9ad5d9a2f91.jpg
         * img_size : 790
         * img_tags : null
         * img_width : 1080
         * img_height : 1920
         */

        private String img_src;
        private double img_size;
        private Object img_tags;
        private double img_width;
        private double img_height;

        public String getImg_src() {
            return img_src;
        }

        public void setImg_src(String img_src) {
            this.img_src = img_src;
        }

        public double getImg_size() {
            return img_size;
        }

        public void setImg_size(double img_size) {
            this.img_size = img_size;
        }

        public Object getImg_tags() {
            return img_tags;
        }

        public void setImg_tags(Object img_tags) {
            this.img_tags = img_tags;
        }

        public double getImg_width() {
            return img_width;
        }

        public void setImg_width(double img_width) {
            this.img_width = img_width;
        }

        public double getImg_height() {
            return img_height;
        }

        public void setImg_height(double img_height) {
            this.img_height = img_height;
        }
    }
}
