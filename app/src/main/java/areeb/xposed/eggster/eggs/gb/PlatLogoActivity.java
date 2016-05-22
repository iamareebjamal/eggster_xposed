package areeb.xposed.eggster.eggs.gb;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;
import areeb.xposed.eggster.R;

public class PlatLogoActivity extends Activity {
    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToast = Toast.makeText(this, "Zombie art by Jack Larson", Toast.LENGTH_SHORT);

        ImageView content = new ImageView(this);
        content.setBackgroundColor(0xFF000000);
        content.setImageResource(R.drawable.platlogogb);
        content.setScaleType(ImageView.ScaleType.FIT_CENTER);

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