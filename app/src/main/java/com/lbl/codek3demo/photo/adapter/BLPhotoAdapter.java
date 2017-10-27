package com.lbl.codek3demo.photo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lbl.codek3demo.R;
import com.lbl.codek3demo.photo.bean.Photo;
import com.lbl.codek3demo.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BLPhotoAdapter extends BaseAdapter {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_PHOTO = 1;

    public int lastClickPosition = 0;
    public View lastCheckView;
    private List<Photo> mDatas;
    private Context mContext;
    private int mWidth;

    public BLPhotoAdapter(Context context) {
        this.mContext = context;
        int screenWidth = Util.getScreenWidth(mContext);
        mWidth = (screenWidth - dip2px(6)) / 4;
    }

    public int dip2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_PHOTO;
    }

    @Override
    public int getCount() {
        return (mDatas.size());
    }

    @Override
    public Photo getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mDatas.get(position).getId();
    }

    public void setDatas(List<Photo> mDatas) {
        this.mDatas = mDatas;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_photonew_layout, null, false);
            GridView.LayoutParams lp = new GridView.LayoutParams(mWidth, mWidth);
            convertView.setLayoutParams(lp);
            holder.photoImageView = (ImageView) convertView
                    .findViewById(R.id.imageview_photo);
            holder.selectView = (TextView) convertView
                    .findViewById(R.id.checkmark);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Photo photo = getItem(position);
        if (photo.isSelected()) {
            holder.selectView.setBackgroundResource(R.drawable.icon_picker_choose_pressed);
        } else {
            holder.selectView.setBackgroundResource(R.drawable.iv_choose_no);
        }

        Picasso.with(mContext).load("file://" + photo.getPath()).into(holder.photoImageView);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photo.isSelected()) {
                    photo.setSelected(false);
                    holder.selectView.setBackgroundResource(R.drawable.iv_choose_no);
                } else {
                    photo.setSelected(true);
                    holder.selectView.setBackgroundResource(R.drawable.icon_picker_choose_pressed);
                }
                if (onItemChanged != null) {
                    onItemChanged.changeBigImage(position, photo.isSelected(), photo.getPath());
                }
            }
        });
        return convertView;
    }

    public interface IonItemChanged {
        /**
         * 点击改变大图显示
         *
         * @param uri 图片所在的本地地址
         */
        void changeBigImage(int position, boolean choose, String uri);
    }


    IonItemChanged onItemChanged;//图片点击变换

    public void setIonItemChanged(IonItemChanged ionItemChanged) {
        this.onItemChanged = ionItemChanged;
    }

    private class ViewHolder {
        private ImageView photoImageView;
        private TextView selectView;
    }


}
