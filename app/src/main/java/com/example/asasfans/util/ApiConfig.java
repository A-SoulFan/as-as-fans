package com.example.asasfans.util;


import java.util.List;

/**
 * @author akarinini
 * @description api配置
 */
public class ApiConfig {
    private static final String BASE_URL = "https://api.asasfans.asf.ink/v2";
    private static final String PATH_VIDEO = "/asoul-video-interface/advanced-search?";

    private int page;
    private String order;
    private String q;
    private String copyright;
    private String tname;
    private String url;

    public ApiConfig() {
        this.page = 1;
        this.order = "score";
        this.q = "";
        this.tname = "";
        this.copyright = "";
    }
    public ApiConfig(String order, int page, String q, String copyright, String tname) {
        this.page = page;
        this.order = order;
        this.q = q;
        this.copyright = copyright;
        this.tname = tname;
    }

    public String getUrl(){
        url = BASE_URL + PATH_VIDEO
                + "order=" + order
                + "&q=" + q
                + "&copyright=" + copyright
                + "&tname=" + tname
                + "&page=" + page
        ;
        return url;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public void pageSelfAdd(){
        this.page++;
    }

    public void pageSelfDecrement(){
        this.page--;
    }

    public ApiConfig fromString(String url){
        this.page = Integer.valueOf(getOneParameter(url, "page"));
        this.order = getOneParameter(url, "order");
        this.q = getOneParameter(url, "q");
        this.copyright = getOneParameter(url, "copyright");
        this.tname = getOneParameter(url, "tname");

        return this;
    }

    public static String listToString(List<String> list, String s) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + s);
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    public static String getOneParameter(String url,String keyWord) {
        String retValue = "";
        try {

            final String charset = "utf-8";
//            url = URLDecoder.decode(url, charset);

            if (url.indexOf('?') != -1) {
                final String contents = url.substring(url.indexOf('?') + 1);
                String[] keyValues = contents.split("&");
                for (int i = 0; i < keyValues.length; i++) {
                    String key = keyValues[i].substring(0, keyValues[i].indexOf("="));
                    String value = keyValues[i].substring(keyValues[i].indexOf("=") + 1);
                    if (key.equals(keyWord)) {
                        if (value != null || !"".equals(value.trim())) {
                            retValue = value;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retValue;
    }
}
