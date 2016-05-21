package areeb.xposed.eggster.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;

public class Misc {

    // TODO : Create general methods of loading drawables, setBackground, clipping views and translationZ

    @ColorInt
    public static int HSBtoColor(float h, float s, float b) {
        h = constrain(h, 0.0f, 1.0f);
        s = constrain(s, 0.0f, 1.0f);
        b = constrain(b, 0.0f, 1.0f);

        float red = 0.0f;
        float green = 0.0f;
        float blue = 0.0f;

        final float hf = (h - (int) h) * 6.0f;
        final int ihf = (int) hf;
        final float f = hf - ihf;
        final float pv = b * (1.0f - s);
        final float qv = b * (1.0f - s * f);
        final float tv = b * (1.0f - s * (1.0f - f));

        switch (ihf) {
            case 0:         // Red is the dominant color
                red = b;
                green = tv;
                blue = pv;
                break;
            case 1:         // Green is the dominant color
                red = qv;
                green = b;
                blue = pv;
                break;
            case 2:
                red = pv;
                green = b;
                blue = tv;
                break;
            case 3:         // Blue is the dominant color
                red = pv;
                green = qv;
                blue = b;
                break;
            case 4:
                red = tv;
                green = pv;
                blue = b;
                break;
            case 5:         // Red is the dominant color
                red = b;
                green = pv;
                blue = qv;
                break;
        }

        return 0xFF000000 | (((int) (red * 255.0f)) << 16) |
                (((int) (green * 255.0f)) << 8) | ((int) (blue * 255.0f));
    }

    public static float constrain(float amount, float low, float high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    public static void drawArc(Canvas c, float left, float top, float right, float bottom, float startAngle,
                        float sweepAngle, boolean useCenter, @NonNull Paint paint) {
        c.drawArc(new RectF(left, top, right, bottom), startAngle, sweepAngle, useCenter, paint);
    }

    public static void setTint(Drawable drawable, int color){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            drawable.setTintMode(PorterDuff.Mode.SRC_ATOP);
            drawable.setTint(color);
        } else {
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            wrappedDrawable = wrappedDrawable.mutate();
            DrawableCompat.setTintMode(wrappedDrawable, PorterDuff.Mode.SRC_ATOP);
            DrawableCompat.setTint(wrappedDrawable, color);
            DrawableCompat.unwrap(wrappedDrawable);
        }
    }
}
