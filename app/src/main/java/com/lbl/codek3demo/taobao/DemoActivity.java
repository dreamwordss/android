package com.lbl.codek3demo.taobao;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.etsy.android.grid.StaggeredGridView;
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

public class DemoActivity extends Activity implements View.OnClickListener, AbsListView.OnScrollListener {
    Dictionary<Integer, Integer> listViewItemHeights = new Hashtable<>();
    View rootView;
    StaggeredGridView listview;
    TaobaoSearchAdapter mAdapter;
    List<taobaoBean.DataBean.ContentBean> lists = new ArrayList<>();
    private boolean mHasRequestedMore;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_demo_layout,
                null);
        setContentView(rootView);

        initView();
        initListener();
        getDataFromNet(false, 0);
    }


    int currentRank = 0;

    private void initView() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View header = layoutInflater.inflate(R.layout.item_header, null);
        View footer = layoutInflater.inflate(R.layout.item_foot, null);
        listview = (StaggeredGridView) findViewById(R.id.grid_view);
        listview.addHeaderView(header);
        listview.addFooterView(footer);
        mAdapter = new TaobaoSearchAdapter(DemoActivity.this, lists);
        listview.setAdapter(mAdapter);
        listview.setOnScrollListener(this);

    }

    private Handler handler = new Handler();

    private void initListener() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataFromNet(false, 0);
                        Log.e("bilang", "刷新");
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        Log.d("bilang", "onScrollStateChanged:" + scrollState);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        Log.d("bilang", "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);
        // our handling
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d("bilang", "加载更多 - so load more");
                mHasRequestedMore = true;
                onLoadMoreItems();
            }
        }
    }

    private void onLoadMoreItems() {

        // stash all the data in our backing store
        getDataFromNet(true, currentRank);

        // notify the adapter that we can update now
    }

    /**
     * 获取屏幕上滚动距离
     *
     * @return
     */
    private int getScrollY() {
        View c = listview.getChildAt(0); //this is the first visible row
        if (c == null) {
            return 0;
        }
        int scrollY = -c.getTop();
        listViewItemHeights.put(listview.getFirstVisiblePosition(), c.getHeight());
        for (int i = 0; i < listview.getFirstVisiblePosition(); ++i) {
            if (listViewItemHeights.get(i) != null) // (this is a sanity check)
                scrollY += listViewItemHeights.get(i); //add all heights of the views that are gone
        }
        return scrollY;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    private void getDataFromNet(final boolean more, int rank) {
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
                        processData(response, more);
                        swipeRefreshLayout.setRefreshing(false);

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
                mHasRequestedMore = false;
            }
            lists.addAll(dataBean.content);

            mAdapter.notifyDataSetChanged();
            Log.e("bilang", "解析成功==" + lists.toString());
        } catch (Exception e) {
            Log.e("bilang", "解析失败==" + e.toString());
        }
    }


}
