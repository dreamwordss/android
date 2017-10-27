package com.lbl.codek3demo.scalldemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lbl.codek3demo.R;

import java.util.ArrayList;
import java.util.List;

public class StickListView extends Activity {
    private ListView lv;

    private LinearLayout invis;
    Adapter adapter;
    private List<String> strs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky);
        invis = (LinearLayout) findViewById(R.id.invis);

        strs = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            strs.add("data-----" + i);
        }

        lv = (ListView) findViewById(R.id.lv);
        View header = View.inflate(this, R.layout.stick_header, null);//头部内容
        lv.addHeaderView(header);//添加头部
        lv.addHeaderView(View.inflate(this, R.layout.stick_action, null));//ListView条目中的悬浮部分 添加到头部
        adapter = new Adapter(StickListView.this, strs);
        lv.setAdapter(adapter);
        lv.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 1) {
                    invis.setVisibility(View.VISIBLE);
                } else {

                    invis.setVisibility(View.GONE);
                }
            }
        });
    }

    class Adapter extends BaseAdapter {
        private List<String> datas;
        private Context context;

        public Adapter(Context context, List<String> datas) {
            this.context = context;
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int i) {
            return datas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.cell_video, null, false);
            TextView duration = (TextView) convertView
                    .findViewById(R.id.mvp_image);
            String sss = (String) getItem(position);
            duration.setText(sss);

            return convertView;
        }
    }

}
