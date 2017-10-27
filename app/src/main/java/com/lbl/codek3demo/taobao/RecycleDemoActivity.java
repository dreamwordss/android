package com.lbl.codek3demo.taobao;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

public class RecycleDemoActivity extends Activity implements View.OnClickListener {
    Dictionary<Integer, Integer> listViewItemHeights = new Hashtable<>();
    View rootView;
    List<taobaoBean.DataBean.ContentBean> lists = new ArrayList<>();
    private RecyclerView recyclerView;

    //    private MyRecyclerViewAdapter adapter;
    private MyAdapter adapter;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_recycle_layout,
                null);
        setContentView(rootView);
        initView();
        initListener();
        getDataFromNet(false, 0);
    }


    int currentRank = 0;
    private Handler handler = new Handler();
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    boolean isLoading;
    int[] shorts;
    View header;

    private void initView() {
        shorts = new int[2];
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
                        getDataFromNet(true, currentRank);

                    }
                }, 2000);
            }
        });
        //1.找到控件
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //2.声名为瀑布流的布局方式: 3列,垂直方向
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //3.为recyclerView设置布局管理器
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        //3.创建适配器
//        adapter = new MyRecyclerViewAdapter(this, lists);
        adapter = new MyAdapter(this, lists);

        //设置添加,移除item的动画,DefaultItemAnimator为默认的
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //4.设置适配器
        recyclerView.setAdapter(adapter);

        header = LayoutInflater.from(this).inflate(R.layout.item_header, recyclerView, false);
        adapter.setHeaderView(header);

//        //添加点击事件
//        adapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnRecyclerItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                //Toast.makeText(MainActivity.this,"单击了:"+mDatas.get(position),Toast.LENGTH_SHORT).show();
////                adapter.addItem(position, "添加的内容");
//                Log.i("tag", "onItemClick: " + position);
//            }
//        });
//        //设置长按事件
//        adapter.setOnItemLongClickListener(new MyRecyclerViewAdapter.onRecyclerItemLongClickListener() {
//            @Override
//            public void onItemLongClick(View view, int position) {
//                //Toast.makeText(MainActivity.this,"长按了:"+mDatas.get(position),Toast.LENGTH_SHORT).show();
////                adapter.removeItem(position);
//                Log.i("tag", "onItemLongClick: " + position);
//            }
//        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("test", "StateChanged = " + newState);


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] lastVisibleItemPositions = staggeredGridLayoutManager.findLastVisibleItemPositions(shorts);
                int lastVisibleItemPosition = lastVisibleItemPositions[0];
                int lastVisibleItemPosition1 = lastVisibleItemPositions[1];

                View firstVisiableChildView = staggeredGridLayoutManager.findViewByPosition(lastVisibleItemPosition);
                int itemHeight = firstVisiableChildView.getHeight();

                int height = ((lastVisibleItemPosition) * itemHeight) / 2 + firstVisiableChildView.getTop();

                Log.d("test", "onScrolled-----" + lastVisibleItemPosition + "---" + lastVisibleItemPosition1 + "----" + adapter.getItemCount() + "---" + height);
                if (lastVisibleItemPosition + 2 == adapter.getItemCount()) {
                    Log.d("test", "加载更多loading executed");

                    boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                    if (isRefreshing) {
                        adapter.notifyItemRemoved(adapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getDataFromNet(true, currentRank);

                            }
                        }, 1000);
                    }
                }
            }
        });

    }

    int[] ss = new int[2];
    int[] aa = new int[2];

    public int getScrollY() {
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
        int[] firstVisibleItemPositions = layoutManager.findFirstVisibleItemPositions(ss);
        Log.e("bilang", firstVisibleItemPositions[0] + "---" + firstVisibleItemPositions[1]);
        int[] lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(aa);
        Log.e("bilang", lastVisibleItemPositions[0] + "---" + lastVisibleItemPositions[1]);

//        View firstVisiableChildView = layoutManager.findViewByPosition(position);
//        int itemHeight = firstVisiableChildView.getHeight();


        return 1;
    }


    boolean isCanAnima = true;
    boolean isShow = true;

    private void initListener() {
        findViewById(R.id.scroll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollview();
            }
        });
    }

    private void scrollview() {
//        recyclerView.scrollBy(0, 1000);
        StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
//        recyclerView.smoothScrollToPosition(1);
        recyclerView.scrollToPosition(0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollBy(0, header.getHeight() - 300);
            }
        }, 150);
//        recyclerView.scrollBy(0, 3000);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }


    private Animator mAnimatorTitle;
    private Animator mAnimatorContent;


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
                        swipeRefreshLayout.setRefreshing(false);
                        isLoading = false;
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
                        isLoading = false;
                        if (more) {
                            Log.d("test", "加载更多完成load more completed");
                        }
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
