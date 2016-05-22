/*
 * Copyright (C) 2010 The Android Open Source Project
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
 */
package areeb.xposed.eggster.eggs.ics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.Toast;
import areeb.xposed.eggster.R;

public class PlatLogoActivity extends Activity {
    final Handler mHandler = new Handler();
    Toast mToast;
    ImageView mContent;
    Vibrator mZzz;
    int mCount;
    Runnable mSuperLongPress = new Runnable() {
        public void run() {
            mCount++;
            mZzz.vibrate(50 * mCount);
            final float scale = 1f + 0.25f * mCount * mCount;
            mContent.setScaleX(scale);
            mContent.setScaleY(scale);

            if (mCount <= 3) {
                mHandler.postDelayed(mSuperLongPress, ViewConfiguration.getLongPressTimeout());
            } else {
                startActivity(new Intent(Intent.ACTION_MAIN).setFlags(
                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                        .setClassName("areeb.xposed.eggster",
                                "areeb.xposed.eggster.eggs.ics.Nyandroid"));
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mZzz = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        mToast = Toast.makeText(this, "Android 4.0: Ice Cream Sandwich", Toast.LENGTH_SHORT);

        mContent = new ImageView(this);
        mContent.setImageResource(R.drawable.platlogoics);
        mContent.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        mContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    mContent.setPressed(true);
                    mHandler.removeCallbacks(mSuperLongPress);
                    mCount = 0;
                    mHandler.postDelayed(mSuperLongPress, 2 * ViewConfiguration.getLongPressTimeout());
                } else if (action == MotionEvent.ACTION_UP) {
                    if (mContent.isPressed()) {
                        mContent.setPressed(false);
                        mHandler.removeCallbacks(mSuperLongPress);
                        mToast.show();
                    }
                }
                return true;
            }
        });

        setContentView(mContent);
    }
}