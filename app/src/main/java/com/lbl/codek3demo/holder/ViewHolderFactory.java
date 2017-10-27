package com.lbl.codek3demo.holder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lbl.codek3demo.R;
import com.lbl.codek3demo.adapter.MvpAdapter;
import com.lbl.codek3demo.cell.CellEmpty;
import com.lbl.codek3demo.cell.ImageCell;
import com.lbl.codek3demo.cell.ImageTextCell;
import com.lbl.codek3demo.cell.SpecialCell;
import com.lbl.codek3demo.cell.TextCell;
import com.lbl.codek3demo.cell.VideoCell;
import com.lbl.codek3demo.data.MvpBean;


/**
 * author：libilang
 * time: 17/7/18 17:24
 * 邮箱：libi_lang@163.com
 * 创建工程模式的holder去管理所有的类型 是的modle View解耦
 */
public class ViewHolderFactory {
    public static BaseViewHolder<MvpBean> buildViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MvpAdapter.VIEW_IMAGE://图
                return new ImageCell(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_image, parent, false), parent.getContext());
            case MvpAdapter.VIEW_TEXT://文字
                return new TextCell(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_text, parent, false), parent.getContext());
            case MvpAdapter.VIEW_IMAGETEXT://图文
                return new ImageTextCell(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_imagetext, parent, false), parent.getContext());
            case MvpAdapter.VIEW_VIDEO://视频
                return new VideoCell(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_video, parent, false), parent.getContext());
            case MvpAdapter.VIEW_SPCIAL://special 任意类型
                return new SpecialCell(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_special, parent, false), parent.getContext());
            default:
            case MvpAdapter.VIEW_EMPTY:
                return new CellEmpty(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell_image, parent, false));
        }
    }

}
