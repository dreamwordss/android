package com.lbl.codek3demo.taobao;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lbl.codek3demo.R;
import com.lbl.codek3demo.pullstargview.PullLoadMoreRecyclerView;
import com.lbl.codek3demo.taobao.bean.SuccessRespBean;
import com.lbl.codek3demo.taobao.bean.taobaoBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * author：libilang
 * time: 17/8/29 11:10
 * 邮箱：libi_lang@163.com
 */

public class PullDemoActivity extends Activity implements View.OnClickListener, AbsListView.OnScrollListener {
    Dictionary<Integer, Integer> listViewItemHeights = new Hashtable<>();
    View rootView;
    TaobaoSearchAdapter mAdapter;
    List<taobaoBean.DataBean.ContentBean> lists = new ArrayList<>();
    private boolean mHasRequestedMore;
    PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_demo_2,
                null);
        setContentView(rootView);
        initView();
        getDataFromNet(false, 0);
    }


    int currentRank = 0;
    StaggeredRecycleViewAdapter mRecyclerViewAdapter;

    private void initView() {
        LayoutInflater layoutInflater = getLayoutInflater();

        View header;
        View footer = layoutInflater.inflate(R.layout.item_foot, null);
        mAdapter = new TaobaoSearchAdapter(PullDemoActivity.this, lists);
        mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) findViewById(R.id.pullLoadMoreRecyclerView);
        mPullLoadMoreRecyclerView.setStaggeredGridLayout(2);
        mRecyclerViewAdapter = new StaggeredRecycleViewAdapter(this, setList());
        mPullLoadMoreRecyclerView.setAdapter(mRecyclerViewAdapter);
        header = LayoutInflater.from(this).inflate(R.layout.item_header, mPullLoadMoreRecyclerView.getRecyclerView(), false);
        mRecyclerViewAdapter.setHeaderView(header);
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                setRefresh();
                getData();
            }

            @Override
            public void onLoadMore() {
                mCount = mCount + 1;
                getData();
            }
        });
        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPullLoadMoreRecyclerView.scrollDisY(2500);
            }
        });
    }

    private void getData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerViewAdapter.getDataList().addAll(setList());
                mRecyclerViewAdapter.notifyDataSetChanged();
                mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
            }
        }, 1000);
    }

    private void setRefresh() {

        mRecyclerViewAdapter.getDataList().clear();

        mCount = 1;

    }

    private int mCount = 1;

    private List<Map<String, String>> setList() {
        List<Map<String, String>> dataList = new ArrayList<>();
        int start = 30 * (mCount - 1);
        Map<String, String> map;
        for (int i = start; i < 30 * mCount; i++) {
            map = new HashMap<>();
            map.put("text", "Third" + i);
            map.put("height", (120 + 5 * i) + "");
            dataList.add(map);
        }
        return dataList;

    }

    private Handler handler = new Handler();


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
    }

    private void onLoadMoreItems() {

        getDataFromNet(true, currentRank);

    }

    /**
     * 获取屏幕上滚动距离
     *
     * @return
     */

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
