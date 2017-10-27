package com.lbl.codek3demo.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.v4.util.LruCache;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lbl.codek3demo.photo.bean.MediaModeVideo;

import java.util.ArrayList;

/**
 * author：libilang
 * time: 17/7/27 6:03
 * 邮箱：libi_lang@163.com
 * 缓存类
 */
public class BLGalleryCache {
    private LruCache<String, Bitmap> mBitmapCache;
    private ArrayList<String> mCurrentTasks;
    private int mMaxWidth;

    public BLGalleryCache(int size, int maxWidth, int maxHeight) {
        mMaxWidth = maxWidth;
        mBitmapCache = new LruCache<String, Bitmap>(size) {
            @Override
            protected int sizeOf(String key, Bitmap b) {
                return b.getHeight() * b.getWidth() * 4;
            }
        };
        mCurrentTasks = new ArrayList<String>();
    }

    private void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            mBitmapCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromCache(String key) {
        return mBitmapCache.get(key);
    }

    /**
     * Gets a bitmap from cache. <br/>
     * <br/>
     * If it is not in cache, this method will: <br/>
     * <b>1:</b> check if the bitmap url is currently being processed in the
     * BLBitmapLoaderTask and cancel if it is already in a task (a control to see
     * if it's inside the currentTasks list). <br/>
     * <b>2:</b> check if an internet connection is available and continue if
     * so. <br/>
     * <b>3:</b> download the bitmap, scale the bitmap if necessary and put it
     * into the memory cache. <br/>
     * <b>4:</b> Remove the bitmap url from the currentTasks list. <br/>
     * <b>5:</b> Notify the ListAdapter.
     *
     * @param -           Reference to activity object, in order to call
     *                    notifyDataSetChanged() on the ListAdapter.
     * @param imageKey    - The bitmap url (will be the key).
     * @param imageView   - The ImageView that should get an available bitmap or a
     *                    placeholder image.
     * @param isScrolling - If set to true, we skip executing more tasks since the user
     *                    probably has flinged away the view.
     */
    public void BLLoadVideoPhotoBitmap(Context activity, MediaModeVideo videoentity, String imageKey,
                                       ImageView imageView, boolean isScrolling, int type) {
        if (type == 0) {
            final Bitmap bitmap = getBitmapFromCache(imageKey);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                if (!isScrolling && !mCurrentTasks.contains(imageKey)) {
                    BLBitmapLoaderTask task = null;
                    if (activity instanceof BLVideoActivity) {
                        BLVideoActivity chooseActivity = (BLVideoActivity) activity;
                        task = new BLBitmapLoaderTask(imageKey,
                                chooseActivity.getAdapter());
                    }
                    task.execute();
                }
            }
        } else if (type == 1) {
            if (!isScrolling) {
                BLBitmapLoaderCropTask task = null;
                task = new BLBitmapLoaderCropTask(imageKey,
                        imageView);
                task.execute();
            }
        }
    }

    public void loadPhotoBitmap(Context activity, String imageKey,
                                ImageView imageView, boolean isScrolling, int type) {
        if (type == 0) {
            final Bitmap bitmap = getBitmapFromCache(imageKey);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                if (!isScrolling && !mCurrentTasks.contains(imageKey)) {
                    BLBitmapLoaderTask task = null;
//                    task = new BLBitmapLoaderTask(imageKey,
//                            activity.getAdapter());
                    task.execute();
                }
            }
        } else if (type == 1) {
            if (!isScrolling) {
                BLBitmapLoaderCropTask task = null;
                task = new BLBitmapLoaderCropTask(imageKey,
                        imageView);
                task.execute();
            }
        }

    }

    private class BLBitmapLoaderTask extends AsyncTask<Void, Void, Bitmap> {
        private BaseAdapter mAdapter;
        private String mImageKey;

        public BLBitmapLoaderTask(String imageKey, BaseAdapter adapter) {
            mAdapter = adapter;
            mImageKey = imageKey;
        }

        @Override
        protected void onPreExecute() {
            mCurrentTasks.add(mImageKey);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = null;
            try {
                bitmap = ThumbnailUtils.createVideoThumbnail(mImageKey,
                        Thumbnails.FULL_SCREEN_KIND);

                if (bitmap != null) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, mMaxWidth,
                            mMaxWidth, false);
                    addBitmapToCache(mImageKey, bitmap);
                    return bitmap;
                }
                return null;
            } catch (Exception e) {
                if (e != null) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap param) {
            mCurrentTasks.remove(mImageKey);
            if (param != null) {
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void clear() {
        mBitmapCache.evictAll();
    }

    private class BLBitmapLoaderCropTask extends AsyncTask<Void, Void, Bitmap> {
        private ImageView mImageView;
        private String mImageKey;

        public BLBitmapLoaderCropTask(String imageKey, ImageView imageView) {
            mImageView = imageView;
            mImageKey = imageKey;
        }
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap bitmap = null;
            try {
                bitmap = ThumbnailUtils.createVideoThumbnail(mImageKey,
                        Thumbnails.FULL_SCREEN_KIND);
                if (bitmap != null) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, mMaxWidth,
                            mMaxWidth, false);
                    return bitmap;
                }
                return null;
            } catch (Exception e) {
                if (e != null) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap param) {
            if (param != null) {
                mImageView.setImageBitmap(param);
            }
        }
    }

}
