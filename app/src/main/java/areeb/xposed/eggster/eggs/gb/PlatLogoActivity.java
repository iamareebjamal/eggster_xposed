package areeb.xposed.eggster.eggs.gb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import areeb.xposed.eggster.R;
import areeb.xposed.eggster.R.style;

@SuppressLint("ShowToast")
public class PlatLogoActivity extends Activity {
    private Toast mToast;


    @SuppressLint({"ShowToast", "InlinedApi", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);


        SharedPreferences pref = getSharedPreferences("preferenceggs", Context.MODE_PRIVATE);


        String gbToast = pref.getString("gb_toast_text", getString(R.string.pref_default_gb_text));
        Boolean transBG = pref.getBoolean("gb_transp_bg", false);
        String sysUIMode = pref.getString("gb_sysui", "Normal");

        if (sysUIMode.equals("Normal")) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        mToast = Toast.makeText(this, gbToast, Toast.LENGTH_SHORT);
        ImageView content = new ImageView(this);

        if (transBG) {
            content.setBackgroundColor(0xCA000000);
        } else {
            content.setBackgroundColor(0xFF000000);
        }

        content.setImageResource(R.drawable.platlogogb);
        content.setScaleType(ImageView.ScaleType.FIT_CENTER);

        if (Build.VERSION.SDK_INT >= 19 && sysUIMode.equals("Immersive Mode")) {
            content.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION        // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN            // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);    //Immersive
        } else if (Build.VERSION.SDK_INT >= 19 && sysUIMode.equals("Translucent Mode")) {
            super.setTheme(style.Wallpaper_TranslucentDecor);
        }


        setContentView(content);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Boolean forcePort = getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getBoolean("gb_force_port", false);

        if (forcePort) {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            mToast.show();
        }
        return super.dispatchTouchEvent(ev);
    }
}