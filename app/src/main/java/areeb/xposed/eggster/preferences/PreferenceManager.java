package areeb.xposed.eggster.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import areeb.xposed.eggster.Egg;
import areeb.xposed.eggster.EggsPoached;

public class PreferenceManager {

    private SharedPreferences preferences;
    public static final String EGG_NAME = "egg_name";
    public static final String ENABLED = "enabled";
    public static final String LOGGING = "log";

    public PreferenceManager(Context context){
        preferences = context.getSharedPreferences("easter_preference", Context.MODE_WORLD_READABLE);
    }

    public boolean isEnabled() {
        return preferences.getBoolean(ENABLED, true);
    }

    public void setEnabled(boolean enabled){
        preferences.edit().putBoolean(ENABLED, enabled).commit();
    }

    public boolean isLoggingEnable() {
        return preferences.getBoolean(LOGGING, false);
    }

    public void setLoggingEnabled(boolean enabled){
        preferences.edit().putBoolean(LOGGING, enabled).commit();
    }

    public String getEasterEgg() {
        return isEnabled()?preferences.getString(EGG_NAME, null):null;
    }

    public void setEasterEgg(Egg egg){
        preferences.edit().putString(EGG_NAME, egg.getId()).commit();
    }

}
