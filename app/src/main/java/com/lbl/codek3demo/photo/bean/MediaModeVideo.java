package com.lbl.codek3demo.photo.bean;

import java.io.Serializable;

public class MediaModeVideo implements Serializable {

    private String url;
    private int time;
    private String name;
    private boolean isClicked;

    public MediaModeVideo(String url, int time, String name) {
        this.url = url;
        this.time = time;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setIsClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }
}
