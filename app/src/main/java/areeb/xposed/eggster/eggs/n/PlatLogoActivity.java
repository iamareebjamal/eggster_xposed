package areeb.xposed.eggster.eggs.n;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import areeb.xposed.eggster.R;
import areeb.xposed.eggster.utils.Misc;
import areeb.xposed.eggster.utils.PathInterpolator;
import com.balysv.materialripple.MaterialRippleLayout;

public class PlatLogoActivity extends Activity {
    FrameLayout mLayout;
    int mTapCount;
    int mKeyCount;
    PathInterpolator mInterpolator = new PathInterpolator(0f, 0f, 0.5f, 1f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLayout = new FrameLayout(this);
        setContentView(mLayout);
    }


    @Override
    public void onAttachedToWindow() {
        final DisplayMetrics dm = getResources().getDisplayMetrics();
        final float dp = dm.density;
        final int size = (int)
                (Math.min(Math.min(dm.widthPixels, dm.heightPixels), 600 * dp) - 100 * dp);

        final ImageView im = new ImageView(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            im.setTranslationZ(20);

        im.setScaleX(0.5f);
        im.setScaleY(0.5f);
        im.setAlpha(0f);

        final Drawable N;

        N = ContextCompat.getDrawable(this, R.drawable.platlogo_n);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            im.setImageDrawable(new RippleDrawable(
                    ColorStateList.valueOf(0xFFFFFFFF),
                    N,
                    null));
        } else {
                im.setImageDrawable(N);
        }

        im.setClickable(true);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                im.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mTapCount < 5) return false;

                        startActivity(new Intent(Intent.ACTION_MAIN).setFlags(
                                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                                .setClassName("areeb.xposed.eggster",
                                        "areeb.xposed.eggster.eggs.mm.MLandActivity"));
                        finish();
                        return true;
                    }
                });
                mTapCount++;
            }
        });

        // Enable hardware keyboard input for TV compatibility.
        im.setFocusable(true);
        im.requestFocus();
        im.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode != KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

                    ++mKeyCount;
                    if (mKeyCount > 2) {
                        if (mTapCount > 5) {
                            im.performLongClick();
                        } else {
                            im.performClick();
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        mLayout.addView(im, new FrameLayout.LayoutParams(size, size, Gravity.CENTER));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            MaterialRippleLayout.on(im)
                    .rippleOverlay(true)
                    .rippleColor(0xFFFFFF)
                    .rippleAlpha(0.2f)
                    .rippleRoundedCorners(size*3)
                    .create();
        }

        im.animate().scaleX(1.2f).scaleY(1.2f).alpha(1f)
                .setInterpolator(mInterpolator)
                .setDuration(500)
                .setStartDelay(800)
                .start();
    }


}