package com.lbl.codek3demo.taobao;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.lbl.codek3demo.R;


/**
 * @author libilang
 *         2016.1.13 7:40:12
 * @ClassName: CheckCardPersonPopWindow
 * @Description: TODO 绑定银行卡 选择持卡人
 */
public class GoodsfilterRightView extends PopupWindow {
    protected final int DISMISS = 1212;
    protected final int ONCLICK = 1213;
    private Context mContext;


    private View mRootView;
    private ImageView mBackgroudView;
    private LinearLayout mRightViewContent;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DISMISS:
                    dismiss();
                    break;
                case ONCLICK:
                    break;
                default:
                    break;
            }
        }
    };

    public GoodsfilterRightView() {
        super();
    }


    public GoodsfilterRightView(Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootView = inflater.inflate(R.layout.layout_allparty_right_view, null);
        mBackgroudView = (ImageView) mRootView.findViewById(R.id.iv_pop_bg);
        mRightViewContent = (LinearLayout) mRootView.findViewById(R.id.layout_all_content);
        // 设置SelectPicPopupWindow的View
        this.setContentView(mRootView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        this.setOutsideTouchable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        mRightViewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickener.click(view);
            }
        });
        mRootView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int with = mRootView.findViewById(R.id.layout_pop_menu)
                        .getWidth();
                int selfWith = mRootView.getWidth();
                int trueWith = selfWith - with;
                int X = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (X < trueWith) {
                        invokeStopAnim();
                    }
                }
                return true;
            }
        });

        invokeStartAnim();
    }


    private void invokeStartAnim() {
        mRightViewContent.setVisibility(View.VISIBLE);
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        translateAnimation.setDuration(300);
        animationSet.addAnimation(translateAnimation);
        mRightViewContent.startAnimation(animationSet);

        mBackgroudView.setVisibility(View.VISIBLE);
        AnimationSet animationSet1 = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300);
        animationSet1.addAnimation(alphaAnimation);
        mBackgroudView.startAnimation(alphaAnimation);
    }

    public void invokeStopAnim() {

        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.1f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        translateAnimation.setDuration(300);
        // translateAnimation.setFillAfter(true);
        animationSet.addAnimation(translateAnimation);
        mRightViewContent.startAnimation(animationSet);


        AnimationSet animationSet1 = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        animationSet1.addAnimation(alphaAnimation);
        mBackgroudView.startAnimation(alphaAnimation);
        mHandler.sendEmptyMessageDelayed(DISMISS, 300);

    }

    private void rotateArrow(Boolean isOpen, ImageView imgView) {
        RotateAnimation animation = null;
        if (isOpen) {
            animation = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            animation = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        animation.setDuration(200);//设置动画持续时间
        animation.setRepeatCount(0);
        animation.setFillAfter(true);
        imgView.setAnimation(animation);
        animation.startNow();
    }

    public void setClick(itemClickener click) {
        itemClickener = click;
    }

    itemClickener itemClickener;

    public interface itemClickener {
        void click(View view);
    }

}
