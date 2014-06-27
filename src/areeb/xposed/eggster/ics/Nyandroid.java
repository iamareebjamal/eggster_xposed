/*);
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package areeb.xposed.eggster.ics;





import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.*;
import android.graphics.drawable.AnimationDrawable;
import android.os.*;
import android.util.AttributeSet;
import android.view.*;
import android.widget.*;
import areeb.xposed.eggster.R;

import java.util.Random;

import com.nineoldandroids.animation.TimeAnimator;
import com.nineoldandroids.view.ViewHelper;



public class Nyandroid extends Activity {
    final static boolean DEBUG = false;

    
	public static class Board extends FrameLayout
    {
        public static final boolean FIXED_STARS = true;
        
        public int NUM_CATS = 10;
        
        
        
        

        static Random sRNG = new Random();

        static float lerp(float a, float b, float f) {
            return (b-a)*f + a;
        }

        static float randfrange(float a, float b) {
            return lerp(a, b, sRNG.nextFloat());
        }

        static int randsign() {
            return sRNG.nextBoolean() ? 1 : -1;
        }

        static <E> E pick(E[] array) {
            if (array.length == 0) return null;
            return array[sRNG.nextInt(array.length)];
        }

        public class FlyingCat extends ImageView {
            public static final float VMAX = 1000.0f;
            public static final float VMIN = 100.0f;

            public float v, vr;

            public float dist;
            public float z;

            public ComponentName component;

            
			public FlyingCat(Context context, AttributeSet as) {
                super(context, as);
                setImageResource(R.drawable.nyandroid_anim); // @@@

                if (DEBUG) setBackgroundColor(0x80FF0000);
            }

            
			@SuppressLint("DefaultLocale")
			public String toString() {
                return String.format("<cat (%.1f, %.1f) (%d x %d)>",
                    ViewHelper.getX(this), ViewHelper.getY(this), getWidth(), getHeight());
            }

            
			
			public void reset() {
                final float scale = lerp(0.1f,2f,z);
                ViewHelper.setScaleX(this, scale);
                ViewHelper.setScaleY(this, scale);
                //setScaleX(scale); setScaleY(scale);
                
                ViewHelper.setX(this, -scale*getWidth()+1);
                ViewHelper.setY(this, randfrange(0, Board.this.getHeight()-scale*getHeight()));

                //setX(-scale*getWidth()+1);
                //setY(randfrange(0, Board.this.getHeight()-scale*getHeight()));
                v = lerp(VMIN, VMAX, z);

                dist = 0;

//                android.util.Log.d("Nyandroid", "reset cat: " + this);
            }

            
			public void update(float dt) {
                dist += v * dt;
                ViewHelper.setX(this, ViewHelper.getX(this) + v * dt);
            }
        }

        TimeAnimator mAnim;

        public Board(Context context, AttributeSet as) {
            super(context, as);
            
            String numCats = context.getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getString("number_of_cats", "10");
            
            try{
            	
            int temp = Integer.parseInt(numCats);
            
            if (numCats != null && numCats.length() > 0 && numCats.matches("\\d*") && Integer.parseInt(numCats) > 0) {
    			
    			NUM_CATS = temp;
    			
    		}
            
            } catch (NumberFormatException e){
            	
            	NUM_CATS = 10;
            	e.printStackTrace();
            	
            }
            
            
    		

            //setLayerType(View.LAYER_TYPE_HARDWARE, null);
            //setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            setBackgroundColor(0xFF003366);
        }

        
		private void reset() {
//            android.util.Log.d("Nyandroid", "board reset");
            removeAllViews();

            final ViewGroup.LayoutParams wrap = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

            if (FIXED_STARS) {
                for(int i=0; i<20; i++) {
                    ImageView fixedStar = new ImageView(getContext(), null);
                    if (DEBUG) fixedStar.setBackgroundColor(0x8000FF80);
                    fixedStar.setImageResource(R.drawable.star_anim); // @@@
                    addView(fixedStar, wrap);
                    final float scale = randfrange(0.1f, 1f);
                    ViewHelper.setScaleX(fixedStar, scale);
                    ViewHelper.setScaleY(fixedStar, scale);
                    ViewHelper.setX(fixedStar, randfrange(0, getWidth()));
                    ViewHelper.setY(fixedStar, randfrange(0, getHeight()));
                    //fixedStar.setScaleX(scale);
                    //fixedStar.setScaleY(scale);
                    //fixedStar.setX(randfrange(0, getWidth()));
                    //fixedStar.setY(randfrange(0, getHeight()));
                    final AnimationDrawable anim = (AnimationDrawable) fixedStar.getDrawable();
                    postDelayed(new Runnable() { 
                        
						public void run() {
                            anim.start();
                        }}, (int) randfrange(0, 1000));
                }
            }
            
            

            for(int i=0; i<NUM_CATS; i++) {
 
            	
                FlyingCat nv = new FlyingCat(getContext(), null);
                addView(nv, wrap);
                nv.z = ((float)i/NUM_CATS);
                nv.z *= nv.z;
                nv.reset();
                ViewHelper.setX(nv, randfrange(0,Board.this.getWidth()));
                //nv.setX(randfrange(0,Board.this.getWidth()));
                final AnimationDrawable anim = (AnimationDrawable) nv.getDrawable();
                postDelayed(new Runnable() { 
                    public void run() {
                        anim.start();
                    }}, (int) randfrange(0, 1000));
            }

            if (mAnim != null) {
                mAnim.cancel();
            }
            mAnim = new TimeAnimator();
            mAnim.setTimeListener(new TimeAnimator.TimeListener() {
				public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
                    //setRotation(totalTime * 0.01f); // not as cool as you would think
//                    android.util.Log.d("Nyandroid", "t=" + totalTime);

                    for (int i=0; i<getChildCount(); i++) {
                        View v = getChildAt(i);
                        if (!(v instanceof FlyingCat)) continue;
                        FlyingCat nv = (FlyingCat) v;
                        nv.update(deltaTime / 1000f);
                        final float catWidth = nv.getWidth() * ViewHelper.getScaleX(nv);
                        final float catHeight = nv.getHeight() * ViewHelper.getScaleY(nv);
                        if (   ViewHelper.getX(nv) + catWidth < -2
                            || ViewHelper.getX(nv) > getWidth() + 2
                            || ViewHelper.getY(nv) + catHeight < -2
                            || ViewHelper.getY(nv) > getHeight() + 2)
                        {
                            nv.reset();
                        }
                    }
                }
            });
        }

        
		public void startAnimation() {
            stopAnimation();
            if (mAnim == null) {
                post(new Runnable() { public void run() {
                    reset();
                    startAnimation();
                } });
            } else {
                mAnim.start();
            }
        }

        
		public void stopAnimation() {
            if (mAnim != null) mAnim.cancel();
        }

        
		@Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (mAnim != null) mAnim.cancel();
        }

        @Override
        public boolean isOpaque() {
            return true;
        }
    }

    private Board mBoard;


	
	
	
	@SuppressLint("NewApi")
	@Override
    public void onStart() {
		String lol = getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getString("ics_sysui", getString(R.string.pref_none));
        getWindow().addFlags(
                  WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                );
		
        super.onStart();
        if (lol.equals(getString(R.string.pref_none)))
        	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (lol.equals(getString(R.string.pref_translucent))){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        
        mBoard = new Board(this, null);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1 && lol.equals(getString(R.string.pref_none))){
        mBoard.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        if (lol.equals(getString(R.string.pref_immerge))) {
       	 getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
                  | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                  );
  		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
           mBoard.setSystemUiVisibility(
                   View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                   | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                   | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                   | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION 		// hide nav bar
                   | View.SYSTEM_UI_FLAG_FULLSCREEN 			// hide status bar
                   | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); 	// immerge
       } 
        
        if (lol.equals(getString(R.string.pref_translucent))) {
        	setTheme(R.style.Wallpaper_TranslucentDecor);
        }
        
        setContentView(mBoard);
    } 

	
    @Override
    public void onPause() {
        super.onPause();
        mBoard.stopAnimation();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBoard.startAnimation();
    }
}