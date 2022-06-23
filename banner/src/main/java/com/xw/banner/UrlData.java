package com.xw.banner;

public class UrlData {

    private String url;
    private int type = 0;//0 图片 ; 1 视频

    public UrlData(String url, int type) {
        this.url = url;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
