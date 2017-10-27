package com.lbl.codek3demo.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * author：libilang
 * time: 17/7/18 17:24
 * 邮箱：libi_lang@163.com
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void onBind(int position, T iItem);

}
