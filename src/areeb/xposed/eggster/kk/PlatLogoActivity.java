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
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import areeb.xposed.eggster.R;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;


public class PlatLogoActivity extends Activity {
	FrameLayout mContent;
	int mCount;
	final Handler mHandler = new Handler();
	static final int BGCOLOR = 0xffed1d24;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		Typeface bold = Typeface
				.createFromAsset(getAssets(), "Roboto-Bold.ttf");
		Typeface light = Typeface.createFromAsset(getAssets(),
				"Roboto-Light.ttf");
		
		SharedPreferences pref = getSharedPreferences("preferenceggs", Context.MODE_PRIVATE);
        String kkLetter = pref.getString("kk_letter", getString(R.string.pref_default_kk_letter));
        String kkText = pref.getString("kk_text", getString(R.string.pref_default_kk_text));

		mContent = new FrameLayout(this);
		mContent.setBackgroundColor(0xC0000000);

		final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
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
		letter.setTextSize(300);
		letter.setTextColor(Color.WHITE);
		letter.setGravity(Gravity.CENTER);
		letter.setText(kkLetter);

		final int p = (int) (4 * metrics.density);

		final TextView tv = new TextView(this);
        if (light != null) tv.setTypeface(light);
        tv.setTextSize(30);
        tv.setPadding(p, p, p, p);
        tv.setTextColor(0xFFFFFFFF);
        tv.setGravity(Gravity.CENTER);
        tv.setText(kkText);
        tv.setVisibility(View.INVISIBLE);

		mContent.addView(bg);
		mContent.addView(letter, lp);
		mContent.addView(logo, lp);
		
	
			final FrameLayout.LayoutParams lapar = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		lapar.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		lapar.bottomMargin = 10 * p;

		mContent.addView(tv, lapar);

		mContent.setOnClickListener(new View.OnClickListener() {
			int clicks;

			@Override
			public void onClick(View v) {
				clicks++;
				if (clicks >= 6) {
					mContent.performLongClick();
					return;
				}
				ViewPropertyAnimator.animate(letter).cancel();
				ViewPropertyAnimator vpa = ViewPropertyAnimator.animate(letter);
				if (Math.random() > 0.5D)
					;
				for (int i = 360;;) {
					vpa.rotationBy(i)
							.setInterpolator(new DecelerateInterpolator())
							.setDuration(700L).start();
					return;
				}
			}
		});

		mContent.setOnLongClickListener(new View.OnLongClickListener() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
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
							.alpha(1.0F)
							.scaleX(1.0F)
							.scaleY(1.0F)
							.setDuration(1000L)
							.setStartDelay(500L)
							.setInterpolator(
									new AnticipateOvershootInterpolator())
							.start();
					ViewHelper.setAlpha(tv, 0F);
					v.setVisibility(0);
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
								.setFlags(
										Intent.FLAG_ACTIVITY_NEW_TASK
												| Intent.FLAG_ACTIVITY_CLEAR_TASK
												| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
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

		setContentView(mContent);
	}
}