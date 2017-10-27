/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.lbl.codek3demo.taobao;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * Can display bitmap with rounded corners. This implementation works only with
 * ImageViews wrapped in ImageViewAware. <br />
 * This implementation is inspired by <a href=
 * "http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/"
 * > Romain Guy's article</a>. It rounds images using custom drawable drawing.
 * Original bitmap isn't changed. <br />
 * <br />
 * If this implementation doesn't meet your needs then consider <a
 * href="https://github.com/vinc3m1/RoundedImageView">RoundedImageView</a> or <a
 * href="https://github.com/Pkmmte/CircularImageView">CircularImageView</a>
 * projects for usage.
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.5.6
 */
public class RoundedBitmap implements BitmapDisplayer {

	protected final int leftTopCr;
	protected final int rightTopCr;
	protected final int leftBottomCr;
	protected final int rightBottomCr;
	protected final int margin;

	public RoundedBitmap(int leftTop, int leftBottom, int rightTop,
                         int rightBottom) {
		this.leftTopCr = leftTop;
		this.leftBottomCr = leftBottom;
		this.rightBottomCr = rightBottom;
		this.rightTopCr = rightTop;
		this.margin = 0;
	}

	@Override
	public void display(Bitmap bitmap, ImageAware imageAware,
			LoadedFrom loadedFrom) {
		if (!(imageAware instanceof ImageViewAware)) {
			throw new IllegalArgumentException(
					"ImageAware should wrap ImageView. ImageViewAware is expected.");
		}

		imageAware.setImageDrawable(new RoundedDrawable(bitmap, leftTopCr,
				leftBottomCr, rightTopCr, rightBottomCr, margin));
	}

	public static class RoundedDrawable extends Drawable {

		protected final float cornerRadius;
		protected final int margin;

		protected final RectF mRect = new RectF(), mBitmapRect;
		protected final BitmapShader bitmapShader;
		protected final Paint paint;
		protected final int lt, lb, rt, rb;

		public RoundedDrawable(Bitmap bitmap, int lt, int lb, int rt, int rb,
				int margin) {
			this.cornerRadius = Math.max(Math.max(lb, lt), Math.max(rb, rt));
			this.margin = margin;
			this.lt = lt;
			this.lb = lb;
			this.rt = rt;
			this.rb = rb;

			bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
					Shader.TileMode.CLAMP);
			mBitmapRect = new RectF(margin, margin, bitmap.getWidth() - margin,
					bitmap.getHeight() - margin);

			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setShader(bitmapShader);
		}

		@Override
		protected void onBoundsChange(Rect bounds) {
			super.onBoundsChange(bounds);
			mRect.set(margin, margin, bounds.width() - margin, bounds.height()
					- margin);

			// Resize the original bitmap to fit the new bound
			Matrix shaderMatrix = new Matrix();
			shaderMatrix.setRectToRect(mBitmapRect, mRect,
					Matrix.ScaleToFit.FILL);
			bitmapShader.setLocalMatrix(shaderMatrix);

		}

		@Override
		public void draw(Canvas canvas) {

			canvas.drawRoundRect(mRect, cornerRadius, cornerRadius, paint);

			float halfW = mRect.width() / 2;
			float halfH = mRect.height() / 2;

			// lt
			if (lt > 0) {
				canvas.drawRoundRect(new RectF(0, 0, halfW, halfH), lt, lt,
						paint);
			} else {
				canvas.drawRect(0, 0, halfW, halfH, paint);
			}

			// lb
			if (lb > 0) {
				canvas.drawRoundRect(new RectF(0, halfH, halfW, halfH * 2), lb,
						lb, paint);
			} else {
				canvas.drawRect(0, halfH, halfW, halfH * 2, paint);
			}

			// rt
			if (rt > 0) {
				canvas.drawRoundRect(new RectF(halfW, 0, halfW * 2, halfH), rt,
						rt, paint);
			} else {
				canvas.drawRect(halfW, 0, halfW * 2, halfH, paint);
			}

			// rb

			if (rb > 0) {
				canvas.drawRoundRect(new RectF(halfW, halfH, halfW * 2,
						halfH * 2), rb, rb, paint);
			} else {
				canvas.drawRect(halfW, halfH, halfW * 2, halfH * 2, paint);
			}

		}

		@Override
		public int getOpacity() {
			return PixelFormat.TRANSLUCENT;
		}

		@Override
		public void setAlpha(int alpha) {
			paint.setAlpha(alpha);
		}

		@Override
		public void setColorFilter(ColorFilter cf) {
			paint.setColorFilter(cf);
		}
	}
}
