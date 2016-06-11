package areeb.xposed.eggster.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import areeb.xposed.eggster.Egg;

public class PreferenceManager {

    public static final String PREF_NAME = "easter_preference";
    public static final String EGG_NAME = "egg_name";
    public static final String ENABLED = "enabled";
    public static final String LOGGING = "log";
    private SharedPreferences preferences;

    public PreferenceManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_WORLD_READABLE);
    }

    public boolean isEnabled() {
        return preferences.getBoolean(ENABLED, true);
    }

    public void setEnabled(boolean enabled) {
        preferences.edit().putBoolean(ENABLED, enabled).commit();
    }

    public boolean isLoggingEnable() {
        return preferences.getBoolean(LOGGING, false);
    }

    public void setLoggingEnabled(boolean enabled) {
        preferences.edit().putBoolean(LOGGING, enabled).commit();
    }

    public String getEasterEgg() {
        return isEnabled() ? preferences.getString(EGG_NAME, null) : null;
    }

    public void setEasterEgg(Egg egg) {
        preferences.edit().putString(EGG_NAME, egg.getId()).commit();
    }

    public static boolean isModuleActive(){
        return false;
    }

}
