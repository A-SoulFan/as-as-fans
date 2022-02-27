package com.example.asasfans.ui.video;

import java.util.List;

/**
 * @author akarinini
 * @description Gson根据json生成的数据类，20个视频的bvid
 */

public class PubdateVideoBean {
    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"page":1,"numResults":20,"result":[["BV1FS4y1k7PM"],["BV1fa411k7Pj"],["BV1LF411t7vf"],["BV1Qb4y147eh"],["BV1r44y1J7g4"],["BV11T4y1D74G"],["BV1KZ4y1k762"],["BV11R4y1L7Q1"],["BV1oq4y1t7Z4"],["BV1cR4y1j7ed"],["BV1s44y1n76H"],["BV1XF411E7qv"],["BV12q4y1t7pE"],["BV1Db4y1475g"],["BV13u411X7B3"],["BV1Hm4y1o7av"],["BV1Hm4y1o75Y"],["BV1DT4y1D7sr"],["BV1Xi4y117P2"],["BV1FT4y1Q7GA"]]}
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
         * page : 1
         * numResults : 20
         * result : [["BV1FS4y1k7PM"],["BV1fa411k7Pj"],["BV1LF411t7vf"],["BV1Qb4y147eh"],["BV1r44y1J7g4"],["BV11T4y1D74G"],["BV1KZ4y1k762"],["BV11R4y1L7Q1"],["BV1oq4y1t7Z4"],["BV1cR4y1j7ed"],["BV1s44y1n76H"],["BV1XF411E7qv"],["BV12q4y1t7pE"],["BV1Db4y1475g"],["BV13u411X7B3"],["BV1Hm4y1o7av"],["BV1Hm4y1o75Y"],["BV1DT4y1D7sr"],["BV1Xi4y117P2"],["BV1FT4y1Q7GA"]]
         */

        private int page;
        private int numResults;
        private List<List<String>> result;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getNumResults() {
            return numResults;
        }

        public void setNumResults(int numResults) {
            this.numResults = numResults;
        }

        public List<List<String>> getResult() {
            return result;
        }

        public void setResult(List<List<String>> result) {
            this.result = result;
        }
    }
}
