package com.lbl.codek3demo.taobao;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.lbl.codek3demo.R;
import com.lbl.codek3demo.util.StringUtil;
import com.lbl.codek3demo.util.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;
import java.util.Random;

import static com.lbl.codek3demo.R.id.ivItemFlag;


/**
 */

/**
 * author：libilang
 * time: 17/8/6 11:10
 * 邮箱：libi_lang@163.com
 * 视频详情页-- 猜你喜欢 视频瀑布流adapter
 */
public class VideoDetailVideoPullFallAdapter extends BaseAdapter {
    private static final String TAG = "VideoSharebuyPullFallAdapter";
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    public List<VideoRecommendBean.VideosInfoEntity> data;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private Context context;
    private DisplayImageOptions opt, headerOpt;
    private int Type = 1;//1 是视频详情页的猜你喜欢视频  2是晒单详情页的猜你喜欢晒单

    public VideoDetailVideoPullFallAdapter(Context context, int type, List<VideoRecommendBean.VideosInfoEntity> data) {
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        this.context = context;
        this.data = data;
        this.Type = type;
        int radio = Util.dipToPX(context, 5);
        opt = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .displayer(new RoundedBitmap(radio, 0, radio, 0))
                .showImageOnLoading(R.mipmap.ic_launcher_round)
                .showImageOnFail(R.mipmap.ic_launcher_round)
                .showImageForEmptyUri(R.mipmap.ic_launcher_round)
                .build();
        headerOpt=opt;

    }

    @Override
    public View getView(final int position, View convertView,
                        final ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.pull_video_sharebuy_item,
                    parent, false);
            vh = new ViewHolder();
            vh.imgContent = (DynamicHeightImageView) convertView
                    .findViewById(R.id.ivVideoIcon);
            vh.title = (TextView) convertView.findViewById(R.id.title);
            vh.content = (TextView) convertView.findViewById(R.id.content);
            vh.likecount = (TextView) convertView.findViewById(R.id.play_count);
            vh.username = (TextView) convertView.findViewById(R.id.user_name);
            vh.imageCover = (ImageView) convertView.findViewById(R.id.ivVideoIcon);
            vh.isvideo = (ImageView) convertView.findViewById(ivItemFlag);
            vh.userhead = (ImageView) convertView.findViewById(R.id.user_head);
            vh.layout_search_content = (LinearLayout) convertView.findViewById(R.id.layout_search_content);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        double positionHeight = getPositionRatio(position);

        if (position % 2 == 0) {
            vh.imgContent.setHeightRatio(1);
        } else {
            vh.imgContent.setHeightRatio(0.637);
        }
        if (Type == 1) {//视频详情页猜你喜欢
            final VideoRecommendBean.VideosInfoEntity video1 = data.get(position);
            if (video1 != null) {
                ImageLoader.getInstance().displayImage(video1.getImgURL(),
                        vh.imageCover, opt);
                ImageLoader.getInstance().displayImage(video1.getImgURL(), vh.userhead,
                        headerOpt);
                vh.username.setText(video1.getUsername());
                vh.title.setText(StringUtil.getCorrectString(video1.getTitle()));
                if (!TextUtils.isEmpty(video1.dcrp)) {
                    vh.content.setText(video1.dcrp);
                }
                vh.likecount.setText(video1.getPlay()+"");
            }
        } else {//晒单详情页猜你喜欢

        }

        return convertView;
    }

    /**
     * 删除线
     */
    private void addStrikeSpan(String prePrice, TextView prePriceTextView) {
        if (prePrice != null) {
            SpannableString sp = new SpannableString(prePrice);
            sp.setSpan(new StrikethroughSpan(), 0, prePrice.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            prePriceTextView.setText(sp);
        }
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;

    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        try {
            return position;
        }catch (Exception e){
            return position;
        }
    }

    static class ViewHolder {
        LinearLayout layout_search_content;
        DynamicHeightImageView imgContent;
        TextView title, content, likecount, username;
        ImageView imageCover, isvideo;
        ImageView userhead;

    }
}
