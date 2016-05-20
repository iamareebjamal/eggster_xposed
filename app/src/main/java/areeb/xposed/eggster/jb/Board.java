/*
*  Jelly Bean Game
*  Copyright (C) 2012 The Android Open Source Project, Modified by George Piskas and further edited by Areeb Jamal
*
*  This program is free software; you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation; either version 2 of the License, or
*  (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License along
*  with this program; if not, write to the Free Software Foundation, Inc.,
*  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
*  Contact: geopiskas@gmail.com & jamal.areeb@gmail.com
*/

package areeb.xposed.eggster.jb;

import java.util.Random;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import areeb.xposed.eggster.R;

import com.nineoldandroids.animation.TimeAnimator;
import com.nineoldandroids.view.ViewHelper;


public class Board extends RelativeLayout {
	
    static int BEANS[] = {
        R.drawable.redbean0,
        R.drawable.redbean0,
        R.drawable.redbean0,
        R.drawable.redbean0,
        R.drawable.redbean1,
        R.drawable.redbean1,
        R.drawable.redbean2,
        R.drawable.redbean2,
        R.drawable.redbeandroid,
      };

      static int COLORS[] = {
          0xFF00CC00,
          0xFFCC0000,
          0xFF0000CC,
          0xFFFFFF00,
          0xFFFF8000,
          0xFF00CCFF,
          0xFFFF0080,
          0xFF8000FF,
          0xFFFF8080,
          0xFF8080FF,
          0xFFB0C0D0,
          0xFFDDDDDD,
          0xFF333333,
      };

      static int NUM_BEANS = 20;
      static float MIN_SCALE = 0.2f;
      static float MAX_SCALE = 1f;

      static float LUCKY = 0.001f;

  	private int MAX_RADIUS = (int) (576 * MAX_SCALE);
      
	public Board(Context context, AttributeSet as) {
		super(context, as);
		setWillNotDraw(true);
		String numBeans = context.getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getString("number_of_jb", "20");
		
		try{
		int temp = Integer.parseInt(numBeans);
		
		if (numBeans != null && numBeans.length() > 0 && numBeans.matches("\\d*") && Integer.parseInt(numBeans) > 0) {
			
			NUM_BEANS = temp;
			
		}
		
		} catch (NumberFormatException e){
			
			NUM_BEANS = 20;
			e.printStackTrace();
			
		}
		
		
		
		
	}
	
	private Random sRNG = new Random();

	private float lerp(float a, float b, float f) {
		return (b - a) * f + a;
	}

	private float randfrange(float a, float b) {
		return lerp(a, b, sRNG.nextFloat());
	}

	private int randsign() {
		return sRNG.nextBoolean() ? 1 : -1;
	}

	private boolean flip() {
		return sRNG.nextBoolean();
	}

	private float mag(float x, float y) {
		return (float) Math.sqrt(x * x + y * y);
	}

	private float clamp(float x, float a, float b) {
		return ((x < a) ? a : ((x > b) ? b : x));
	}

	private int pickInt(int[] array) {
		if (array.length == 0)
			return 0;
		return array[sRNG.nextInt(array.length)];
	}

	private class Head extends ImageView {
		
		private float x, y, a;

		private float va;
		private float vx, vy;

		private float r;

		private float z;

		private int h, w;

		private boolean grabbed;
		private float grabx, graby;
		private float grabx_offset, graby_offset;

		public Rect mRect;

		private Head(Context context, AttributeSet as) {
			super(context, as);
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		private void pickBean() {
			int beanId; 
			if (randfrange(0,1) <= LUCKY) {
                beanId = R.drawable.jandycane;
            } else {
            	beanId = pickInt(BEANS);
            }
			BitmapDrawable head = (BitmapDrawable) getContext().getResources().getDrawable(beanId);
			Bitmap bits = head.getBitmap();
			h = bits.getHeight();
			w = bits.getWidth();
			setImageResource(beanId);
			
            Paint pt = new Paint();
            final int color = pickInt(COLORS);
            ColorMatrix CM = new ColorMatrix();
            float[] M = CM.getArray();
            // we assume the color information is in the red channel
            /* R */ M[0]  = (float)((color & 0x00FF0000) >> 16) / 0xFF;
            /* G */ M[5]  = (float)((color & 0x0000FF00) >> 8)  / 0xFF;
            /* B */ M[10] = (float)((color & 0x000000FF))       / 0xFF;
            pt.setColorFilter(new ColorMatrixColorFilter(M));
            if (Build.VERSION.SDK_INT >= 11) {
                setLayerType(LAYER_TYPE_HARDWARE, pt);
            } else {
            	setColorFilter(new ColorMatrixColorFilter(M));
            }
		}
		
		@Override
		public void getHitRect(Rect outRect) {
		    if (mRect == null){
		        super.getHitRect(outRect);
		    } else {
		        outRect.set(mRect);
		    }
		}
		

		private void reset() {
			pickBean();

			final float scale = lerp(MIN_SCALE, MAX_SCALE, z);
			ViewHelper.setScaleX(this, scale);
			ViewHelper.setScaleY(this, scale);


			r = 0.3f * Math.max(h, w) * scale;

			a = randfrange(0, 360);
			va = randfrange(-30, 30);

			vx = randfrange(-40, 40) * z;
			vy = randfrange(-40, 40) * z;
			final float boardh = boardHeight;
			final float boardw = boardWidth;
			if (flip()) {
				x = (vx < 0 ? boardw + 2 * r : -r * 4f);
				y = (randfrange(0, boardh - 3 * r) * 0.5f + ((vy < 0) ? boardh * 0.5f
						: 0));
			} else {
				y = (vy < 0 ? boardh + 2 * r : -r * 4f);
				x = (randfrange(0, boardw - 3 * r) * 0.5f + ((vx < 0) ? boardw * 0.5f
						: 0));
			}
		}

		private void update(float dt) {
			if (grabbed) {
				vx = (vx * 0.75f) + ((grabx - x) / dt) * 0.25f;
				x = grabx;
				vy = (vy * 0.75f) + ((graby - y) / dt) * 0.25f;
				y = graby;
			} else {
				x = (x + vx * dt);
				y = (y + vy * dt);
				a = (a + va * dt);
			}
		}
		

		@Override
		public boolean onTouchEvent(MotionEvent e) {
			switch (e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				grabbed = true;
				grabx_offset = e.getRawX() - x;
				graby_offset = e.getRawY() - y;
				va = 0;
			case MotionEvent.ACTION_MOVE:
				grabx = e.getRawX() - grabx_offset;
				graby = e.getRawY() - graby_offset;
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				grabbed = false;
				float a = randsign() * clamp(mag(vx, vy) * 0.33f, 0, 1080f);
				va = randfrange(a * 0.5f, a);
				break;
			}
			return true;
		}
	}
	
	private TimeAnimator mAnim;
	private int boardWidth;
	private int boardHeight;
	
	private void reset() {
		removeAllViews();
		for (int i = 0; i < NUM_BEANS; i++) {
			Head nv = new Head(getContext(), null);
			nv.z = ((float) i / NUM_BEANS);
			nv.z *= nv.z;
			nv.reset();
			nv.x = (randfrange(0, boardWidth));
			nv.y = (randfrange(0, boardHeight));
			addView(nv);
		}

		if (mAnim != null) {
			mAnim.cancel();
		}
		mAnim = new TimeAnimator();
		mAnim.setTimeListener(new TimeAnimator.TimeListener() {

			@Override
			public void onTimeUpdate(TimeAnimator animation, long totalTime,
					long deltaTime) {

				for (int i = 0; i < getChildCount(); i++) {
					View v = getChildAt(i);
					if (!(v instanceof Head))
						continue;
					Head nv = (Head) v;
					nv.update(deltaTime / 1000f);

					ViewHelper.setRotation(nv, nv.a);
					
					ViewHelper.setX(nv, nv.x - ViewHelper.getPivotX(nv));
					ViewHelper.setY(nv, nv.y - ViewHelper.getPivotY(nv));
			        RectF rect = new RectF();
			        rect.top = 0;
			        rect.bottom = (float) nv.h; 
			        rect.left = 0; 
			        rect.right = (float) nv.w;  
			        rect.offset((float) ViewHelper.getX(nv), (float) ViewHelper.getY(nv));

			        if (nv.mRect == null) nv.mRect = new Rect();
			        rect.round(nv.mRect);
			        
					if (nv.x < -MAX_RADIUS || nv.x > boardWidth + MAX_RADIUS
							|| nv.y < -MAX_RADIUS
							|| nv.y > boardHeight + MAX_RADIUS) {
						nv.reset();
					}
				}
			}
		});
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		boardWidth = w;
		boardHeight = h;
	}

	public void startAnimation() {
		stopAnimation();
		if (mAnim == null) {
			post(new Runnable() {
				public void run() {
					reset();
					startAnimation();
				}
			});
		} else {
			mAnim.start();
		}
	}

	public void stopAnimation() {
		if (mAnim != null)
			mAnim.cancel();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		stopAnimation();
	}

	@Override
	public boolean isOpaque() {
		return false;
	}
}