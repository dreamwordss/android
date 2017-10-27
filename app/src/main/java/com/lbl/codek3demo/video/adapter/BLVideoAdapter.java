package com.lbl.codek3demo.video.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;

import com.lbl.codek3demo.R;
import com.lbl.codek3demo.photo.bean.MediaModeVideo;
import com.lbl.codek3demo.util.Util;
import com.lbl.codek3demo.video.BLLoadVideoPhotoAsync;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * author：libilang
 * time: 17/3/31 2:01
 * 邮箱：libi_lang@163.com
 * 选择视频界面 视频的adapter
 */
public class BLVideoAdapter extends BaseAdapter {
    private Activity activity;
    private static final int TYPE_VIDEO = 1;
    private itemCheckedListener listener;
    private List<MediaModeVideo> mDatas;

    private Context mContext;
    private int mWidth;
    // 是否显示相机，默认不显示
    private HashMap<Integer, View> lmap = new HashMap<Integer, View>();

    public BLVideoAdapter(Context context) {
        this.mContext = context;
        int screenWidth = Util.getScreenWidth(mContext);
        mWidth = screenWidth / 4;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_VIDEO;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public MediaModeVideo getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setDatas(List<MediaModeVideo> mDatas) {
        this.mDatas = mDatas;
    }


    public void setItemCheckedListener(itemCheckedListener itemCheckedListener) {
        this.listener = itemCheckedListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (lmap.get(position) == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_choose_video_layout, null, false);
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.imageview_video);
            holder.selectView = (CheckBox) convertView
                    .findViewById(R.id.checkmark);
            lmap.put(position, convertView);
            GridView.LayoutParams lp = new GridView.LayoutParams(mWidth, mWidth);
            convertView.setLayoutParams(lp);
            convertView.setTag(holder);
        } else {
            convertView = lmap.get(position);
            holder = (ViewHolder) convertView.getTag();
        }

        final MediaModeVideo mediaModeVideo = getItem(position);
        //异步加载出视频的第一帧图像
        new BLLoadVideoPhotoAsync(getActivity(), mediaModeVideo, holder.imageView, false, mWidth, 0)
                .executeOnExecutor(BLLoadVideoPhotoAsync.es,
                        mediaModeVideo.getUrl().toString());

        holder.selectView.setSelected(mediaModeVideo.isClicked());
        holder.selectView.setChecked(mediaModeVideo.isClicked());
        holder.selectView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    File file = new File(mediaModeVideo.getUrl());
                    if (!file.exists()) {
                        return;
                    }
                    //选中添加
                    mediaModeVideo.setIsClicked(true);
                    holder.selectView.setSelected(true);
                    holder.selectView.setChecked(true);
                    listener.itemCheckedListener(position, true);
                } else {
                    //取消删除
                    mediaModeVideo.setIsClicked(false);
                    holder.selectView.setSelected(false);
                    holder.selectView.setChecked(false);
                    listener.itemCheckedListener(position, false);
                }
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击去看视频
                if (listener != null) {
                    File file = new File(mediaModeVideo.getUrl());
                    if (!file.exists()) {
                        return;
                    }
                    listener.simpleLookListener(position, mediaModeVideo.getUrl(), mediaModeVideo.isClicked());
                }
            }
        });

        return convertView;
    }

    private class ViewHolder {
        private ImageView imageView;
        private CheckBox selectView;
    }

    /**
     * 点击拍摄
     */
    private Intent intent;


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public interface itemCheckedListener {
        void itemCheckedListener(int position, boolean chooses);

        void simpleLookListener(int position, String url, boolean isCheck);
    }
}
