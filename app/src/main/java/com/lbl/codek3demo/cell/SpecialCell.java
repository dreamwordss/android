package com.lbl.codek3demo.cell;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.lbl.codek3demo.R;
import com.lbl.codek3demo.data.MvpBean;
import com.lbl.codek3demo.holder.BaseViewHolder;

import static com.lbl.codek3demo.adapter.MvpAdapter.colorArray;

/**
 * author：libilang
 * time: 17/7/18 17:24
 * 邮箱：libi_lang@163.com
 */

public class SpecialCell extends BaseViewHolder<MvpBean> {
    private Context context;
    RelativeLayout layoutTitle;

    public SpecialCell(View view, Context context) {
        super(view);
        this.context = context;
        initView(view);
    }

    private void initView(View view) {
        layoutTitle = (RelativeLayout) view.findViewById(R.id.titlelayout);

    }

    @Override
    public void onBind(int position, final MvpBean iItem) {
        layoutTitle.setBackgroundColor(colorArray[position]);

    }
}
