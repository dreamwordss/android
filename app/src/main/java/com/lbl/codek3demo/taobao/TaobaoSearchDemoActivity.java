package com.lbl.codek3demo.taobao;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.etsy.android.grid.StaggeredGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lbl.codek3demo.R;
import com.lbl.codek3demo.taobao.bean.SuccessRespBean;
import com.lbl.codek3demo.taobao.bean.taobaoBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import okhttp3.Call;

/**
 * author：libilang
 * time: 17/8/29 11:10
 * 邮箱：libi_lang@163.com
 */

public class TaobaoSearchDemoActivity extends Activity implements View.OnClickListener {
    Dictionary<Integer, Integer> listViewItemHeights = new Hashtable<>();
    View rootView;
    BLPullToRefreshStaggeredGridView listview;
    TaobaoSearchAdapter adapter;
    List<taobaoBean.DataBean.ContentBean> lists = new ArrayList<>();
    RelativeLayout mTitle;
    GoodsfilterRightView goodsfilterRightView;
    TextView tvGoodsFilter, tv_up;
    LinearLayout layoutBottom;
    RelativeLayout loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_taobao_layout,
                null);
        setContentView(rootView);
        mTouchSlopDistance = ViewConfiguration.get(this).getScaledTouchSlop();
        initView();
        initListener();
        showHideTitleBar(true);
        layoutBottomAnimator(true);
        getData(false, 0);
    }


    float mFirstY, mCurrentY, mTouchSlopDistance = 10;
    int currentRank = 0;

    private void initView() {
        loadingView = (RelativeLayout) findViewById(R.id.loading_view);
        mTitle = (RelativeLayout) findViewById(R.id.title_layout);
        tv_up = (TextView) findViewById(R.id.tv_up);
        tvGoodsFilter = (TextView) findViewById(R.id.tv_goods_filter);
        layoutBottom = (LinearLayout) findViewById(R.id.layout_bootom);
        listview = (BLPullToRefreshStaggeredGridView) findViewById(R.id.tianmao_listview);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.setNeedPreLoading(false);
        adapter = new TaobaoSearchAdapter(TaobaoSearchDemoActivity.this, lists);
        listview.getRefreshableView().setAdapter(adapter);
    }

    boolean isCanAnima = true;
    boolean isShow = true;

    private void initListener() {
        tv_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listview.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
                listview.getRefreshableView().resetToTop();
                adapter.notifyDataSetChanged();
                showHideTitleBar(true);
                layoutBottomAnimator(true);
            }
        });
        tvGoodsFilter.setOnClickListener(this);
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<StaggeredGridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<StaggeredGridView> refreshView) {
                //刷新
                getData(false, 0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<StaggeredGridView> refreshView) {
                //加载更多
                getData(true, currentRank);
            }
        });
        listview.getRefreshableView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mFirstY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurrentY = event.getY();
                        if (mCurrentY - mFirstY > mTouchSlopDistance / 2) {
                            // 下滑 显示titleBar
                            showHideTitleBar(true);
                        } else if (mFirstY - mCurrentY > mTouchSlopDistance) {
                            // 上滑 隐藏titleBar
                            showHideTitleBar(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
        listview.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView mListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (getScrollY() < 1000) {
                    if (isCanAnima && !isShow) {
                        Log.e("bilang", "---显示");
                        layoutBottomAnimator(false);
                    }
                } else {
                    if (isCanAnima && isShow) {
                        layoutBottomAnimator(true);
                    }
                }
            }
        });
    }

    //控制显示底部的按钮
    public void layoutBottomAnimator(boolean Up) {
        if (Up) {
            isShow = false;
            Log.e("bilang", "---隐藏");
            Animation animation = AnimationUtils.loadAnimation(TaobaoSearchDemoActivity.this, R.anim.anim_up);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isCanAnima = false;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isCanAnima = true;
                    animation.cancel();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    isCanAnima = false;
                }
            });
            layoutBottom.startAnimation(animation);
        } else {
            isShow = true;
            Animation animation = AnimationUtils.loadAnimation(TaobaoSearchDemoActivity.this, R.anim.anim_down);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isCanAnima = false;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isCanAnima = true;
                    animation.cancel();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    isCanAnima = false;
                }
            });
            layoutBottom.startAnimation(animation);
        }
    }


    //获取屏幕上滚动距离
    private int getScrollY() {
        View c = listview.getRefreshableView().getChildAt(0); //this is the first visible row
        if (c == null) {
            return 0;
        }
        int scrollY = -c.getTop();
        listViewItemHeights.put(listview.getRefreshableView().getFirstVisiblePosition(), c.getHeight());
        for (int i = 0; i < listview.getRefreshableView().getFirstVisiblePosition(); ++i) {
            if (listViewItemHeights.get(i) != null) // (this is a sanity check)
                scrollY += listViewItemHeights.get(i); //add all heights of the views that are gone
        }
        return scrollY;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_goods_filter:
                showFilterView();
                break;
        }
    }

    private void showFilterView() {
        goodsfilterRightView = new GoodsfilterRightView(this);
        goodsfilterRightView.setClick(new GoodsfilterRightView.itemClickener() {
            @Override
            public void click(View view) {
                goodsfilterRightView.invokeStopAnim();
            }
        });
        goodsfilterRightView.getBackground().setAlpha(0);
        goodsfilterRightView.showAtLocation(rootView, Gravity.RIGHT
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    private Animator mAnimatorTitle;
    private Animator mAnimatorContent;

    //头部搜索view 和 瀑布流移动
    private void showHideTitleBar(boolean down) {
        if (mAnimatorTitle != null && mAnimatorTitle.isRunning()) {
            mAnimatorTitle.cancel();
        }
        if (mAnimatorContent != null && mAnimatorContent.isRunning()) {
            mAnimatorContent.cancel();
        }
        if (down) {
            mAnimatorTitle = ObjectAnimator.ofFloat(mTitle, "translationY", mTitle.getTranslationY(), 0);
            mAnimatorTitle.setDuration(350);
            mAnimatorContent = ObjectAnimator.ofFloat(listview, "translationY", listview.getTranslationY(), getResources().getDimension(R.dimen.title_));
            mAnimatorContent.setDuration(350);
        } else {
            mAnimatorTitle = ObjectAnimator.ofFloat(mTitle, "translationY", mTitle.getTranslationY(), -mTitle.getHeight());
            mAnimatorTitle.setDuration(350);
            mAnimatorContent = ObjectAnimator.ofFloat(listview, "translationY", listview.getTranslationY(), 0);
            mAnimatorContent.setDuration(350);
        }
        mAnimatorTitle.start();
        mAnimatorContent.start();
    }

    private void getData(final boolean more, int rank) {
        if (more) {
            currentRank = rank + 20;
        } else {
            currentRank = rank;
        }
        String url = "https://napi.xiaohongchun.com/v1/resource/search/%E9%9D%A2%E8%86%9C?suggest=true&size=20&rank=" + currentRank;
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("bilang", "首页请求失败==" + e.getMessage());
                        listview.onRefreshComplete();
                    }

                    /**
                     * 当请求成功的时候回调
                     * @param response 请求成功数据
                     * @param id
                     */
                    @Override
                    public void onResponse(String response, int id) {
//                        Log.e("bilang", "首页请求成功==" + response);
                        //解析数据
                        listview.onRefreshComplete();
                        processData(response, more);
                        loadingView.setVisibility(View.GONE);
                    }
                });
    }

    private void processData(String json, boolean more) {
        //使用FastJson去解析Json数据，将json字符串转换成一个ResultBeanData对象
        try {
            SuccessRespBean respBean = JSONObject.parseObject(json, SuccessRespBean.class);
            taobaoBean.DataBean dataBean = JSON.parseObject(respBean.data, taobaoBean.DataBean.class);
            if (!more) {
                lists.clear();
            }
            lists.addAll(dataBean.content);

            adapter.notifyDataSetChanged();
            Log.e("bilang", "解析成功==" + lists.toString());
        } catch (Exception e) {
            Log.e("bilang", "解析失败==" + e.toString());
        }
    }


}
