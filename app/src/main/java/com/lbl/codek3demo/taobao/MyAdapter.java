package com.lbl.codek3demo.taobao;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.lbl.codek3demo.R;
import com.lbl.codek3demo.taobao.bean.taobaoBean;

import java.text.DecimalFormat;
import java.util.List;

/**
 * author：libilang
 * time: 17/9/4 12:15
 * 邮箱：libi_lang@163.com
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    private Context context;

    //获取从Activity中传递过来每个item的数据集合
    private List<taobaoBean.DataBean.ContentBean> mDatas;
    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;

    //构造函数
    public MyAdapter(Context context, List<taobaoBean.DataBean.ContentBean> list) {
        this.mDatas = list;
        this.context = context;

    }

    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    /**
     * 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    *
     */
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

    //创建View，如果是HeaderView或者是FooterView，直接在Holder中返回
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ListHolder(mHeaderView);
        }
//        if (mFooterView != null && viewType == TYPE_FOOTER) {
//            return new ListHolder(mFooterView);
//        }

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.tianmao_item2, parent, false);
        return new ListHolder(layout);
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的，   HeaderView和FooterView, 就不同绑定了
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holders, int position) {
        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER)
            return;
        final int pos = getRealPosition(holders);
        if (getItemViewType(position) == TYPE_NORMAL) {
            if (holders instanceof ListHolder) {
                Log.e("onBindViewHolder" +
                        "", pos + "---" + mDatas.size() + "---" + getItemCount());
                ListHolder holder = (ListHolder) holders;
                taobaoBean.DataBean.ContentBean bean = mDatas.get(pos);
                DecimalFormat df = new DecimalFormat("######0.00");
                double ratio = 1;
                if (bean.getType().equalsIgnoreCase("sharebuy")) {
                    if (bean.getSharebuy().width_height_ratio <= 0) {
                        ratio = 1;
                    } else {
                        ratio = 1 / bean.getSharebuy().width_height_ratio;
                        if (ratio > 1.75) {
                            ratio = 1.75;
                        } else if (ratio < 0.6) {
                            ratio = 0.6;
                        } else {
                            String format = df.format(ratio);
                            ratio = Double.valueOf(format);
                        }
                    }
                    Log.d("bilang", "sharebuy:" + position + " ratio:" + ratio);
                } else {
                    if (bean.getVideo().ncover_ratio <= 0) {
                        ratio = 1;
                    } else {
                        ratio = 1 / bean.getVideo().ncover_ratio;
                        if (ratio > 1.75) {
                            ratio = 1.75;
                        } else if (ratio < 0.6) {
                            ratio = 0.6;
                        } else {
                            String format = df.format(ratio);
                            ratio = Double.valueOf(format);
                        }
                    }
                    Log.d("bilang", "video:--" + position + " ratio:" + ratio);
                }
                holder.imgContent.setHeightRatio(ratio);
                if (bean.getType().equalsIgnoreCase("sharebuy")) {
                    taobaoBean.DataBean.ContentBean.ShareBuyBean shareBuyBean = bean.getSharebuy();
                    Glide.with(context).load(shareBuyBean.getCover_url() + "-mid2x.webp").into(holder.imgContent);
                } else {
                    taobaoBean.DataBean.ContentBean.VideoBean videoBean = bean.getVideo();
                    Glide.with(context).load(videoBean.getCover_url() + "-mid2x.webp").into(holder.imgContent);
                }

            }
        }
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

    //在这里面加载ListView中的每个item的布局
    class ListHolder extends RecyclerView.ViewHolder {
        BLMeasureImageView imgContent;


        public ListHolder(View itemView) {
            super(itemView);
            //如果是headerview或者是footerview,直接返回
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }

            imgContent = (BLMeasureImageView) itemView
                    .findViewById(R.id.ivvideoicon);
        }

    }

    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
        return mHeaderView == null ? mDatas.size() : mDatas.size() + 1;
//        if (mHeaderView == null && mFooterView == null) {
//            return mDatas.size();
//        } else if (mHeaderView == null && mFooterView != null) {
//            return mDatas.size() + 1;
//        } else if (mHeaderView != null && mFooterView == null) {
//            return mDatas.size() + 1;
//        } else {
//            return mDatas.size() + 2;
//        }
    }
}
