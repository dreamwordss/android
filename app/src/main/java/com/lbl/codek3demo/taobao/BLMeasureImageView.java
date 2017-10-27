package com.lbl.codek3demo.taobao;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * author：libilang
 * time: 17/8/2 20:01
 * 邮箱：libi_lang@163.com  重写imageview 可变高度
 */

public class BLMeasureImageView extends ImageView {
    private double mRatio;//想要显示的图片的高宽比例
    public BLMeasureImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public BLMeasureImageView(Context context) {
        super(context);
    }
    public void setHeightRatio(double ratio) {
        if (ratio != mRatio) {
            mRatio = ratio;
            requestLayout();
        }
    }
    public double getHeightRatio() {
        return mRatio;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRatio > 0.0) {
            // set the image views size
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * mRatio);
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}

