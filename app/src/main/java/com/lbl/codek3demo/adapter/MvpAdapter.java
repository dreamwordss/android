package com.lbl.codek3demo.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lbl.codek3demo.R;
import com.lbl.codek3demo.data.MvpBean;
import com.lbl.codek3demo.holder.BaseViewHolder;
import com.lbl.codek3demo.holder.ViewHolderFactory;
import com.lbl.codek3demo.util.StringUtil;

import java.util.List;

/**
 * author：libilang
 * time: 17/7/18 17:24
 * 邮箱：libi_lang@163.com
 */

public class MvpAdapter extends BLAdapter<MvpBean> {

    public static int VIEW_COUNT = 6;//类型数量
    public static final int VIEW_IMAGE = 0;//图片cell
    public static final int VIEW_TEXT = 1;//文字cell
    public static final int VIEW_VIDEO = 2;//视频cell
    public static final int VIEW_SPCIAL = 3;//特殊类型的cell
    public static final int VIEW_IMAGETEXT = 4;//图文cell
    public static final int VIEW_EMPTY = 10;//缺省cell
    public ListView mlistView;
    public ArrayMap<Object, Integer> mHolderHelper;
    public static int[] colorArray = null;

    public MvpAdapter(List<MvpBean> list, ListView listView, Context context) {
        super(list, context);
        this.mlistView = listView;
        mHolderHelper = new ArrayMap<>();
        colorArray = StringUtil.getColorArray(context, R.array.caption_style_colors);
    }

    @Override
    public int getCount() {
        int count;
        if (list == null) {
            count = 0;
        } else
            count = list.size();
        return count;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        String type = list.get(position).type;
        //----------
        if ("image".equalsIgnoreCase(type)) {//图片
            return VIEW_IMAGE;
        } else if ("text".equalsIgnoreCase(type)) {//文字
            return VIEW_TEXT;
        } else if ("video".equalsIgnoreCase(type)) {//视频
            return VIEW_VIDEO;
        } else if ("imagetext".equalsIgnoreCase(type)) {//图文
            return VIEW_IMAGETEXT;
        } else if ("special".equalsIgnoreCase(type)) {//特殊别的任意类型
            return VIEW_SPCIAL;
        } else {
            return VIEW_EMPTY;
        }
    }

    @Override
    public View CreatView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder;
        if (convertView == null) {
            holder = ViewHolderFactory.buildViewHolder(parent, getItemViewType(position));
            convertView = holder.itemView;
            convertView.setTag(holder);
        } else {
            holder = (BaseViewHolder) convertView.getTag();
        }
        holder.onBind(position, getItem(position));
        mHolderHelper.put(holder, position + mlistView.getHeaderViewsCount());
        return convertView;
    }
}
