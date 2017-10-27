package com.lbl.codek3demo.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lbl.codek3demo.R;

import java.util.ArrayList;

import static com.lbl.codek3demo.video.VideoListActivity.PATHS;

/**
 * author：libilang
 * time: 17/7/27 18:57
 * 邮箱：libi_lang@163.com
 */

public class VideoEditActivity extends Activity {
    private ArrayList<String> videoUrls = new ArrayList<>();
    TextView next_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editvideo);
        initArgs();
        initView();
    }

    private void initView() {
        next_activity = (TextView) findViewById(R.id.next_activity);
        next_activity.setText("第1层 \n点击进去下一层");

        next_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(VideoEditActivity.this, VideoEditTwoActivity.class);
                intent.putStringArrayListExtra(VideoListActivity.PATHS, videoUrls);
                startActivity(intent);
            }
        });
    }

    private void initArgs() {
        Intent intent = getIntent();
        videoUrls = intent.getStringArrayListExtra(PATHS);
    }

}
