package com.lbl.codek3demo.video;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.lbl.codek3demo.photo.bean.MediaModeVideo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author：libilang
 * time: 17/7/27 2:01
 * 邮箱：libi_lang@163.com
 * 异步读取视频第一帧图片缓存
 */
public class BLLoadVideoPhotoAsync extends AsyncTask<String, String, String> {

    public Context activity;
    private ImageView mImageView;
    public static BLGalleryCache mCache;
    private boolean mIsScrolling;
    private int mWidth;
    private int mType;

    private MediaModeVideo videoentity;
    public final static ExecutorService es = Executors.newFixedThreadPool(10);//最多一次执行10个线程


    public BLLoadVideoPhotoAsync(Activity activity, MediaModeVideo mediaMode_video, ImageView imageView,
                                 boolean isScrolling, int width, int type) {
        mImageView = imageView;
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.activity = activity;
        mWidth = width;
        mIsScrolling = isScrolling;
        mType = type;
        this.videoentity = mediaMode_video;
        final int memClass = ((ActivityManager) activity
                .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        final int size = 1024 * 1024 * memClass / 8;
        BLGalleryRetainCache c = BLGalleryRetainCache.getOrCreateRetainableCache();
        mCache = c.mRetainedCache;

        if (mCache == null) {
            mCache = new BLGalleryCache(size, mWidth, mWidth);
            c.mRetainedCache = mCache;
        }
    }
    @Override
    protected String doInBackground(String... params) {
        String url;
        url = params[0].toString();
        return url;
    }
    @Override
    protected void onPostExecute(String result) {
        mCache.BLLoadVideoPhotoBitmap(activity, videoentity, result, mImageView, mIsScrolling,
                mType);
    }

}
