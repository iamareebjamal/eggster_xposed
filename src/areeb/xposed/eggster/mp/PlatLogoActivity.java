package areeb.xposed.eggster.mp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import areeb.xposed.eggster.R;

// TODO: No ripple effect. Sorry.

public class PlatLogoActivity extends Activity {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_m_easter_egg);
		ImageView mmmTasty = (ImageView) findViewById(R.id.mLogo);
		ViewHelper.setScaleX(mmmTasty, 0.5F);
		ViewHelper.setScaleY(mmmTasty, 0.5F);
		ViewHelper.setAlpha(mmmTasty, 0);

		ViewPropertyAnimator.animate(mmmTasty).setStartDelay(1000)
			  .alpha(1).scaleX(1).scaleY(1)
			  .setInterpolator(new DecelerateInterpolator()).setDuration(600).start();
			  
		mmmTasty.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick (View v) {
				Toast.makeText(PlatLogoActivity.this, "¯\\_(ツ)_/¯", Toast.LENGTH_LONG).show();
				finish();
				return true;
			}
		});

        String sysUIMode = (getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getString("mp_sysui", getString(R.string.pref_none)));

        if (sysUIMode.equals(getString(R.string.pref_none)))
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (sysUIMode.equals(getString(R.string.pref_translucent))) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else if (sysUIMode.equals(getString(R.string.pref_immerge)))
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY); // immerge
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Boolean forcePort = getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getBoolean("mp_force_port", false);

        if (forcePort == true) {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }

    }
}
