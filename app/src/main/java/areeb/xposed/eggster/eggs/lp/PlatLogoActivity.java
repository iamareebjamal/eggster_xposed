package areeb.xposed.eggster.eggs.lp;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ImageView;
import areeb.xposed.eggster.R;
import areeb.xposed.eggster.utils.PathInterpolator;
import com.balysv.materialripple.MaterialRippleLayout;

public class PlatLogoActivity extends Activity {
    final static int[] FLAVORS = {
            0xFF9C27B0, 0xFFBA68C8, // grape
            0xFFFF9800, 0xFFFFB74D, // orange
            0xFFF06292, 0xFFF8BBD0, // bubblegum
            0xFFAFB42B, 0xFFCDDC39, // lime
            0xFFFFEB3B, 0xFFFFF176, // lemon
            0xFF795548, 0xFFA1887F, // mystery flavor
    };
    FrameLayout mLayout;
    int mTapCount;
    int mKeyCount;
    PathInterpolator mInterpolator = new PathInterpolator(0f, 0f, 0.5f, 1f);

    static int newColorIndex() {
        return 2 * ((int) (Math.random() * FLAVORS.length / 2));
    }

    Drawable makeRipple() {
        final int idx = newColorIndex();
        final ShapeDrawable popbg = new ShapeDrawable(new OvalShape());
        popbg.getPaint().setColor(FLAVORS[idx]);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final RippleDrawable ripple = new RippleDrawable(
                    ColorStateList.valueOf(FLAVORS[idx+1]),
                    popbg, null);

            return ripple;
        }

        return popbg;
    }

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

        final View stick = new View(this) {
            Paint mPaint = new Paint();
            Path mShadow = new Path();

            @Override
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public void onAttachedToWindow() {
                super.onAttachedToWindow();
                setWillNotDraw(false);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    setOutlineProvider(new ViewOutlineProvider() {
                        @Override
                        public void getOutline(View view, Outline outline) {
                            outline.setRect(0, getHeight() / 2, getWidth(), getHeight());
                        }
                    });
                }

            }

            @Override
            public void onDraw(Canvas c) {
                final int w = c.getWidth();
                final int h = c.getHeight() / 2;
                c.translate(0, h);

                int colors[] = new int[]{0xFFFFFFFF, 0xFFAAAAAA};

                final GradientDrawable g;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    g = new GradientDrawable();
                    g.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                    g.setColors(colors);
                } else {
                    g = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
                }

                g.setGradientCenter(w * 0.75f, 0);
                g.setBounds(0, 0, w, h);
                g.draw(c);
                mPaint.setColor(0xFFAAAAAA);
                mShadow.reset();
                mShadow.moveTo(0, 0);
                mShadow.lineTo(w, 0);
                mShadow.lineTo(w, size / 2 + 1.5f * w);
                mShadow.lineTo(0, size / 2);
                mShadow.close();
                c.drawPath(mShadow, mPaint);
            }
        };
        mLayout.addView(stick, new FrameLayout.LayoutParams((int) (32 * dp),
                ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL));
        stick.setAlpha(0f);

        final ImageView im = new ImageView(this);
        //im.setTranslationZ(20);
        im.setScaleX(0);
        im.setScaleY(0);

        final Drawable platlogo;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            platlogo = ContextCompat.getDrawable(this, R.drawable.platlogo_lp);
        } else {
            platlogo = VectorDrawableCompat.create(getResources(), R.drawable.platlogo_lp, getTheme());
        }

        platlogo.setAlpha(0);
        im.setImageDrawable(platlogo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            im.setBackground(makeRipple());
        else
            im.setBackgroundDrawable(makeRipple());

        im.setClickable(true);
        final ShapeDrawable highlight = new ShapeDrawable(new OvalShape());
        highlight.getPaint().setColor(0x10FFFFFF);
        highlight.setBounds((int) (size * .15f), (int) (size * .15f),
                (int) (size * .6f), (int) (size * .6f));

        // Test for working
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            im.getOverlay().add(highlight);
        } else {
            im.setImageDrawable(highlight);
        }

        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTapCount == 0) {

                    //TODO : Improve version handling

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        im.animate()
                                .translationZ(20)
                                .scaleX(1)
                                .scaleY(1)
                                .setInterpolator(mInterpolator)
                                .setDuration(700)
                                .setStartDelay(500)
                                .start();
                    } else {
                        im.animate()
                                .scaleX(1)
                                .scaleY(1)
                                .setInterpolator(mInterpolator)
                                .setDuration(700)
                                .setStartDelay(500)
                                .start();
                    }

                    final ObjectAnimator a = ObjectAnimator.ofInt(platlogo, "alpha", 0, 255);
                    a.setInterpolator(mInterpolator);
                    a.setStartDelay(1000);
                    a.start();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        stick.animate()
                                .translationZ(20)
                                .alpha(1)
                                .setInterpolator(mInterpolator)
                                .setDuration(700)
                                .setStartDelay(750)
                                .start();
                    } else {
                        stick.animate()
                                .alpha(1)
                                .setInterpolator(mInterpolator)
                                .setDuration(700)
                                .setStartDelay(750)
                                .start();
                    }


                    im.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if (mTapCount < 5) return false;

                            startActivity(new Intent(Intent.ACTION_MAIN).setFlags(
                                    Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                                    .setClassName("areeb.xposed.eggster",
                                            "areeb.xposed.eggster.eggs.lp.LLandActivity"));
                            finish();

                            return true;
                        }
                    });
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        im.setBackground(makeRipple());
                    else
                        im.setBackgroundDrawable(makeRipple());
                }
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

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            MaterialRippleLayout.on(im)
                    .rippleOverlay(true)
                    .rippleColor(0xFFFFFF)
                    .rippleAlpha(0.2f)
                    .rippleRoundedCorners(size/3)
                    .create();
        }

        im.animate().scaleX(0.3f).scaleY(0.3f)
                .setInterpolator(mInterpolator)
                .setDuration(500)
                .setStartDelay(800)
                .start();

    }


}