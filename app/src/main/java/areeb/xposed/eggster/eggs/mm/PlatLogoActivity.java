package areeb.xposed.eggster.eggs.mm;

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

        final View im = new View(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            im.setTranslationZ(20);

        im.setScaleX(0.5f);
        im.setScaleY(0.5f);
        im.setAlpha(0f);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            im.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                public void getOutline(View view, Outline outline) {
                    final int pad = (int) (8 * dp);
                    outline.setOval(pad, pad, view.getWidth() - pad, view.getHeight() - pad);
                }
            });
        }

        final float hue = (float) Math.random();
        final Paint bgPaint = new Paint();

        // TODO : Check for color
        bgPaint.setColor(Misc.HSBtoColor(hue, 0.4f, 1f));
        final Paint fgPaint = new Paint();
        fgPaint.setColor(Misc.HSBtoColor(hue, 0.5f, 1f));
        final Drawable M;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            M = ContextCompat.getDrawable(this, R.drawable.platlogo_m);
        } else {
            M = VectorDrawableCompat.create(getResources(), R.drawable.platlogo_m, getTheme());
        }

        final Drawable platlogo = new Drawable() {
            @Override
            public void setAlpha(int alpha) {
            }

            @Override
            public void setColorFilter(@Nullable ColorFilter colorFilter) {
            }

            @Override
            public int getOpacity() {
                return PixelFormat.TRANSLUCENT;
            }

            @Override
            public void draw(Canvas c) {
                final float r = c.getWidth() / 2f;
                c.drawCircle(r, r, r, bgPaint);
                Misc.drawArc(c, 0, 0, 2 * r, 2 * r, 135, 180, false, fgPaint);
                M.setBounds(0, 0, c.getWidth(), c.getHeight());
                M.draw(c);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            im.setBackground(new RippleDrawable(
                    ColorStateList.valueOf(0xFFFFFFFF),
                    platlogo,
                    null));
            im.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                im.setBackground(platlogo);
            else
                im.setBackgroundDrawable(platlogo);

        }

        im.setClickable(true);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTapCount == 0) {
                    showMarshmallow(im);
                }
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
                    if (mKeyCount == 0) {
                        showMarshmallow(im);
                    }
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
                    .rippleRoundedCorners(size)
                    .create();
        }

        im.animate().scaleX(1f).scaleY(1f).alpha(1f)
                .setInterpolator(mInterpolator)
                .setDuration(500)
                .setStartDelay(800)
                .start();
    }

    public void showMarshmallow(View im) {
        final Drawable fg;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fg = getDrawable(R.drawable.platlogo_mm);
        } else {
            fg = VectorDrawableCompat.create(getResources(), R.drawable.platlogo_mm, getTheme());
        }

        fg.setBounds(0, 0, im.getWidth(), im.getHeight());
        fg.setAlpha(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            im.getOverlay().add(fg);
        } else {
            im.setBackgroundDrawable(fg);
        }

        final Animator fadeIn = ObjectAnimator.ofInt(fg, "alpha", 255);
        fadeIn.setInterpolator(mInterpolator);
        fadeIn.setDuration(300);
        fadeIn.start();
    }

}