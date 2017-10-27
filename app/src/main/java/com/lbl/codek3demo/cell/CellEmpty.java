package com.lbl.codek3demo.cell;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.lbl.codek3demo.data.MvpBean;
import com.lbl.codek3demo.holder.BaseViewHolder;

/**
 * author：libilang
 * time: 17/7/18 17:24
 * 邮箱：libi_lang@163.com
 */
public class CellEmpty extends BaseViewHolder<MvpBean> {

    private Context context;

    RelativeLayout layoutTitle;

    public CellEmpty(View view) {
        super(view);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        bindViews();

    }

    private void bindViews() {
    }

    @Override
    public void onBind(int position, MvpBean iItem) {

    }
}
