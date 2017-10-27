package com.lbl.codek3demo.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.lbl.codek3demo.photo.bean.MediaModeVideo;
import com.lbl.codek3demo.photo.bean.Photo;
import com.lbl.codek3demo.photo.bean.VideoPhotoFloder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BLPhotoUtils {

    /**
     * 获取本地图片分类文件夹信息
     *
     * @author libilang
     */
    public static Map<String, VideoPhotoFloder> getPhotos(Context context) {
        Map<String, VideoPhotoFloder> floderMap = new HashMap<String, VideoPhotoFloder>();

        String allPhotosKey = "相机胶卷";
        VideoPhotoFloder allFloder = new VideoPhotoFloder();
        allFloder.setName(allPhotosKey);
        allFloder.setDirPath(allPhotosKey);
        allFloder.setPhotoList(new ArrayList<Photo>());
        floderMap.put(allPhotosKey, allFloder);

        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();
        // 只查询jpeg和png的图片
        Cursor mCursor = mContentResolver.query(imageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED + " desc");

        while (mCursor.moveToNext()) {
            // 获取图片的路径
            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            // 获取该图片的父路径名
            File parentFile = new File(path).getParentFile();
            if (parentFile == null) {
                continue;
            }
            String dirPath = parentFile.getAbsolutePath();
            if (floderMap.containsKey(dirPath)) {
                Photo photo = new Photo(path);
                //------添加Uri
                Uri uri = Uri.fromFile(new File(path));
                photo.filrUrlpath = path;
                //-------------
                VideoPhotoFloder videoPhotoFloder = floderMap.get(dirPath);
                videoPhotoFloder.getPhotoList().add(photo);
                floderMap.get(allPhotosKey).getPhotoList().add(photo);
                continue;
            } else {
                // 初始化imageFloder
                VideoPhotoFloder videoPhotoFloder = new VideoPhotoFloder();
                List<Photo> photoList = new ArrayList<>();
                Photo photo = new Photo(path);
                photoList.add(photo);
                videoPhotoFloder.setPhotoList(photoList);
                videoPhotoFloder.setDirPath(dirPath);
                videoPhotoFloder.setName(dirPath.substring(dirPath.lastIndexOf(File.separator) + 1, dirPath.length()));
                floderMap.put(dirPath, videoPhotoFloder);
                floderMap.get(allPhotosKey).getPhotoList().add(photo);
            }
        }
        mCursor.close();
        return floderMap;
    }


    /**
     * 获取手机视频
     * @author libilang
     */
    public static Map<String, VideoPhotoFloder> getVideo(Context context) {
        Uri MEDIA_EXTERNAL_CONTENT_URI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Map<String, VideoPhotoFloder> floderMap = new HashMap<String, VideoPhotoFloder>();

        String allPhotosKey = "相机胶卷";
        VideoPhotoFloder allFloder = new VideoPhotoFloder();
        allFloder.setName(allPhotosKey);
        allFloder.setDirPath(allPhotosKey);
        allFloder.setMediaModeVideolist(new ArrayList<MediaModeVideo>());
        floderMap.put(allPhotosKey, allFloder);

        final String orderBy = MediaStore.Video.Media.DATE_TAKEN;

        String[] proj = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DURATION};

        Cursor cursor = context.getContentResolver().query(MEDIA_EXTERNAL_CONTENT_URI,
                proj, null, null, orderBy + " DESC");
        Log.e("tag", "cursor connt:" + cursor.getCount());
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String url = cursor.getString(1);
                if (url.toLowerCase().endsWith(".mp4") && !url.contains("cache/")) {
                    String name = cursor.getString(2);
                    int duration = cursor.getInt(3);

                    File parentFile = new File(url).getParentFile();
                    if (parentFile == null) {
                        continue;
                    }
                    String dirPath = parentFile.getAbsolutePath();
                    if (floderMap.containsKey(dirPath)) {
                        MediaModeVideo mediaModeVideo = new MediaModeVideo(url, duration,
                                name);
                        VideoPhotoFloder videoPhotoFloder = floderMap.get(dirPath);
                        videoPhotoFloder.getMediaModeVideolist().add(mediaModeVideo);
                        File file = new File(url);
                        if (file.exists())
                            floderMap.get(allPhotosKey).getMediaModeVideolist().add(mediaModeVideo);
                    } else {
                        VideoPhotoFloder videoPhotoFloder = new VideoPhotoFloder();
                        ArrayList<MediaModeVideo> mGalleryModelList = new ArrayList<MediaModeVideo>();
                        MediaModeVideo mediaModeVideo = new MediaModeVideo(url, duration,
                                name);
                        mGalleryModelList.add(mediaModeVideo);
                        videoPhotoFloder.setMediaModeVideolist(mGalleryModelList);
                        videoPhotoFloder.setDirPath(dirPath);
                        videoPhotoFloder.setName(dirPath.substring(
                                dirPath.lastIndexOf(File.separator) + 1,
                                dirPath.length()));
                        floderMap.put(dirPath, videoPhotoFloder);
                        floderMap.get(allPhotosKey).getMediaModeVideolist().add(mediaModeVideo);
                    }
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        return floderMap;
    }

}
