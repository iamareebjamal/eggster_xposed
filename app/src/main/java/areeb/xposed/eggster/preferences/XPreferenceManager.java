package areeb.xposed.eggster.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import areeb.xposed.eggster.Egg;
import areeb.xposed.eggster.EggsPoached;
import com.crossbowffs.remotepreferences.RemotePreferences;

public class XPreferenceManager {

    private SharedPreferences preferences;

    public XPreferenceManager(Context context) {
        preferences = new RemotePreferences(context, EggsPoached.PACKAGE_NAME, PreferenceManager.PREF_NAME);
    }

    public boolean isEnabled() {
        return preferences.getBoolean(PreferenceManager.ENABLED, true);
    }

    public boolean isLoggingEnable() {
        return preferences.getBoolean(PreferenceManager.LOGGING, false);
    }

    public String getEasterEgg() {
        return preferences.getString(PreferenceManager.EGG_NAME, Egg.getSystemEgg().getId());
    }
}
