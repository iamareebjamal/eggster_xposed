package areeb.xposed.eggster.m;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import areeb.xposed.eggster.R;

public class MLandActivity extends Activity {
    MLand mLand;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mland);
        mLand = (MLand) findViewById(R.id.world);
        mLand.setScoreFieldHolder((ViewGroup) findViewById(R.id.scores));
        final View welcome = findViewById(R.id.welcome);
        mLand.setSplash(welcome);
        final int numControllers = mLand.getGameControllers().size();
        if (numControllers > 0) {
            mLand.setupPlayers(numControllers);
        }

        // Vector Fixes
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            ImageView play = (ImageView) findViewById(R.id.play_button_image);
            Drawable playImg = VectorDrawableCompat.create(getResources(), R.drawable.play, null);
            play.setImageDrawable(playImg);

            ImageButton plus = (ImageButton) findViewById(R.id.player_plus_button);
            Drawable plusImg = VectorDrawableCompat.create(getResources(), R.drawable.plus, null);
            plus.setImageDrawable(plusImg);

            ImageButton minus = (ImageButton) findViewById(R.id.player_minus_button);
            Drawable minusImg = VectorDrawableCompat.create(getResources(), R.drawable.minus, null);
            minus.setImageDrawable(minusImg);
        }
    }

    public void updateSplashPlayers() {
        final int N = mLand.getNumPlayers();
        final View minus = findViewById(R.id.player_minus_button);
        final View plus = findViewById(R.id.player_plus_button);
        if (N == 1) {
            minus.setVisibility(View.INVISIBLE);
            plus.setVisibility(View.VISIBLE);
            plus.requestFocus();
        } else if (N == mLand.MAX_PLAYERS) {
            minus.setVisibility(View.VISIBLE);
            plus.setVisibility(View.INVISIBLE);
            minus.requestFocus();
        } else {
            minus.setVisibility(View.VISIBLE);
            plus.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        mLand.stop();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        mLand.onAttachedToWindow(); // resets and starts animation
        updateSplashPlayers();
        mLand.showSplash();
    }

    public void playerMinus(View v) {
        mLand.removePlayer();
        updateSplashPlayers();
    }

    public void playerPlus(View v) {
        mLand.addPlayer();
        updateSplashPlayers();
    }

    public void startButtonPressed(View v) {
        findViewById(R.id.player_minus_button).setVisibility(View.INVISIBLE);
        findViewById(R.id.player_plus_button).setVisibility(View.INVISIBLE);
        mLand.start(true);
    }
}