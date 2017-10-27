package com.lbl.codek3demo.taobao;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.etsy.android.grid.StaggeredGridView;
import com.handmark.pulltorefresh.library.OverscrollHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * 非线性瀑布流view，基于    PullToRefreshview         人生就是一次又一次的翻越高山，要死也要死在峰顶
 * author：libilang
 * 邮箱：libi_lang@163.com  两种加载方式，可是只是头部刷新，也可以两者都有
 */
@SuppressLint("NewApi")
public class BLPullToRefreshStaggeredGridView extends PullToRefreshBase<StaggeredGridView> {

    private static final OnRefreshListener<StaggeredGridView> defaultOnRefreshListener1 = new OnRefreshListener<StaggeredGridView>() {

        @Override
        public void onRefresh(PullToRefreshBase<StaggeredGridView> refreshView) {
        }

    };

    private static final OnRefreshListener2<StaggeredGridView> defaultOnRefreshListener = new OnRefreshListener2<StaggeredGridView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<StaggeredGridView> refreshView) {
            Log.e("bilang", refreshView.isHeaderShown() + "333onPullDownToRefresh--头 尾--" + refreshView.isFooterShown());

        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<StaggeredGridView> refreshView) {
            Log.e("bilang", refreshView.isHeaderShown() + "333onPullUpToRefresh--头 尾--" + refreshView.isFooterShown());

        }
    };


    public BLPullToRefreshStaggeredGridView(Context context) {
        super(context);

        /**
         * Added so that by default, Pull-to-Refresh refreshes the page
         */
        setOnRefreshListener(defaultOnRefreshListener);
    }

    public BLPullToRefreshStaggeredGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        /**
         * Added so that by default, Pull-to-Refresh refreshes the page
         */
        setOnRefreshListener(defaultOnRefreshListener);

    }

    public BLPullToRefreshStaggeredGridView(Context context, Mode mode) {
        super(context, mode);

        /**
         * Added so that by default, Pull-to-Refresh refreshes the page
         */
        setOnRefreshListener(defaultOnRefreshListener);

    }

    public BLPullToRefreshStaggeredGridView(Context context, Mode mode,
                                            AnimationStyle style) {
        super(context, mode, style);

        /**
         * Added so that by default, Pull-to-Refresh refreshes the page
         */
        setOnRefreshListener(defaultOnRefreshListener);

    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected StaggeredGridView createRefreshableView(Context context,
                                                      AttributeSet attrs) {
        StaggeredGridView gridView;

        if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
            gridView = new InternalStaggeredGridViewSDK9(context, attrs);
        } else {
            gridView = new StaggeredGridView(context, attrs);
        }

        gridView.setId(com.handmark.pulltorefresh.library.R.id.gridview);
        return gridView;
    }

    @Override
    protected boolean isReadyForPullStart() {
        boolean result = false;
        View v = getRefreshableView().getChildAt(0);
        if (getRefreshableView().getFirstVisiblePosition() == 0) {
            if (v != null) {
                boolean isTopFullyVisible = v.getTop() >= 0;
                result = isTopFullyVisible;
            }
        }
        return result;
    }
    @Override
    protected boolean isReadyForPullEnd() {
        boolean result = false;
        int last = getRefreshableView().getChildCount() - 1;
        View v = getRefreshableView().getChildAt(last);

        int firstVisiblePosition = getRefreshableView()
                .getFirstVisiblePosition();
        int visibleItemCount = getRefreshableView().getChildCount();
        int itemCount = getRefreshableView().getAdapter().getCount();
        if (firstVisiblePosition + visibleItemCount + 1 >= itemCount) {
            if (v != null) {
                boolean isLastFullyVisible = v.getBottom() <= getRefreshableView()
                        .getHeight();
                result = isLastFullyVisible;
            }
        }
//        Log.e("bilang", "isReadyForPullEnd==" + result);
        return result;
    }

    @Override
    protected void onPtrRestoreInstanceState(Bundle savedInstanceState) {
        super.onPtrRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPtrSaveInstanceState(Bundle saveState) {
        super.onPtrSaveInstanceState(saveState);
    }

    @TargetApi(9)
    final class InternalStaggeredGridViewSDK9 extends StaggeredGridView {
        // WebView doesn't always scroll back to it's edge so we add some
        // fuzziness
        static final int OVERSCROLL_FUZZY_THRESHOLD = 2;

        // WebView seems quite reluctant to overscroll so we use the scale
        // factor to scale it's value
        static final float OVERSCROLL_SCALE_FACTOR = 1.5f;

        public InternalStaggeredGridViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                       int scrollY, int scrollRangeX, int scrollRangeY,
                                       int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

            final boolean returnValue = super.overScrollBy(deltaX, deltaY,
                    scrollX, scrollY, scrollRangeX, scrollRangeY,
                    maxOverScrollX, maxOverScrollY, isTouchEvent);
            // Does all of the hard work...
            OverscrollHelper.overScrollBy(BLPullToRefreshStaggeredGridView.this,
                    deltaX, scrollX, deltaY, getScrollRange(), isTouchEvent);

            return returnValue;
        }

        /**
         * Taken from the AOSP ScrollView source
         */
        private int getScrollRange() {
            int scrollRange = 0;
            if (getChildCount() > 0) {
                View child = getChildAt(0);
                scrollRange = Math.max(0, child.getHeight()
                        - (getHeight() - getPaddingBottom() - getPaddingTop()));
            }
            return scrollRange;
        }

    }

    public void setScroll() {
        View view = this.getRefreshableView().getChildAt(0);
        Log.e("bilang", "view.getTop()====" + view.getTop());
        this.getRefreshableView().scrollTo(0, view.getTop());
    }


}
