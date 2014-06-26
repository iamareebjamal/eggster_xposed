package areeb.xposed.eggster;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.SeekBar;

  @SuppressLint("DrawAllocation")
public class CenterBar extends SeekBar {

private Rect rect;
private Paint paint ;
private int seekbar_height;

public CenterBar(Context context) {
    super(context);
    super.setProgressDrawable(new ColorDrawable(android.R.color.transparent));
}

public CenterBar(Context context, AttributeSet attrs) {

    super(context, attrs);
    rect = new Rect();
    paint = new Paint();
    seekbar_height = 6;
    super.setProgressDrawable(new ColorDrawable(android.R.color.transparent));
}

public CenterBar(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    super.setProgressDrawable(new ColorDrawable(android.R.color.transparent));
}



@Override
protected synchronized void onDraw(Canvas canvas) {
    super.setProgressDrawable(new ColorDrawable(android.R.color.transparent));
    rect.set(0 + getThumbOffset(),
            (getHeight() / 2) - (seekbar_height/2),
            getWidth()- getThumbOffset(),
            (getHeight() / 2) + (seekbar_height/2));

    paint.setColor(Color.GRAY);

    canvas.drawRect(rect, paint);



    if (this.getProgress() > 50) {


        rect.set(getWidth() / 2,
                (getHeight() / 2) - (seekbar_height/2),
                getWidth() / 2 + (getWidth() / 100) * (getProgress() - 53),
                getHeight() / 2 + (seekbar_height/2));
        paint.setColor(Color.parseColor("#FF33B5E5"));
        canvas.drawRect(rect, paint);

    }

    if (this.getProgress() < 50) {

        rect.set(getWidth() / 2 - ((getWidth() / 100) * (47 - getProgress())),
                (getHeight() / 2) - (seekbar_height/2),
                 getWidth() / 2,
                 getHeight() / 2 + (seekbar_height/2));

        paint.setColor(Color.parseColor("#FF33B5E5"));
        canvas.drawRect(rect, paint);
    }
    super.setThumb(getResources().getDrawable(R.drawable.cyanothumb));
    super.setProgressDrawable(new ColorDrawable(android.R.color.transparent));
    super.onDraw(canvas);
    }
}