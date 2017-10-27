package com.lbl.codek3demo.taobao;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lbl.codek3demo.R;
import com.lbl.codek3demo.taobao.bean.taobaoBean;
import com.lbl.codek3demo.util.StringUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import static com.lbl.codek3demo.R.id.ivReadStatus;
import static com.lbl.codek3demo.R.id.tvPublishTime;

/**
 * author：libilang
 * time: 17/8/29 16:02
 * 邮箱：libi_lang@163.com
 */

public class TaobaoSearchAdapter extends BaseAdapter {
    private static final String TAG = "GoodsPullFallAdapter";
    private final int VIEW_CONTENT = 0;
    private final int VIEW_SHAREBUY = 1;

    private LayoutInflater mLayoutInflater;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    public List<taobaoBean.DataBean.ContentBean> data;
    private Random mRandom;
    public Context context;
    private DisplayImageOptions opt, headerOpt;

    public TaobaoSearchAdapter(Context context, List<taobaoBean.DataBean.ContentBean> data) {
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        this.context = context;
        this.data = data;
        opt = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
        headerOpt = opt;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        LinearLayout layout_search_content, isLikeLayout;
        RelativeLayout user_layout;
        BLMeasureImageView imgContent;
        TextView tvPlayTime, mTvVideoTitle, mTvVideoContent, mTvPlayCount, tvPublishTime, mTvAuthorName;
        ImageView mIvVideoIcon, ivReadStatus, ivItemFlag;
        ImageView islike;
        CircleImageView userhead;
        View diveder;

    }

    @Override
    public View getView(final int position, View convertView,
                        final ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.tianmao_item,
                    parent, false);
            vh = new ViewHolder();
            vh.imgContent = (BLMeasureImageView) convertView
                    .findViewById(R.id.ivvideoicon);
            vh.diveder = convertView.findViewById(R.id.diveder);
            vh.user_layout = (RelativeLayout) convertView.findViewById(R.id.user_layout);
            vh.mTvVideoTitle = (TextView) convertView.findViewById(R.id.tvVideoTitle);
            vh.mTvVideoContent = (TextView) convertView.findViewById(R.id.tvVideoDcip);
            vh.mTvPlayCount = (TextView) convertView.findViewById(R.id.tvPlayCount);
            vh.tvPublishTime = (TextView) convertView.findViewById(tvPublishTime);
            vh.mTvAuthorName = (TextView) convertView.findViewById(R.id.tvAuthorName);
            vh.mIvVideoIcon = (ImageView) convertView.findViewById(R.id.ivVideoIcon);
            vh.ivReadStatus = (ImageView) convertView.findViewById(ivReadStatus);
            vh.userhead = (CircleImageView) convertView.findViewById(R.id.user_head);
            vh.isLikeLayout = (LinearLayout) convertView.findViewById(R.id.tvPlayCount_layout);
            vh.islike = (ImageView) convertView.findViewById(R.id.islike);
            vh.layout_search_content = (LinearLayout) convertView.findViewById(R.id.layout_search_content_top);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        taobaoBean.DataBean.ContentBean bean = data.get(position);
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
        vh.imgContent.setHeightRatio(ratio);
        vh.isLikeLayout.setVisibility(View.GONE);
        //晒单
        if (bean.getType().equalsIgnoreCase("sharebuy")) {
            taobaoBean.DataBean.ContentBean.ShareBuyBean shareBuyBean = bean.getSharebuy();
            if (shareBuyBean != null) {
                Glide.with(context).load(shareBuyBean.getCover_url()+"-mid2x.webp").into(vh.imgContent);
                if (shareBuyBean.getUser() != null)
                    Glide.with(context).load(shareBuyBean.getUser().getAvatar()+"-icon2x.jpg").into(vh.userhead);
                String title = shareBuyBean.getTitle();
                String description = shareBuyBean.getDcrp();
                if (!StringUtil.isStringEmpty(title)) {
                    vh.mTvVideoTitle.setText(title);
                    vh.mTvVideoTitle.setVisibility(View.VISIBLE);
                } else {
                    vh.mTvVideoTitle.setVisibility(View.GONE);
                }
                if (!StringUtil.isStringEmpty(description)) {
                    vh.mTvVideoContent.setText(description);
                    vh.mTvVideoContent.setVisibility(View.VISIBLE);
                } else {
                    vh.mTvVideoContent.setVisibility(View.GONE);
                }
                if (StringUtil.isStringEmpty(title) && StringUtil.isStringEmpty(description)) {
                    vh.diveder.setVisibility(View.GONE);
                } else {
                    vh.diveder.setVisibility(View.VISIBLE);
                }
                vh.mTvAuthorName.setText(StringUtil.getCorrectString(shareBuyBean.getUser().getNick()));
                int playCount = shareBuyBean.getLike_count();
                String playFinalString;
                if (playCount > 10000) {
                    double doublePlayCount = playCount * 0.0001;
                    BigDecimal bg = new BigDecimal(doublePlayCount).setScale(1, RoundingMode.UP);
                    double finalDoubleCount = bg.doubleValue();
                    playFinalString = finalDoubleCount + "万";
                } else {
                    playFinalString = playCount + "";
                }
                vh.mTvPlayCount.setText(StringUtil.getCorrectString(playFinalString));
                if (shareBuyBean.isIs_like()) {
                    vh.islike.setImageResource(R.drawable.graphic_detail_praise_p);
                    vh.mTvPlayCount.setTextColor(context.getResources().getColor(R.color.xhc_red));
                } else {
                    vh.mTvPlayCount.setTextColor(context.getResources().getColor(R.color.xhc_black_second));
                }

            }
        } else {
            //视频
            final taobaoBean.DataBean.ContentBean.VideoBean videoBean = bean.getVideo();
            if (videoBean != null) {
                Glide.with(context).load(videoBean.getCover_url()+"-mid2x.webp").into(vh.imgContent);
                if (videoBean.getUser() != null)
                    Glide.with(context).load(videoBean.getUser().getAvatar()+"-icon2x.jpg").into(vh.userhead);
                String title = videoBean.getTitle();
                String description = videoBean.getDcrp();
                if (!StringUtil.isStringEmpty(title)) {
                    vh.mTvVideoTitle.setText(title);
                    vh.mTvVideoTitle.setVisibility(View.VISIBLE);
                } else {
                    vh.mTvVideoTitle.setVisibility(View.GONE);
                }
                if (!StringUtil.isStringEmpty(description)) {
                    vh.mTvVideoContent.setText(description);
                    vh.mTvVideoContent.setVisibility(View.VISIBLE);
                } else {
                    vh.mTvVideoContent.setVisibility(View.GONE);
                }
                if (StringUtil.isStringEmpty(title) && StringUtil.isStringEmpty(description)) {
                    vh.diveder.setVisibility(View.GONE);
                } else {
                    vh.diveder.setVisibility(View.VISIBLE);
                }
                vh.mTvAuthorName.setText(StringUtil.getCorrectString(videoBean.getUser().getNick()));
                int playCount = videoBean.getLike_count();
                String playFinalString;
                if (playCount > 10000) {
                    double doublePlayCount = playCount * 0.0001;
                    BigDecimal bg = new BigDecimal(doublePlayCount).setScale(1, RoundingMode.UP);
                    double finalDoubleCount = bg.doubleValue();
                    playFinalString = finalDoubleCount + "万";
                } else {
                    playFinalString = playCount + "";
                }
                vh.mTvPlayCount.setText(StringUtil.getCorrectString(playFinalString));
                vh.mTvPlayCount.setTextColor(context.getResources().getColor(R.color.xhc_red));
                vh.islike.setImageResource(R.drawable.graphic_detail_praise_p);
            }
        }


        return convertView;
    }
}
