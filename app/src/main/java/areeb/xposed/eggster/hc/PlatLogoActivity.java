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

package areeb.xposed.eggster.hc;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import areeb.xposed.eggster.R;

@SuppressLint({"ShowToast", "InlinedApi"})
public class PlatLogoActivity extends Activity {
    Toast mToast;
    int BGCOLOR = 0xD0000000;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = getSharedPreferences("preferenceggs", Context.MODE_PRIVATE);
        String SysUImode = pref.getString("hc_sysui", getString(R.string.pref_none));
        if (SysUImode.equals("Translucent Mode")) {
            setTheme(R.style.Wallpaper_TranslucentDecor);
        }
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setBackgroundColor(BGCOLOR);

        String hcToast = pref.getString("hc_toast_text", getString(R.string.pref_default_hc_text));
        mToast = Toast.makeText(this, hcToast, Toast.LENGTH_SHORT);

        if (SysUImode.equals(getString(R.string.pref_none)))
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView content = new ImageView(this);

        content.setImageResource(R.drawable.platlogo_hc);
        content.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        if (Build.VERSION.SDK_INT >= 19 && SysUImode.equals("Immersive Mode")) {
            content.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        setContentView(content);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Boolean forcePort = getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getBoolean("hc_force_port", false);

        if (forcePort == true) {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            mToast.show();
        }
        return super.dispatchTouchEvent(ev);
    }
}