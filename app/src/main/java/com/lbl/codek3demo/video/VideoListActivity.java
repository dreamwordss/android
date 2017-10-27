package com.lbl.codek3demo.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lbl.codek3demo.R;

import java.util.ArrayList;

/**
 * author：libilang
 * time: 17/7/27 18:57
 * 邮箱：libi_lang@163.com
 */

public class VideoListActivity extends Activity {
    public static final String PATHS = "video_path";
    private ArrayList<String> videoUrls = new ArrayList<>();
    LinearLayout video_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listvideo);
        initArgs();
        initView();
        initDada();
    }

    private void initView() {
        video_layout = (LinearLayout) findViewById(R.id.video_layout);
    }

    private void initArgs() {
        Intent intent = getIntent();
        videoUrls = intent.getStringArrayListExtra(PATHS);
    }

    private void initDada() {
        for (int i = 0; i < videoUrls.size(); i++) {
            VideoItem videoItem = new VideoItem(this);
            ImageView videoImage = (ImageView) videoItem.findViewById(R.id.videoimage);
            videoImage.setImageBitmap(BLLoadVideoPhotoAsync.mCache.getBitmapFromCache(videoUrls.get(i)));
            video_layout.addView(videoItem);
        }
    }
}
