//package com.handmark.pulltorefresh.library.internal;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Movie;
//import android.util.AttributeSet;
//import android.view.View.MeasureSpec;
//
///**
// * @author john 讀取gif文件
// *
// */
//public class MyGifView extends pl.droidsonroids.gif.GifImageView {
//
//	private long movieStart;
//	private Movie movie;
//	public String gifURL;
//
//	public void setDrawable(int drawable) {
//		if (movie == null) {
//			movie = Movie.decodeStream(getResources().openRawResource(drawable));
//		}
//	}
//
//	public MyGifView(Context context, AttributeSet attributeSet) {
//		super(context, attributeSet);
//		// 以文件流（InputStream）读取进gif图片资源
//		// movie=Movie.decodeStream(getResources().openRawResource(R.drawable.keyboard));
//	}
//
//	public MyGifView(Context context) {
//		super(context);
//	}
//
//	public MyGifView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//	}
//
//	public MyGifView(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
//		super(context, attrs, defStyle, defStyleRes);
//	}
//
//	@Override
//	protected void onDraw(Canvas canvas) {
//		long curTime = android.os.SystemClock.uptimeMillis();
//		// 第一次播放
//		if (movieStart == 0) {
//			movieStart = curTime;
//		}
//		if (movie != null) {
//			int duraction = movie.duration();
//			int relTime = (int) ((curTime - movieStart) % duraction);
//			movie.setTime(relTime);
//			movie.draw(canvas, 0, 0);
//			// 强制重绘
//			invalidate();
//		}
//		super.onDraw(canvas);
//	}
//
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
//
//		int originalHeight = MeasureSpec.getSize(heightMeasureSpec);
//
//		int calculatedHeight = originalWidth * 1 / 1;
//
//		int calculatedWidth = originalHeight * 1 / 1;
//
//		boolean lockY = false;
//		int finalWidth, finalHeight;
//		finalWidth = lockY ? calculatedWidth : originalWidth;
//		finalHeight = lockY ? originalHeight : calculatedHeight;
//
//		// if (reSize) {
//		super.onMeasure(
//				MeasureSpec.makeMeasureSpec(originalWidth, MeasureSpec.EXACTLY),
//				MeasureSpec.makeMeasureSpec(originalWidth, MeasureSpec.EXACTLY));
//		// } else {
//		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		// }
//	}
//}
