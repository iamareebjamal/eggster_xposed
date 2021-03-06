package areeb.xposed.eggster.eggs.lp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import areeb.xposed.eggster.R;
import areeb.xposed.eggster.utils.Misc;
import com.nineoldandroids.animation.TimeAnimator;

import java.util.ArrayList;

public class LLand extends FrameLayout {
    public static final String TAG = "LLand";

    public static final boolean DEBUG = Log.isLoggable(TAG, Log.DEBUG);
    public static final boolean DEBUG_DRAW = false; // DEBUG
    public static final boolean AUTOSTART = true;
    public static final boolean HAVE_STARS = true;
    public static final float DEBUG_SPEED_MULTIPLIER = 1f; // 0.1f;
    public static final boolean DEBUG_IDDQD = false;
    final static int[] POPS = {
            // resid                // spinny!
            R.drawable.pop_belt, 0,
            R.drawable.pop_droid, 0,
            R.drawable.pop_pizza, 1,
            R.drawable.pop_stripes, 0,
            R.drawable.pop_swirl, 1,
            R.drawable.pop_vortex, 1,
            R.drawable.pop_vortex2, 1,
    };
    static final Rect sTmpRect = new Rect();
    private static final int DAY = 0, NIGHT = 1, TWILIGHT = 2, SUNSET = 3;
    private static final int[][] SKIES = {
            {0xFFc0c0FF, 0xFFa0a0FF}, // DAY
            {0xFF000010, 0xFF000000}, // NIGHT
            {0xFF000040, 0xFF000010}, // TWILIGHT
            {0xFFa08020, 0xFF204080}, // SUNSET
    };
    private static Params PARAMS;
    final float hsv[] = {0, 0, 0};
    private TimeAnimator mAnim;
    private TextView mScoreField;
    private View mSplash;
    private Player mDroid;
    private ArrayList<Obstacle> mObstaclesInPlay = new ArrayList<Obstacle>();
    private float t, dt;
    private int mScore;
    private float mLastPipeTime; // in sec
    private int mWidth, mHeight;
    private boolean mAnimating, mPlaying;
    private boolean mFrozen; // after death, a short backoff
    private boolean mFlipped;
    private int mTimeOfDay;

    public LLand(Context context) {
        this(context, null);
    }

    public LLand(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LLand(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFocusable(true);
        PARAMS = new Params(getResources());
        mTimeOfDay = irand(0, SKIES.length);
    }

    public static final void L(String s, Object... objects) {
        if (DEBUG) {
            Log.d(TAG, String.format(s, objects));
        }
    }

    public static final float lerp(float x, float a, float b) {
        return (b - a) * x + a;
    }

    public static final float rlerp(float v, float a, float b) {
        return (v - a) / (b - a);
    }

    public static final float clamp(float f) {
        return f < 0f ? 0f : f > 1f ? 1f : f;
    }

    public static final float frand() {
        return (float) Math.random();
    }

    public static final float frand(float a, float b) {
        return lerp(frand(), a, b);
    }

    public static final int irand(int a, int b) {
        return (int) lerp(frand(), (float) a, (float) b);
    }

    @Override
    public boolean willNotDraw() {
        return !DEBUG;
    }

    public int getGameWidth() {
        return mWidth;
    }

    public int getGameHeight() {
        return mHeight;
    }

    public float getGameTime() {
        return t;
    }

    public float getLastTimeStep() {
        return dt;
    }

    public void setScoreField(TextView tv) {
        mScoreField = tv;
        if (tv != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                tv.setTranslationZ(PARAMS.HUD_Z);
            if (!(mAnimating && mPlaying)) {
                tv.setTranslationY(-500);
            }
        }
    }

    public void setSplash(View v) {
        mSplash = v;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        stop();
        reset();
        if (AUTOSTART) {
            start(false);
        }
    }

    private void reset() {
        L("reset");
        final Drawable sky = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                SKIES[mTimeOfDay]
        );
        sky.setDither(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            setBackground(sky);
        else
            setBackgroundDrawable(sky);

        mFlipped = frand() > 0.5f;
        setScaleX(mFlipped ? -1 : 1);

        setScore(0);

        int i = getChildCount();
        while (i-- > 0) {
            final View v = getChildAt(i);
            if (v instanceof GameView) {
                removeViewAt(i);
            }
        }

        mObstaclesInPlay.clear();

        mWidth = getWidth();
        mHeight = getHeight();

        boolean showingSun = (mTimeOfDay == DAY || mTimeOfDay == SUNSET) && frand() > 0.25;
        if (showingSun) {
            final Star sun = new Star(getContext());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sun.setBackgroundResource(R.drawable.sun);
            } else {
                Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.sun, null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    sun.setBackground(drawable);
                else
                    sun.setBackgroundDrawable(drawable);
            }

            final int w = getResources().getDimensionPixelSize(R.dimen.sun_size);
            sun.setTranslationX(frand(w, mWidth - w));
            if (mTimeOfDay == DAY) {
                sun.setTranslationY(frand(w, (mHeight * 0.66f)));
                Misc.setTint(sun.getBackground(), 0); // No tint mode
            } else {
                sun.setTranslationY(frand(mHeight * 0.66f, mHeight - w));
                Misc.setTint(sun.getBackground(), 0xC0FF8000);
            }
            addView(sun, new LayoutParams(w, w));
        }
        if (!showingSun) {
            final boolean dark = mTimeOfDay == NIGHT || mTimeOfDay == TWILIGHT;
            final float ff = frand();
            if ((dark && ff < 0.75f) || ff < 0.5f) {
                final Star moon = new Star(getContext());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    moon.setBackgroundResource(R.drawable.moon);
                } else {
                    Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.moon, null);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        moon.setBackground(drawable);
                    else
                        moon.setBackgroundDrawable(drawable);
                }
                moon.getBackground().setAlpha(dark ? 255 : 128);
                moon.setScaleX(frand() > 0.5 ? -1 : 1);
                moon.setRotation(moon.getScaleX() * frand(5, 30));
                final int w = getResources().getDimensionPixelSize(R.dimen.sun_size);
                moon.setTranslationX(frand(w, mWidth - w));
                moon.setTranslationY(frand(w, mHeight - w));
                addView(moon, new LayoutParams(w, w));
            }
        }

        final int mh = mHeight / 6;
        final boolean cloudless = frand() < 0.25;
        final int N = 20;
        for (i = 0; i < N; i++) {
            final float r1 = frand();
            final Scenery s;
            if (HAVE_STARS && r1 < 0.3 && mTimeOfDay != DAY) {
                s = new Star(getContext());
            } else if (r1 < 0.6 && !cloudless) {
                s = new Cloud(getContext());
            } else {
                s = new Building(getContext());

                s.z = (float) i / N;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    s.setTranslationZ(PARAMS.SCENERY_Z * (1 + s.z));
                s.v = 0.85f * s.z; // buildings move proportional to their distance
                hsv[0] = 175;
                hsv[1] = 0.25f;
                hsv[2] = 1 * s.z;
                s.setBackgroundColor(Color.HSVToColor(hsv));
                s.h = irand(PARAMS.BUILDING_HEIGHT_MIN, mh);
            }
            final LayoutParams lp = new LayoutParams(s.w, s.h);
            if (s instanceof Building) {
                lp.gravity = Gravity.BOTTOM;
            } else {
                lp.gravity = Gravity.TOP;
                final float r = frand();
                if (s instanceof Star) {
                    lp.topMargin = (int) (r * r * mHeight);
                } else {
                    lp.topMargin = (int) (1 - r * r * mHeight / 2) + mHeight / 2;
                }
            }

            addView(s, lp);
            s.setTranslationX(frand(-lp.width, mWidth + lp.width));
        }

        mDroid = new Player(getContext());
        mDroid.setX(mWidth / 2);
        mDroid.setY(mHeight / 2);
        addView(mDroid, new LayoutParams(PARAMS.PLAYER_SIZE, PARAMS.PLAYER_SIZE));

        mAnim = new TimeAnimator();
        mAnim.setTimeListener(new TimeAnimator.TimeListener() {
            @Override
            public void onTimeUpdate(TimeAnimator timeAnimator, long t, long dt) {
                step(t, dt);
            }
        });
    }

    private void setScore(int score) {
        mScore = score;
        if (mScoreField != null) mScoreField.setText(String.valueOf(score));
    }

    private void addScore(int incr) {
        setScore(mScore + incr);
    }

    private void start(boolean startPlaying) {
        L("start(startPlaying=%s)", startPlaying ? "true" : "false");
        if (startPlaying) {
            mPlaying = true;

            t = 0;
            // there's a sucker born every OBSTACLE_PERIOD
            mLastPipeTime = getGameTime() - PARAMS.OBSTACLE_PERIOD;

            if (mSplash != null && mSplash.getAlpha() > 0f) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mSplash.setTranslationZ(PARAMS.HUD_Z);
                    mSplash.animate().alpha(0).translationZ(0).setDuration(400);
                } else {
                    mSplash.animate().alpha(0).setDuration(400);
                }

                mScoreField.animate().translationY(0)
                        .setInterpolator(new DecelerateInterpolator())
                        .setDuration(1500);
            }

            mScoreField.setTextColor(0xFFAAAAAA);
            mScoreField.setBackgroundResource(R.drawable.scorecard);
            mDroid.setVisibility(View.VISIBLE);
            mDroid.setX(mWidth / 2);
            mDroid.setY(mHeight / 2);
        } else {
            mDroid.setVisibility(View.GONE);
        }
        if (!mAnimating) {
            mAnim.start();
            mAnimating = true;
        }
    }

    private void stop() {
        if (mAnimating) {
            mAnim.cancel();
            mAnim = null;
            mAnimating = false;
            mScoreField.setTextColor(0xFFFFFFFF);
            mScoreField.setBackgroundResource(R.drawable.scorecard_gameover);
            mTimeOfDay = irand(0, SKIES.length); // for next reset
            mFrozen = true;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFrozen = false;
                }
            }, 250);
        }
    }

    private void step(long t_ms, long dt_ms) {
        t = t_ms / 1000f; // seconds
        dt = dt_ms / 1000f;

        if (DEBUG) {
            t *= DEBUG_SPEED_MULTIPLIER;
            dt *= DEBUG_SPEED_MULTIPLIER;
        }

        // 1. Move all objects and update bounds
        final int N = getChildCount();
        int i = 0;
        for (; i < N; i++) {
            final View v = getChildAt(i);
            if (v instanceof GameView) {
                ((GameView) v).step(t_ms, dt_ms, t, dt);
            }
        }

        // 2. Check for altitude
        if (mPlaying && mDroid.below(mHeight)) {
            if (DEBUG_IDDQD) {
                poke();
            } else {
                L("player hit the floor");
                stop();
            }
        }

        // 3. Check for obstacles
        boolean passedBarrier = false;
        for (int j = mObstaclesInPlay.size(); j-- > 0; ) {
            final Obstacle ob = mObstaclesInPlay.get(j);
            if (mPlaying && ob.intersects(mDroid) && !DEBUG_IDDQD) {
                L("player hit an obstacle");
                stop();
            } else if (ob.cleared(mDroid)) {
                if (ob instanceof Stem) passedBarrier = true;
                mObstaclesInPlay.remove(j);
            }
        }

        if (mPlaying && passedBarrier) {
            addScore(1);
        }

        // 4. Handle edge of screen
        // Walk backwards to make sure removal is safe
        while (i-- > 0) {
            final View v = getChildAt(i);
            if (v instanceof Obstacle) {
                if (v.getTranslationX() + v.getWidth() < 0) {
                    removeViewAt(i);
                }
            } else if (v instanceof Scenery) {
                final Scenery s = (Scenery) v;
                if (v.getTranslationX() + s.w < 0) {
                    v.setTranslationX(getWidth());
                }
            }
        }

        // 3. Time for more obstacles!
        if (mPlaying && (t - mLastPipeTime) > PARAMS.OBSTACLE_PERIOD) {
            mLastPipeTime = t;
            final int obstacley = (int) (Math.random()
                    * (mHeight - 2 * PARAMS.OBSTACLE_MIN - PARAMS.OBSTACLE_GAP)) + PARAMS.OBSTACLE_MIN;

            final int inset = (PARAMS.OBSTACLE_WIDTH - PARAMS.OBSTACLE_STEM_WIDTH) / 2;
            final int yinset = PARAMS.OBSTACLE_WIDTH / 2;

            final int d1 = irand(0, 250);
            final Obstacle s1 = new Stem(getContext(), obstacley - yinset, false);
            addView(s1, new LayoutParams(
                    PARAMS.OBSTACLE_STEM_WIDTH,
                    (int) s1.h,
                    Gravity.TOP | Gravity.LEFT));
            s1.setTranslationX(mWidth + inset);
            s1.setTranslationY(-s1.h - yinset);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                s1.setTranslationZ(PARAMS.OBSTACLE_Z * 0.75f);
            s1.animate()
                    .translationY(0)
                    .setStartDelay(d1)
                    .setDuration(250);
            mObstaclesInPlay.add(s1);

            final Obstacle p1 = new Pop(getContext(), PARAMS.OBSTACLE_WIDTH);
            addView(p1, new LayoutParams(
                    PARAMS.OBSTACLE_WIDTH,
                    PARAMS.OBSTACLE_WIDTH,
                    Gravity.TOP | Gravity.LEFT));
            p1.setTranslationX(mWidth);
            p1.setTranslationY(-PARAMS.OBSTACLE_WIDTH);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                p1.setTranslationZ(PARAMS.OBSTACLE_Z);
            p1.setScaleX(0.25f);
            p1.setScaleY(0.25f);
            p1.animate()
                    .translationY(s1.h - inset)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setStartDelay(d1)
                    .setDuration(250);
            mObstaclesInPlay.add(p1);

            final int d2 = irand(0, 250);
            final Obstacle s2 = new Stem(getContext(),
                    mHeight - obstacley - PARAMS.OBSTACLE_GAP - yinset,
                    true);
            addView(s2, new LayoutParams(
                    PARAMS.OBSTACLE_STEM_WIDTH,
                    (int) s2.h,
                    Gravity.TOP | Gravity.LEFT));
            s2.setTranslationX(mWidth + inset);
            s2.setTranslationY(mHeight + yinset);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                s2.setTranslationZ(PARAMS.OBSTACLE_Z * 0.75f);
            s2.animate()
                    .translationY(mHeight - s2.h)
                    .setStartDelay(d2)
                    .setDuration(400);
            mObstaclesInPlay.add(s2);

            final Obstacle p2 = new Pop(getContext(), PARAMS.OBSTACLE_WIDTH);
            addView(p2, new LayoutParams(
                    PARAMS.OBSTACLE_WIDTH,
                    PARAMS.OBSTACLE_WIDTH,
                    Gravity.TOP | Gravity.LEFT));
            p2.setTranslationX(mWidth);
            p2.setTranslationY(mHeight);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                p2.setTranslationZ(PARAMS.OBSTACLE_Z);
            p2.setScaleX(0.25f);
            p2.setScaleY(0.25f);
            p2.animate()
                    .translationY(mHeight - s2.h - yinset)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setStartDelay(d2)
                    .setDuration(400);
            mObstaclesInPlay.add(p2);
        }

        if (DEBUG_DRAW) invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (DEBUG) L("touch: %s", ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                poke();
                return true;
            case MotionEvent.ACTION_UP:
                unpoke();
                return true;
        }
        return false;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        if (DEBUG) L("trackball: %s", ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                poke();
                return true;
            case MotionEvent.ACTION_UP:
                unpoke();
                return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent ev) {
        if (DEBUG) L("keyDown: %d", keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_SPACE:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_BUTTON_A:
                poke();
                return true;
        }
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent ev) {
        if (DEBUG) L("keyDown: %d", keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_SPACE:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_BUTTON_A:
                unpoke();
                return true;
        }
        return false;
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent ev) {
        if (DEBUG) L("generic: %s", ev);
        return false;
    }

    private void poke() {
        L("poke");
        if (mFrozen) return;
        if (!mAnimating) {
            reset();
            start(true);
        } else if (!mPlaying) {
            start(true);
        }
        mDroid.boost();
        if (DEBUG) {
            mDroid.dv *= DEBUG_SPEED_MULTIPLIER;
            mDroid.animate().setDuration((long) (200 / DEBUG_SPEED_MULTIPLIER));
        }
    }

    private void unpoke() {
        L("unboost");
        if (mFrozen) return;
        if (!mAnimating) return;
        mDroid.unboost();
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);

        if (!DEBUG_DRAW) return;

        final Paint pt = new Paint();
        pt.setColor(0xFFFFFFFF);
        final int L = mDroid.corners.length;
        final int N = L / 2;
        for (int i = 0; i < N; i++) {
            final int x = (int) mDroid.corners[i * 2];
            final int y = (int) mDroid.corners[i * 2 + 1];
            c.drawCircle(x, y, 4, pt);
            c.drawLine(x, y,
                    mDroid.corners[(i * 2 + 2) % L],
                    mDroid.corners[(i * 2 + 3) % L],
                    pt);
        }

        pt.setStyle(Paint.Style.STROKE);
        pt.setStrokeWidth(getResources().getDisplayMetrics().density);

        final int M = getChildCount();
        pt.setColor(0x8000FF00);
        for (int i = 0; i < M; i++) {
            final View v = getChildAt(i);
            if (v == mDroid) continue;
            if (!(v instanceof GameView)) continue;
            if (v instanceof Pop) {
                final Pop p = (Pop) v;
                c.drawCircle(p.cx, p.cy, p.r, pt);
            } else {
                final Rect r = new Rect();
                v.getHitRect(r);
                c.drawRect(r, pt);
            }
        }

        pt.setColor(Color.BLACK);
        final StringBuilder sb = new StringBuilder("obstacles: ");
        for (Obstacle ob : mObstaclesInPlay) {
            sb.append(ob.hitRect.toShortString());
            sb.append(" ");
        }
        pt.setTextSize(20f);
        c.drawText(sb.toString(), 20, 100, pt);
    }

    private interface GameView {
        public void step(long t_ms, long dt_ms, float t, float dt);
    }

    private static class Params {
        public float TRANSLATION_PER_SEC;
        public int OBSTACLE_SPACING, OBSTACLE_PERIOD;
        public int BOOST_DV;
        public int PLAYER_HIT_SIZE;
        public int PLAYER_SIZE;
        public int OBSTACLE_WIDTH, OBSTACLE_STEM_WIDTH;
        public int OBSTACLE_GAP;
        public int OBSTACLE_MIN;
        public int BUILDING_WIDTH_MIN, BUILDING_WIDTH_MAX;
        public int BUILDING_HEIGHT_MIN;
        public int CLOUD_SIZE_MIN, CLOUD_SIZE_MAX;
        public int STAR_SIZE_MIN, STAR_SIZE_MAX;
        public int G;
        public int MAX_V;
        public float SCENERY_Z, OBSTACLE_Z, PLAYER_Z, PLAYER_Z_BOOST, HUD_Z;

        public Params(Resources res) {
            TRANSLATION_PER_SEC = res.getDimension(R.dimen.translation_per_sec);
            OBSTACLE_SPACING = res.getDimensionPixelSize(R.dimen.obstacle_spacing);
            OBSTACLE_PERIOD = (int) (OBSTACLE_SPACING / TRANSLATION_PER_SEC);
            BOOST_DV = res.getDimensionPixelSize(R.dimen.boost_dv);
            PLAYER_HIT_SIZE = res.getDimensionPixelSize(R.dimen.player_hit_size);
            PLAYER_SIZE = res.getDimensionPixelSize(R.dimen.player_size);
            OBSTACLE_WIDTH = res.getDimensionPixelSize(R.dimen.obstacle_width);
            OBSTACLE_STEM_WIDTH = res.getDimensionPixelSize(R.dimen.obstacle_stem_width);
            OBSTACLE_GAP = res.getDimensionPixelSize(R.dimen.obstacle_gap);
            OBSTACLE_MIN = res.getDimensionPixelSize(R.dimen.obstacle_height_min);
            BUILDING_HEIGHT_MIN = res.getDimensionPixelSize(R.dimen.building_height_min);
            BUILDING_WIDTH_MIN = res.getDimensionPixelSize(R.dimen.building_width_min);
            BUILDING_WIDTH_MAX = res.getDimensionPixelSize(R.dimen.building_width_max);
            CLOUD_SIZE_MIN = res.getDimensionPixelSize(R.dimen.cloud_size_min);
            CLOUD_SIZE_MAX = res.getDimensionPixelSize(R.dimen.cloud_size_max);
            STAR_SIZE_MIN = res.getDimensionPixelSize(R.dimen.star_size_min);
            STAR_SIZE_MAX = res.getDimensionPixelSize(R.dimen.star_size_max);

            G = res.getDimensionPixelSize(R.dimen.G);
            MAX_V = res.getDimensionPixelSize(R.dimen.max_v);

            SCENERY_Z = res.getDimensionPixelSize(R.dimen.scenery_z);
            OBSTACLE_Z = res.getDimensionPixelSize(R.dimen.obstacle_z);
            PLAYER_Z = res.getDimensionPixelSize(R.dimen.player_z);
            PLAYER_Z_BOOST = res.getDimensionPixelSize(R.dimen.player_z_boost);
            HUD_Z = res.getDimensionPixelSize(R.dimen.hud_z);
        }
    }

    private class Player extends ImageView implements GameView {
        private final float[] sHull = new float[]{
                0.3f, 0f,    // left antenna
                0.7f, 0f,    // right antenna
                0.92f, 0.33f, // off the right shoulder of Orion
                0.92f, 0.75f, // right hand (our right, not his right)
                0.6f, 1f,    // right foot
                0.4f, 1f,    // left foot BLUE!
                0.08f, 0.75f, // sinistram
                0.08f, 0.33f,  // cold shoulder
        };
        public final float[] corners = new float[sHull.length];
        public float dv;
        private boolean mBoosting;

        public Player(Context context) {
            super(context);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setBackgroundResource(R.drawable.android);
            } else {
                Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.android, null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    setBackground(drawable);
                else
                    setBackgroundDrawable(drawable);
            }
            Misc.setTint(getBackground(), 0xFF00FF00);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setOutlineProvider(new ViewOutlineProvider() {
                    @Override
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    public void getOutline(View view, Outline outline) {
                        final int w = view.getWidth();
                        final int h = view.getHeight();
                        final int ix = (int) (w * 0.3f);
                        final int iy = (int) (h * 0.2f);
                        outline.setRect(ix, iy, w - ix, h - iy);
                    }
                });
            }

        }

        public void prepareCheckIntersections() {
            final int inset = (PARAMS.PLAYER_SIZE - PARAMS.PLAYER_HIT_SIZE) / 2;
            final int scale = PARAMS.PLAYER_HIT_SIZE;
            final int N = sHull.length / 2;
            for (int i = 0; i < N; i++) {
                corners[i * 2] = scale * sHull[i * 2] + inset;
                corners[i * 2 + 1] = scale * sHull[i * 2 + 1] + inset;
            }
            final Matrix m = getMatrix();
            m.mapPoints(corners);
        }

        public boolean below(int h) {
            final int N = corners.length / 2;
            for (int i = 0; i < N; i++) {
                final int y = (int) corners[i * 2 + 1];
                if (y >= h) return true;
            }
            return false;
        }

        public void step(long t_ms, long dt_ms, float t, float dt) {
            if (getVisibility() != View.VISIBLE) return; // not playing yet

            if (mBoosting) {
                dv = -PARAMS.BOOST_DV;
            } else {
                dv += PARAMS.G;
            }
            if (dv < -PARAMS.MAX_V) dv = -PARAMS.MAX_V;
            else if (dv > PARAMS.MAX_V) dv = PARAMS.MAX_V;

            final float y = getTranslationY() + dv * dt;
            setTranslationY(y < 0 ? 0 : y);
            setRotation(
                    90 + lerp(clamp(rlerp(dv, PARAMS.MAX_V, -1 * PARAMS.MAX_V)), 90, -90));

            prepareCheckIntersections();
        }

        public void boost() {
            mBoosting = true;
            dv = -PARAMS.BOOST_DV;

            animate().cancel();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                animate()
                        .scaleX(1.25f)
                        .scaleY(1.25f)
                        .translationZ(PARAMS.PLAYER_Z_BOOST)
                        .setDuration(100);
            } else {
                animate()
                        .scaleX(1.25f)
                        .scaleY(1.25f)
                        .setDuration(100);
            }
            setScaleX(1.25f);
            setScaleY(1.25f);
        }

        public void unboost() {
            mBoosting = false;

            animate().cancel();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .translationZ(PARAMS.PLAYER_Z)
                        .setDuration(200);
            } else {
                animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(200);
            }
        }
    }

    private class Obstacle extends View implements GameView {
        public final Rect hitRect = new Rect();
        public float h;

        public Obstacle(Context context, float h) {
            super(context);
            setBackgroundColor(0xFFFF0000);
            this.h = h;
        }

        public boolean intersects(Player p) {
            final int N = p.corners.length / 2;
            for (int i = 0; i < N; i++) {
                final int x = (int) p.corners[i * 2];
                final int y = (int) p.corners[i * 2 + 1];
                if (hitRect.contains(x, y)) return true;
            }
            return false;
        }

        public boolean cleared(Player p) {
            final int N = p.corners.length / 2;
            for (int i = 0; i < N; i++) {
                final int x = (int) p.corners[i * 2];
                if (hitRect.right >= x) return false;
            }
            return true;
        }

        @Override
        public void step(long t_ms, long dt_ms, float t, float dt) {
            setTranslationX(getTranslationX() - PARAMS.TRANSLATION_PER_SEC * dt);
            getHitRect(hitRect);
        }
    }

    private class Pop extends Obstacle {
        int mRotate;
        int cx, cy, r;

        public Pop(Context context, float h) {
            super(context, h);
            int idx = 2 * irand(0, POPS.length / 2);
            int res_id = POPS[idx];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setBackgroundResource(res_id);
            } else {
                Drawable drawable = VectorDrawableCompat.create(getResources(), res_id, null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    setBackground(drawable);
                else
                    setBackgroundDrawable(drawable);
            }
            setScaleX(frand() < 0.5f ? -1 : 1);
            mRotate = POPS[idx + 1] == 0 ? 0 : (frand() < 0.5f ? -1 : 1);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setOutlineProvider(new ViewOutlineProvider() {
                    @Override
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    public void getOutline(View view, Outline outline) {
                        final int pad = (int) (getWidth() * 0.02f);
                        outline.setOval(pad, pad, getWidth() - pad, getHeight() - pad);
                    }
                });
            }
        }

        public boolean intersects(Player p) {
            final int N = p.corners.length / 2;
            for (int i = 0; i < N; i++) {
                final int x = (int) p.corners[i * 2];
                final int y = (int) p.corners[i * 2 + 1];
                if (Math.hypot(x - cx, y - cy) <= r) return true;
            }
            return false;
        }

        @Override
        public void step(long t_ms, long dt_ms, float t, float dt) {
            super.step(t_ms, dt_ms, t, dt);
            if (mRotate != 0) {
                setRotation(getRotation() + dt * 45 * mRotate);
            }

            cx = (hitRect.left + hitRect.right) / 2;
            cy = (hitRect.top + hitRect.bottom) / 2;
            r = getWidth() / 2;
        }
    }

    private class Stem extends Obstacle {
        Paint mPaint = new Paint();
        Path mShadow = new Path();
        boolean mDrawShadow;

        public Stem(Context context, float h, boolean drawShadow) {
            super(context, h);
            mDrawShadow = drawShadow;
            mPaint.setColor(0xFFAAAAAA);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                setBackground(null);
            else
                setBackgroundDrawable(null);
        }

        @Override
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            setWillNotDraw(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                setOutlineProvider(new ViewOutlineProvider() {
                    @Override
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    public void getOutline(View view, Outline outline) {
                        outline.setRect(0, 0, getWidth(), getHeight());
                    }
                });
            }
        }

        @Override
        public void onDraw(Canvas c) {
            final int w = c.getWidth();
            final int h = c.getHeight();
            final GradientDrawable g;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                g = new GradientDrawable();
                g.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                g.setColors(new int[]{0xFFFFFFFF, 0xFFAAAAAA});
            } else {
                g = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xFFFFFFFF, 0xFFAAAAAA});
            }

            g.setGradientCenter(w * 0.75f, 0);
            g.setBounds(0, 0, w, h);
            g.draw(c);
            if (!mDrawShadow) return;
            mShadow.reset();
            mShadow.moveTo(0, 0);
            mShadow.lineTo(w, 0);
            mShadow.lineTo(w, PARAMS.OBSTACLE_WIDTH / 2 + w * 1.5f);
            mShadow.lineTo(0, PARAMS.OBSTACLE_WIDTH / 2);
            mShadow.close();
            c.drawPath(mShadow, mPaint);
        }
    }

    private class Scenery extends FrameLayout implements GameView {
        public float z;
        public float v;
        public int h, w;

        public Scenery(Context context) {
            super(context);
        }

        @Override
        public void step(long t_ms, long dt_ms, float t, float dt) {
            setTranslationX(getTranslationX() - PARAMS.TRANSLATION_PER_SEC * dt * v);
        }
    }

    private class Building extends Scenery {
        public Building(Context context) {
            super(context);

            w = irand(PARAMS.BUILDING_WIDTH_MIN, PARAMS.BUILDING_WIDTH_MAX);
            h = 0; // will be setup later, along with z

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                setTranslationZ(PARAMS.SCENERY_Z);
        }
    }

    private class Cloud extends Scenery {
        public Cloud(Context context) {
            super(context);
            int res_id = frand() < 0.01f ? R.drawable.cloud_off : R.drawable.cloud;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setBackgroundResource(res_id);
            } else {
                Drawable drawable = VectorDrawableCompat.create(getResources(), res_id, null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    setBackground(drawable);
                else
                    setBackgroundDrawable(drawable);
            }
            getBackground().setAlpha(0x40);
            w = h = irand(PARAMS.CLOUD_SIZE_MIN, PARAMS.CLOUD_SIZE_MAX);
            z = 0;
            v = frand(0.15f, 0.5f);
        }
    }

    private class Star extends Scenery {
        public Star(Context context) {
            super(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setBackgroundResource(R.drawable.star);
            } else {
                Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.star, null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    setBackground(drawable);
                else
                    setBackgroundDrawable(drawable);
            }
            w = h = irand(PARAMS.STAR_SIZE_MIN, PARAMS.STAR_SIZE_MAX);
            v = z = 0;
        }
    }
}
