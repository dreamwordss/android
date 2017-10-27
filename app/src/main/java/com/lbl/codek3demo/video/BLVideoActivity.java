package com.lbl.codek3demo.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.lbl.codek3demo.R;
import com.lbl.codek3demo.photo.bean.MediaModeVideo;
import com.lbl.codek3demo.photo.bean.VideoPhotoFloder;
import com.lbl.codek3demo.util.BLPhotoUtils;
import com.lbl.codek3demo.util.Util;
import com.lbl.codek3demo.video.adapter.BLVideoAdapter;

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

public class BLVideoActivity extends Activity implements View.OnClickListener {
    public final static String ALL_PHOTO = "相机胶卷";
    public static String FROMWHERE = "fromewhere";
    public final static String TO_VIDEO_INT = "PHOTO_TO_VIDEO";
    public static final int RECORD_REQUEST = 100;
    private Map<String, VideoPhotoFloder> mFloderMap;
    private GridView mGridView;//上面的gridView
    private BLVideoAdapter mVideoAdapter;//适配器
    public List<MediaModeVideo> mediaMode_videos = new ArrayList<MediaModeVideo>();//所以视频的合集
    private TextView tvOK;
    public  List<MediaModeVideo> videoUrls = new ArrayList<>();//视频地址集合
    public static int screenWidth;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_choose);
        initView();
        getVideos();
    }

    public BLVideoAdapter getAdapter() {
        if (mVideoAdapter != null) {
            return mVideoAdapter;
        }
        return null;
    }

    private void initView() {
        Intent intent = getIntent();
        mGridView = (GridView) findViewById(R.id.video_gridview);
        screenWidth = Util.getScreenWidth(this);
        tvOK = (TextView) findViewById(R.id.btn_next);
        tvOK.setOnClickListener(this);
    }

    //获取video视频数据
    private void getVideos() {
        mFloderMap = BLPhotoUtils.getVideo(BLVideoActivity.this
                .getApplicationContext());
        mediaMode_videos.addAll(mFloderMap.get(ALL_PHOTO).getMediaModeVideolist());
        if (mediaMode_videos.size() > 0) {
            mVideoAdapter = new BLVideoAdapter(BLVideoActivity.this);
            mVideoAdapter.setDatas(mediaMode_videos);
            mGridView.setAdapter(mVideoAdapter);
            mVideoAdapter.setActivity(BLVideoActivity.this);
            Set<String> keys = mFloderMap.keySet();
            final List<VideoPhotoFloder> floders = new ArrayList<>();
            for (String key : keys) {
                if (ALL_PHOTO.equals(key)) {
                    VideoPhotoFloder floder = mFloderMap.get(key);
                    floder.setIsSelected(true);
                    floders.add(0, floder);
                } else {
                    floders.add(mFloderMap.get(key));
                }
            }
            initListener();//避免没有视频崩溃
        }
    }

    private void initListener() {
        mVideoAdapter.setItemCheckedListener(new BLVideoAdapter.itemCheckedListener() {

            @Override
            public void itemCheckedListener(int position, boolean chooses) {
//                if (position == 0) {
//                    intent = new Intent();
//                    intent.setClass(BLVideoActivity.this, PhotoCarmeraActivity.class);
//                    startActivityForResult(intent, RECORD_REQUEST);
//                } else {
//                    //现在是直接把多段视频拿到然后判断系统多少 去到视频剪辑界面
//                }
                addVideoUrl(position, chooses);
            }

            @Override
            public void simpleLookListener(int position, String url, boolean isCheck) {

            }
        });
    }

    private void addVideoUrl(int position, boolean chooses) {
        if (chooses) {
            //添加
            File file = new File(mediaMode_videos.get(position).getUrl());
            if (!file.exists()) {
                Log.e("bilang", "请检查视频是否已经被删除");
                return;
            }
            for (int i = 0; i < videoUrls.size(); i++) {
                MediaModeVideo video = videoUrls.get(i);
                if (video.getUrl().equals(mediaMode_videos.get(position).getUrl())) {
                    return;
                }
            }
            videoUrls.add(mediaMode_videos.get(position));
        } else {
            for (int i = 0; i < videoUrls.size(); i++) {
                MediaModeVideo video = videoUrls.get(i);
                if (video.getUrl().equals(mediaMode_videos.get(position).getUrl())) {
                    videoUrls.remove(video);
                    break;
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                gotoVideoList();
                break;
        }
    }

    ArrayList<String> videourlLists;

    private void gotoVideoList() {
        if (videoUrls.size() > 0) {
            videourlLists = new ArrayList<String>();
            for (int i = 0; i < videoUrls.size(); i++) {
                MediaModeVideo entity = videoUrls.get(i);
                videourlLists.add(entity.getUrl());
            }
            Intent intent = new Intent();
            intent.setClass(BLVideoActivity.this, VideoEditActivity.class);
            intent.putStringArrayListExtra(VideoListActivity.PATHS, videourlLists);
            startActivity(intent);
        }
    }
}
