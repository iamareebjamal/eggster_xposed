/*
*  Jelly Bean PlatLogoActivity
*  Copyright (C) 2012 The Android Open Source Project, Modified by Areeb Jamal
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
*  Contact: jamal.areeb@gmail.com
*/

package areeb.xposed.eggster.jb;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import areeb.xposed.eggster.R;

public class PlatLogoActivity extends Activity {
    final Handler mHandler = new Handler();
    Toast mToast;
    ImageView mContent;
    int mCount;
    int mSize;

    private View makeView() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.VERTICAL);
        view.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
        final int p = (int) (8 * metrics.density);
        view.setPadding(p, p, p, p);

        SharedPreferences pref = getSharedPreferences("preferenceggs", Context.MODE_PRIVATE);
        String jbVer = pref.getString("jb_text_1", getString(R.string.pref_default_jb_text_1));
        String jbName = pref.getString("jb_text_2", getString(R.string.pref_default_jb_text_2));

        Typeface normal = Typeface.createFromAsset(getAssets(), "Roboto-Bold.ttf");
        Typeface light = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");


        int jbSize = getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getInt("jb_size", 14);

        try {


            if (String.valueOf(jbSize) != null && jbSize > 0 && String.valueOf(jbSize).matches("\\d*") && String.valueOf(jbSize).length() > 0) {

                mSize = jbSize;

            }

        } catch (NumberFormatException e) {

            mSize = 14;
            e.printStackTrace();

        }

        final float size = mSize * metrics.density;
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.bottomMargin = (int) (-1 * metrics.density);


        TextView tv = new TextView(this);
        if (light != null) tv.setTypeface(light);
        tv.setTextSize(1.25f * size);
        tv.setTextColor(0xFFFFFFFF);
        tv.setShadowLayer(4 * metrics.density, 0, 2 * metrics.density, 0x66000000);
        tv.setText(jbVer);
        view.addView(tv, lp);

        tv = new TextView(this);
        if (normal != null) tv.setTypeface(normal);
        tv.setTextSize(size);
        tv.setTextColor(0xFFFFFFFF);
        tv.setShadowLayer(4 * metrics.density, 0, 2 * metrics.density, 0x66000000);
        tv.setText(jbName);
        view.addView(tv, lp);

        return view;
    }

    @SuppressLint({"ShowToast", "NewApi", "InlinedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        SharedPreferences shr = getSharedPreferences("preferenceggs", Context.MODE_PRIVATE);
        String leMode = shr.getString("jb_sysui_plat", getString(R.string.pref_none));


        if (leMode.equals(getString(R.string.pref_none)))
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (leMode.equals(getString(R.string.pref_translucent))) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        mToast.setView(makeView());

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        mContent = new ImageView(this);
        mContent.setImageResource(R.drawable.platlogo_alt);
        mContent.setScaleType(ImageView.ScaleType.CENTER_INSIDE);


        final int p = (int) (32 * metrics.density);
        mContent.setPadding(p, p, p, p);

        mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToast.show();
                mContent.setImageResource(R.drawable.platlogo);
            }
        });

        mContent.setOnLongClickListener(new View.OnLongClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onLongClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_MAIN)
                            .setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                            //.addCategory("com.android.internal.category.PLATLOGO"));
                            .setClassName("areeb.xposed.eggster", "areeb.xposed.eggster.jb.BeanBag"));
                } catch (ActivityNotFoundException ex) {
                    android.util.Log.e("PlatLogoActivity", "Couldn't find a bag of beans.");
                }
                finish();
                return true;
            }
        });

        if (leMode.equals(getString(R.string.pref_immerge))) {
            mContent.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION        // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN            // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);    // immerge
        }
        setContentView(mContent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Boolean forcePort = getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getBoolean("jb_force_port", false);

        if (forcePort == true) {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }


    }
}