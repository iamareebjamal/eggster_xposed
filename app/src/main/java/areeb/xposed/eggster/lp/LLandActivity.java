package areeb.xposed.eggster.lp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import areeb.xposed.eggster.R;


public class LLandActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lland);
        LLand world = (LLand) findViewById(R.id.world);
        world.setScoreField((TextView) findViewById(R.id.score));
        world.setSplash(findViewById(R.id.welcome));
        Log.v(LLand.TAG, "focus: " + world.requestFocus());
    }
}