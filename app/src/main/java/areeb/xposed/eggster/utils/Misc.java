package areeb.xposed.eggster.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Size;

public class Misc {

    @ColorInt
    public static int HSBtoColor(@Size(3) float[] hsb) {
        return HSBtoColor(hsb[0], hsb[1], hsb[2]);
    }

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
}
