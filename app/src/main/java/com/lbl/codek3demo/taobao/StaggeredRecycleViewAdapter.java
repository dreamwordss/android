package com.lbl.codek3demo.taobao;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lbl.codek3demo.R;

import java.util.List;
import java.util.Map;

/**
 * Created by WuXiaolong on 2015/9/14.
 */
public class StaggeredRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    private Context mContext;
    private List<Map<String, String>> dataList;
    private View mHeaderView;

    public List<Map<String, String>> getDataList() {
        return dataList;
    }

    public StaggeredRecycleViewAdapter(Context context, List<Map<String, String>> dataList) {
        this.dataList = dataList;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    //创建View，如果是HeaderView或者是FooterView，直接在Holder中返回
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ListHolder(mHeaderView);
        }

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ListHolder(layout);
    }

//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        if (mHeaderView != null && viewType == TYPE_HEADER) {
//            return new ListHolder(mHeaderView);
//        }
//
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
//        return new ListHolder(v);
//    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holders, int position) {
        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER)
            return;
        if (getItemViewType(position) == TYPE_NORMAL)
            if (holders instanceof ListHolder) {
                final int pos = getRealPosition(holders);
                ListHolder holder = (ListHolder) holders;
                holder.title.setText(dataList.get(pos).get("text"));
                holder.title.setHeight(Integer.parseInt(dataList.get(pos).get("height")));//高度随机，下拉刷新高度会变
            }

    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? dataList.size() : dataList.size() + 1;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
//        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
//        if (lp != null
//                && lp instanceof StaggeredGridLayoutManager.LayoutParams
//                && holder.getLayoutPosition() == 0) {
//            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
//            p.setFullSpan(true);
//        }
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == 0);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null)
            return TYPE_NORMAL;
        if (position == 0)
            return TYPE_HEADER;
        return TYPE_NORMAL;

//        if (mHeaderView == null && mFooterView == null) {
//            return TYPE_NORMAL;
//        }
//        if (position == 0) {
//            //第一个item应该加载Header
//            return TYPE_HEADER;
//        }
//        if (position == getItemCount() - 1) {
//            //最后一个,应该加载Footer
//            return TYPE_FOOTER;
//        }
//        return TYPE_NORMAL;
    }

    //在这里面加载ListView中的每个item的布局
    class ListHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ListHolder(View itemView) {
            super(itemView);
            //如果是headerview或者是footerview,直接返回
            if (itemView == mHeaderView) {
                return;
            }
            title = (TextView) itemView.findViewById(R.id.title);

        }

    }

}
