package com.lbl.codek3demo.photo.bean;

import java.io.Serializable;

public class Photo implements Serializable {

    /**
     *
     */
    public String filrUrlpath;
    private static final long serialVersionUID = 1L;
    private int id;
    private String path; // 路径
    private boolean isSelected = false;
    public String number = "";

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Photo(String path) {
        this.path = path;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
