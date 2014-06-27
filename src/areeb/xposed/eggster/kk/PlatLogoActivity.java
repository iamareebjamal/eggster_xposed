/*
 * Copyright (C) 2010 The Android Open Source Project
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

package areeb.xposed.eggster.kk;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.*;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import areeb.xposed.eggster.R;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;


public class PlatLogoActivity extends Activity {
	FrameLayout mContent;
	int mCount;
	int MAX_CLICKS = 6;
	int LETTER_SIZE = 300;
	int TEXT_SIZE = 30;
	Interpolator whichinterp;
	final Handler mHandler = new Handler();
	static final int BGCOLOR = 0xffed1d24;
	@SuppressLint({ "NewApi", "InlinedApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		DisplayMetrics metrics	= new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		Typeface 			bold			= Typeface.createFromAsset(getAssets(), "Roboto-Bold.ttf");
		Typeface			light			= Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
		SharedPreferences	pref			= getSharedPreferences("preferenceggs", Context.MODE_PRIVATE);
        String				kkLetter		= pref.getString("kk_letter", getString(R.string.pref_default_kk_letter));
        String				kkText			= pref.getString("kk_text", getString(R.string.pref_default_kk_text));
        final String		interpol		= pref.getString("kk_interpolator", getString(R.string.pref_def_anim));
        String				kkClicks		= pref.getString("kk_clicks", getString(R.string.pref_default_kk_clicks));
        int					kkSize			= pref.getInt("kk_letter_size", 300);
        int					kkTextSize		= pref.getInt("kk_text_size", 30);
		String				leMode 			= pref.getString("kk_sysui", getString(R.string.pref_none));
		
		if (leMode.equals(getString(R.string.pref_none)))
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        try{
        	
            int temp = Integer.parseInt(kkClicks);
            
            if (kkClicks != null && kkClicks.length() > 0 && kkClicks.matches("\\d*") && Integer.parseInt(kkClicks) > 0) {
    			
    			MAX_CLICKS = temp;
    			
    		}
            
            } catch (NumberFormatException e){
            	
            	MAX_CLICKS = 6;
            	e.printStackTrace();
            	
            }
        

        
        try{
        	
        
        
        if (String.valueOf(kkSize) != null && kkSize > 0 && String.valueOf(kkSize).matches("\\d*") && String.valueOf(kkSize).length() > 0) {
			
        	LETTER_SIZE = kkSize;
			
		}
        
        } catch (NumberFormatException e){
        	
        	LETTER_SIZE = 300;
        	e.printStackTrace();
        	
        }
        
        try{
        
        if (String.valueOf(kkTextSize) != null && kkTextSize > 0 && String.valueOf(kkTextSize).matches("\\d*") && String.valueOf(kkTextSize).length() > 0) {
			
        	TEXT_SIZE = kkTextSize;
			
		}
        
        } catch (NumberFormatException e){
        	
        	TEXT_SIZE = 30;
        	e.printStackTrace();
        	
        }
            

		mContent = new FrameLayout(this);
		mContent.setBackgroundColor(0xC0000000);

		final LayoutParams lp = new LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;

		final ImageView logo = new ImageView(this);
		logo.setImageResource(R.drawable.platlogo_kk);
		logo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		logo.setVisibility(View.INVISIBLE);

		final View bg = new View(this);
		bg.setBackgroundColor(BGCOLOR);
		ViewHelper.setAlpha(bg, 0f);

		final TextView letter = new TextView(this);

		letter.setTypeface(bold);
		letter.setTextSize(LETTER_SIZE);
		letter.setTextColor(0xFFFFFFFF);
		letter.setGravity(Gravity.CENTER);
		letter.setText(kkLetter);

		final int p = (int) (4 * metrics.density);

		final TextView tv = new TextView(this);
        if (light != null) tv.setTypeface(light);
        tv.setTextSize(TEXT_SIZE);
        tv.setPadding(p, p, p, p);
        tv.setTextColor(0xFFFFFFFF);
        tv.setGravity(Gravity.CENTER);
        tv.setText(kkText);
        tv.setVisibility(View.INVISIBLE);
        
		mContent.addView(bg);
		mContent.addView(letter, lp);
		mContent.addView(logo, lp);
		
	
			final LayoutParams lapar = new LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lapar.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		if (leMode.equals(getString(R.string.pref_none)))
		lapar.bottomMargin = 10 * p; else lapar.bottomMargin = 15 * p;

		mContent.addView(tv, lapar);
		mContent.setOnClickListener(new View.OnClickListener() {
			int clicks;
			int dur = 700;
			@Override
            public void onClick(View v) {
				if (interpol == "Accelerate"){
					whichinterp = new AccelerateInterpolator();
					} else if (interpol.equals("Decelerate")) {
						whichinterp = new DecelerateInterpolator();
					} else if (interpol.equals("Accelerate + Decelerate")) {
						whichinterp = new AccelerateDecelerateInterpolator();
						dur = 1000;
					} else if (interpol.equals("Anticipate")){
						whichinterp = new AnticipateInterpolator((float) 0.8);
						dur = 1500;
					} else if (interpol.equals("Overshoot")){
						whichinterp = new OvershootInterpolator((float) 0.95);
						dur = 1500;
					} else if (interpol.equals("Anticipate + Overshoot")){
						whichinterp = new AnticipateOvershootInterpolator((float) 0.9);
						dur = 1500;
					} else if (interpol.equals("Linear")){
						whichinterp = new LinearInterpolator();
					} else if (interpol.equals("Bounce")){
						whichinterp = new BounceInterpolator();
						dur = 1800;
					}
                clicks++;
                if (clicks >= MAX_CLICKS) {
                    mContent.performLongClick();
                    return;
                }
                ViewPropertyAnimator.animate(letter).cancel();
                final float offset = (int) ViewHelper.getRotation(letter) % 360;
                ViewPropertyAnimator.animate(letter)
                    .rotationBy((Math.random() > 0.5f ? 360 : -360) - offset)
                    .setInterpolator(whichinterp)
                    .setDuration(dur).start();
            }
        });


		
		mContent.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				boolean check = true;
				if (logo.getVisibility() != 0) {
					ViewHelper.setScaleX(bg, 0.01F);
					ViewPropertyAnimator.animate(bg).alpha(1.0F).scaleX(1.0F)
							.setStartDelay(500L).start();
					ViewPropertyAnimator.animate(letter).alpha(0.0F)
							.scaleY(0.5F).scaleX(0.5F).rotationBy(360.0F)
							.setInterpolator(new AccelerateInterpolator())
							.setDuration(1000L).start();
					ViewHelper.setAlpha(logo, 0.0F);
					logo.setVisibility(0);
					ViewHelper.setScaleX(logo, 0.5F);
					ViewHelper.setScaleY(logo, 0.5F);
					ViewPropertyAnimator
							.animate(logo)
							.scaleX(1.0F)
							.alpha(1.0F)
							.scaleY(1.0F)
							.setDuration(1000L)
							.setInterpolator(new AnticipateOvershootInterpolator())
							.setStartDelay(500L)
							.start();
					ViewHelper.setAlpha(tv, 0F);
					ViewPropertyAnimator.animate(tv).alpha(1f)
							.setDuration(1000).setStartDelay(1000).start();

					//removed original API methods from Giupy as they were already handled by nineolddroid helpers
                    
                    tv.setVisibility(View.VISIBLE);  //Noob Error
					check = true;
				}
				return check;
			}
		});

		logo.setOnLongClickListener(new View.OnLongClickListener() {
			@SuppressLint("InlinedApi")
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public boolean onLongClick(View v) {

				if (Build.VERSION.SDK_INT > 10) {
					try {
						startActivity(new Intent(Intent.ACTION_MAIN)
								.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
								// .addCategory("com.android.internal.category.PLATLOGO"));
								.setClassName("areeb.xposed.eggster",
										"areeb.xposed.eggster.kk.DessertCase"));
					} catch (ActivityNotFoundException ex) {
						android.util.Log.e("PlatLogoActivity",
								"Couldn't catch a break.");
					}
				}
				finish();
				return true;
			}
		});
		
		if (leMode.equals(getString(R.string.pref_translucent))) {
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); }

        if (leMode.equals(getString(R.string.pref_immerge))) {
    			 mContent.setSystemUiVisibility(
    	                   View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    	                   | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    	                   | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    	                   | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION 		// hide nav bar
    	                   | View.SYSTEM_UI_FLAG_FULLSCREEN 			// hide status bar
    	                   | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); 	// immerge
    		}
		
		setContentView(mContent);
	}
}