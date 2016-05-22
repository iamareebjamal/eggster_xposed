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

package areeb.xposed.eggster.eggs.hc;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;
import areeb.xposed.eggster.R;

public class PlatLogoActivity extends Activity {
    Toast mToast;
    int BGCOLOR = 0xD0000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setBackgroundColor(BGCOLOR);

        mToast = Toast.makeText(this, "REZZZZZZZ...", Toast.LENGTH_SHORT);

        ImageView content = new ImageView(this);

        content.setImageResource(R.drawable.platlogo_hc);
        content.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
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