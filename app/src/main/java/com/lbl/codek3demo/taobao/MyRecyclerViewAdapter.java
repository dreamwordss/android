package com.lbl.codek3demo.taobao;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.lbl.codek3demo.R;
import com.lbl.codek3demo.taobao.bean.taobaoBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * author：libilang
 * time: 17/9/4 12:15
 * 邮箱：libi_lang@163.com
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context context;
    private List<taobaoBean.DataBean.ContentBean> list;//数据
    private List<Integer> heightList;//装产出的随机数
    private OnRecyclerItemClickListener mOnItemClickListener;//单击事件
    private onRecyclerItemLongClickListener mOnItemLongClickListener;//长按事件


    public MyRecyclerViewAdapter(Context context, List<taobaoBean.DataBean.ContentBean> list) {
        this.context = context;
        this.list = list;
        //记录为每个控件产生的随机高度,避免滑回到顶部出现空白
        heightList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            int height = new Random().nextInt(200) + 100;//[100,300)的随机数
            heightList.add(height);
        }
    }

//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == TYPE_ITEM) {
//            View view = LayoutInflater.from(context).inflate(R.layout.tianmao_item2, parent,
//                    false);
//            return new MyViewHolder(view);
//        } else if (viewType == TYPE_FOOTER) {
//            View view = LayoutInflater.from(context).inflate(R.layout.item_foot, parent,
//                    false);
//            return new FootViewHolder(view);
//        }
//        return null;
//        //找到item的布局
////        View view = LayoutInflater.from(context).inflate(R.layout.tianmao_item2, parent, false);
////        return new MyViewHolder(view);//将布局设置给holder
//    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.tianmao_item2, parent,
                    false);
            return new MyViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_foot, parent,
                    false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }


    @Override
    public int getItemCount() {
        return list.size() == 0 ? 0 : list.size() + 1;
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holders, int position) {
        //填充数据
        //由于需要实现瀑布流的效果,所以就需要动态的改变控件的高度了
        if (holders instanceof MyViewHolder) {
            MyViewHolder holder = (MyViewHolder) holders;
            taobaoBean.DataBean.ContentBean bean = list.get(position);
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


            //设置单击事件
            if (mOnItemClickListener != null) {
                holder.imgContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //这里是为textView设置了单击事件,回调出去
                        //mOnItemClickListener.onItemClick(v,position);这里需要获取布局中的position,不然乱序
                        mOnItemClickListener.onItemClick(v, holders.getLayoutPosition());
                    }
                });
            }
            //长按事件
            if (mOnItemLongClickListener != null) {
                holder.imgContent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //回调出去
                        mOnItemLongClickListener.onItemLongClick(v, holders.getLayoutPosition());
                        return true;//不返回true,松手还会去执行单击事件
                    }
                });
            }
        }
    }

    /**
     * 绑定视图到holder,就如同ListView的getView(),但是这里已经把复用实现了,我们只需要填充数据就行.
     * 由于在复用的时候都是调用该方法填充数据,但是上滑的时候,又会随机产生高度设置到控件上,这样当滑
     * 到顶部可能就会看到一片空白,因为后面随机产生的高度和之前的高度不一样,就不能填充屏幕了,所以
     * 需要记录每个控件产生的随机高度,然后在复用的时候再设置上去
     */
//    @Override
//    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        //填充数据
//        //由于需要实现瀑布流的效果,所以就需要动态的改变控件的高度了
//        taobaoBean.DataBean.ContentBean bean = list.get(position);
//        DecimalFormat df = new DecimalFormat("######0.00");
//        double ratio = 1;
//        if (bean.getType().equalsIgnoreCase("sharebuy")) {
//            if (bean.getSharebuy().width_height_ratio <= 0) {
//                ratio = 1;
//            } else {
//                ratio = 1 / bean.getSharebuy().width_height_ratio;
//                if (ratio > 1.75) {
//                    ratio = 1.75;
//                } else if (ratio < 0.6) {
//                    ratio = 0.6;
//                } else {
//                    String format = df.format(ratio);
//                    ratio = Double.valueOf(format);
//                }
//            }
//            Log.d("bilang", "sharebuy:" + position + " ratio:" + ratio);
//        } else {
//            if (bean.getVideo().ncover_ratio <= 0) {
//                ratio = 1;
//            } else {
//                ratio = 1 / bean.getVideo().ncover_ratio;
//                if (ratio > 1.75) {
//                    ratio = 1.75;
//                } else if (ratio < 0.6) {
//                    ratio = 0.6;
//                } else {
//                    String format = df.format(ratio);
//                    ratio = Double.valueOf(format);
//                }
//            }
//            Log.d("bilang", "video:--" + position + " ratio:" + ratio);
//        }
//        holder.imgContent.setHeightRatio(ratio);
//        if (bean.getType().equalsIgnoreCase("sharebuy")) {
//            taobaoBean.DataBean.ContentBean.ShareBuyBean shareBuyBean = bean.getSharebuy();
//            Glide.with(context).load(shareBuyBean.getCover_url() + "-mid2x.webp").into(holder.imgContent);
//        } else {
//            taobaoBean.DataBean.ContentBean.VideoBean videoBean = bean.getVideo();
//            Glide.with(context).load(videoBean.getCover_url() + "-mid2x.webp").into(holder.imgContent);
//        }
//
//
//        //设置单击事件
//        if (mOnItemClickListener != null) {
//            holder.imgContent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //这里是为textView设置了单击事件,回调出去
//                    //mOnItemClickListener.onItemClick(v,position);这里需要获取布局中的position,不然乱序
//                    mOnItemClickListener.onItemClick(v, holder.getLayoutPosition());
//                }
//            });
//        }
//        //长按事件
//        if (mOnItemLongClickListener != null) {
//            holder.imgContent.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    //回调出去
//                    mOnItemLongClickListener.onItemLongClick(v, holder.getLayoutPosition());
//                    return true;//不返回true,松手还会去执行单击事件
//                }
//            });
//        }
//    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        BLMeasureImageView imgContent;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgContent = (BLMeasureImageView) itemView
                    .findViewById(R.id.ivvideoicon);

        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }

    /**
     * 处理item的点击事件,因为recycler没有提供单击事件,所以只能自己写了
     */
    interface OnRecyclerItemClickListener {
        public void onItemClick(View view, int position);
    }

    /**
     * 长按事件
     */
    interface onRecyclerItemLongClickListener {
        public void onItemLongClick(View view, int position);
    }

    /**
     * 暴露给外面的设置单击事件
     */
    public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 暴露给外面的长按事件
     */
    public void setOnItemLongClickListener(onRecyclerItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 向指定位置添加元素
     */
//    public void addItem(int position, String value) {
//        if (position > list.size()) {
//            position = list.size();
//        }
//        if (position < 0) {
//            position = 0;
//        }
//        /**
//         * 使用notifyItemInserted/notifyItemRemoved会有动画效果
//         * 而使用notifyDataSetChanged()则没有
//         */
//        list.add(position, value);//在集合中添加这条数据
//        heightList.add(position, new Random().nextInt(200) + 100);//添加一个随机高度,会在onBindViewHolder方法中得到设置
//        notifyItemInserted(position);//通知插入了数据
//    }

    /**
     * 移除指定位置元素
     */
//    public String removeItem(int position) {
//        if (position > list.size() - 1) {
//            return null;
//        }
//        heightList.remove(position);//删除添加的高度
//        String value = list.remove(position);//所以还需要手动在集合中删除一次
//        notifyItemRemoved(position);//通知删除了数据,但是没有删除list集合中的数据
//        return value;
//    }

}
