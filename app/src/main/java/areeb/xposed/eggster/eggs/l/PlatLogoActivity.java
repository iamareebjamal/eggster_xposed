package areeb.xposed.eggster.eggs.l;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import java.util.Random;

public class PlatLogoActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        FrameLayout content = new FrameLayout(this);
        content.setBackgroundColor(-1);
        final View rct1 = new View(this), rct2 = new View(this);
        TextView build = new TextView(this);
        final Random r = new Random();
        // DisplayMetrics metrics = new DisplayMetrics();
        LayoutParams pos1 = new LayoutParams(r.nextInt(1000), r.nextInt(1000)), pos2 = new LayoutParams(
                r.nextInt(1000), r.nextInt(1000)), label = new LayoutParams(-2,
                -2);

        build.setText("android_l.flv - build 1236599");
        build.setTextSize(18);
        build.setTextColor(Color.BLACK);
        pos1.topMargin = r.nextInt(1000);
        pos1.leftMargin = r.nextInt(500);
        pos2.topMargin = r.nextInt(1000);
        pos2.leftMargin = r.nextInt(500);
        // final int p = (int) (4 * metrics.density);
        rct1.setBackgroundColor(Color.RED);
        rct2.setBackgroundColor(Color.BLUE);

        super.onCreate(savedInstanceState);

        setContentView(content);

        label.gravity = Gravity.LEFT | Gravity.BOTTOM;

        content.addView(rct2);
        content.addView(rct1);
        content.addView(build, label);
        rct1.setLayoutParams(pos1);
        rct2.setLayoutParams(pos2);

        new CountDownTimer(Integer.MAX_VALUE, 1000) {
            public void onTick(long millisUntilFinished) {
                LayoutParams newpos1 = new LayoutParams(r.nextInt(1000),
                        r.nextInt(1000)), newpos2 = new LayoutParams(
                        r.nextInt(1000), r.nextInt(1000));
                newpos1.topMargin = r.nextInt(200);
                newpos1.leftMargin = r.nextInt(200);
                newpos2.topMargin = r.nextInt(200);
                newpos2.leftMargin = r.nextInt(200);
                rct1.setLayoutParams(newpos1);
                rct2.setLayoutParams(newpos2);
            }

            @Override
            public void onFinish() {
            }
        }.start();

        content.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(Intent.ACTION_MAIN).setFlags(
                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                        .setClassName("areeb.xposed.eggster",
                                "areeb.xposed.eggster.eggs.kk.DessertCase"));


                finish();
                return true;
            }
        });
    }

}