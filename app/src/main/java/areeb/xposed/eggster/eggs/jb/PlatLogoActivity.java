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

package areeb.xposed.eggster.eggs.jb;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import areeb.xposed.eggster.R;

public class PlatLogoActivity extends Activity {
    Toast mToast;
    ImageView mContent;
    int mCount;
    final Handler mHandler = new Handler();

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
        final int p = (int)(8 * metrics.density);
        view.setPadding(p, p, p, p);

        Typeface normal = Typeface.createFromAsset(getAssets(), "Roboto-Bold.ttf");
        Typeface light = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");

        final float size = 14 * metrics.density;
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.bottomMargin = (int) (-4*metrics.density);

        TextView tv = new TextView(this);
        if (light != null) tv.setTypeface(light);
        tv.setTextSize(1.25f*size);
        tv.setTextColor(0xFFFFFFFF);
        tv.setShadowLayer(4*metrics.density, 0, 2*metrics.density, 0x66000000);
        tv.setText("Android 4.3");
        view.addView(tv, lp);

        tv = new TextView(this);
        if (normal != null) tv.setTypeface(normal);
        tv.setTextSize(size);
        tv.setTextColor(0xFFFFFFFF);
        tv.setShadowLayer(4*metrics.density, 0, 2*metrics.density, 0x66000000);
        tv.setText("JELLY BEAN");
        view.addView(tv, lp);

        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        mToast.setView(makeView());

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        mContent = new ImageView(this);
        mContent.setImageResource(R.drawable.platlogo_alt);
        mContent.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        final int p = (int)(32 * metrics.density);
        mContent.setPadding(p, p, p, p);

        mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToast.show();
                mContent.setImageResource(R.drawable.platlogo);
            }
        });

        mContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(Intent.ACTION_MAIN).setFlags(
                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                        .setClassName("areeb.xposed.eggster",
                                "areeb.xposed.eggster.eggs.jb.BeanBag"));
                finish();
                return true;
            }
        });

        setContentView(mContent);
    }
}