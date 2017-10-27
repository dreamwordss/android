package com.lbl.codek3demo.video;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.lbl.codek3demo.R;

/**
 * author：libilang
 * time: 17/7/28 17:14
 * 邮箱：libi_lang@163.com
 */

public class VideoItem extends LinearLayout {

    public VideoItem(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_view, this);
    }

    public VideoItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
