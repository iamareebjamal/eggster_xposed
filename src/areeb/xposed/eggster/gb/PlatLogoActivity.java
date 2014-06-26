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

package areeb.xposed.eggster.gb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import areeb.xposed.eggster.R;
import areeb.xposed.eggster.R.style;

@SuppressLint("ShowToast")
public class PlatLogoActivity extends Activity {
    Toast mToast;

    @SuppressLint({ "ShowToast", "InlinedApi", "NewApi" })
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        		
        
        SharedPreferences pref = getSharedPreferences("preferenceggs", Context.MODE_PRIVATE);
        String gbToast = pref.getString("gb_toast_text", getString(R.string.pref_default_gb_text));
        Boolean TransBG = pref.getBoolean("gb_transp_bg", false);
        String SysUIMode = pref.getString("gb_sysui", "Normal");
        if (SysUIMode.equals("Normal")) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        mToast = Toast.makeText(this, gbToast, Toast.LENGTH_SHORT);
        ImageView content = new ImageView(this);
        if (TransBG) {
        	content.setBackgroundColor(0xCA000000);
        } else {
        	content.setBackgroundColor(0xFF000000);
        }
        content.setImageResource(R.drawable.platlogogb);
        content.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (Build.VERSION.SDK_INT >= 19 && SysUIMode.equals("Immersive Mode")) {
        content.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION		// hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN			// hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);	//mmersive
        } else if (Build.VERSION.SDK_INT >= 19 && SysUIMode.equals("Translucent Mode")) {
                	super.setTheme(style.Wallpaper_TranslucentDecor);
                }
        
        setContentView(content);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            mToast.show();
        }
        return super.dispatchTouchEvent(ev);
    }
}