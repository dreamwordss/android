package com.lbl.codek3demo.photo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 视频 图片文件夹对象
 *
 * @author libilang
 */
public class VideoPhotoFloder implements Serializable {

    private static final long serialVersionUID = 1L;
    /* 文件夹名 */
    private String name;
    /* 文件夹路径 */
    private String dirPath;
    /* 该文件夹下图片列表 */
    private List<Photo> photoList;
    /* 该文件夹下视频列表*/
    private List<MediaModeVideo> mediaModeVideolist;
    /* 标识是否选中该文件夹 */
    private boolean isSelected;


    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }

    public List<MediaModeVideo> getMediaModeVideolist() {
        return mediaModeVideolist;
    }


    public void setMediaModeVideolist(List<MediaModeVideo> mediaModeVideolist) {
        this.mediaModeVideolist = mediaModeVideolist;
    }

}
