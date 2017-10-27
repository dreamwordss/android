package com.lbl.codek3demo.photo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lbl.codek3demo.R;
import com.lbl.codek3demo.photo.adapter.BLPhotoAdapter;
import com.lbl.codek3demo.photo.bean.Photo;
import com.lbl.codek3demo.photo.bean.VideoPhotoFloder;
import com.lbl.codek3demo.util.BLPhotoUtils;
import com.lbl.codek3demo.util.Util;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * author：libilang
 * time: 17/7/24 15:15
 * 邮箱：libi_lang@163.com
 */

public class BLPhotoActivity extends Activity implements View.OnClickListener {
    public final static String ALL_PHOTO = "相机胶卷";
    private Map<String, VideoPhotoFloder> mFloderMap;
    private GridView mGridView;
    private BLPhotoAdapter mPhotoAdapter;
    public List<Photo> mPhotoLists = new ArrayList<Photo>();
    private Intent intent;
    private int width = 0;
    private int height;
    //晒带的数据集合 和最大张数
    private ArrayList<String> photoList = new ArrayList();
    public ImageView photo_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blphoto_picker);
        initView();
        getPhotoData();
    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.photo_gridview);
        width = Util.getScreenWidth(this);
        photo_iv = (ImageView) findViewById(R.id.photo_iv);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, width);
        photo_iv.setLayoutParams(lp);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
    }

    private void getPhotoData() {
        mFloderMap = BLPhotoUtils.getPhotos(this
                .getApplicationContext());
        VideoPhotoFloder videoPhotoFloder = mFloderMap.get(ALL_PHOTO);
        if (videoPhotoFloder != null) {
            mPhotoLists.addAll(videoPhotoFloder.getPhotoList());
        }
        mPhotoAdapter = new BLPhotoAdapter(this);
        mPhotoAdapter.setIonItemChanged(new BLPhotoAdapter.IonItemChanged() {
            @Override
            public void changeBigImage(int position, boolean choose, String uri) {
                //暂时使用imageloader加载图片
                setLast_path(position, choose, uri);
            }
        });

        mPhotoAdapter.setDatas(mPhotoLists);
        if (mPhotoLists.size() > 0) {
            mGridView.setAdapter(mPhotoAdapter);
            Set<String> keys = mFloderMap.keySet();
            final List<VideoPhotoFloder> floders = new ArrayList<VideoPhotoFloder>();
            for (String key : keys) {
                if (ALL_PHOTO.equals(key)) {
                    VideoPhotoFloder floder = mFloderMap.get(key);
                    floder.setIsSelected(true);
                    floders.add(0, floder);
                } else {
                    floders.add(mFloderMap.get(key));
                }
            }
            String path = null;
            if (floders.size() > 0 && floders.get(0).getPhotoList().size() > 0) {
                path = floders.get(0).getPhotoList().get(0).getPath();
                File file = new File(path);
                if (file.exists() && file.isFile()) {
                    Picasso.with(BLPhotoActivity.this).load("file://" + path).into(photo_iv);
                }
            }
        }
    }

    //选择上的条数
    public void setLast_path(int position, boolean choose, String last_path) {
        if (choose) {
            photoList.add(last_path);
            Picasso.with(BLPhotoActivity.this).load("file://" + last_path).into(photo_iv);
        } else {
            photoList.remove(last_path);
        }
    }

    @Override
    public void onClick(View view) {

    }
}
