package areeb.xposed.eggster;

import de.robv.android.xposed.XSharedPreferences;

public class XPreferenceManager {

    private static XSharedPreferences preferences = new XSharedPreferences(EggsPoached.PACKAGE_NAME, "easter_preference");
    private static final String EGG_NAME = "egg_name";
    private static final String ENABLED = "enabled";
    private static final String LOGGING = "log";

    public static boolean isEnabled() {
        preferences.reload();
        return preferences.getBoolean(ENABLED, true);
    }

    public static void setEnabled(boolean enabled){
        preferences.edit().putBoolean(ENABLED, enabled).commit();
    }

    public static boolean isLoggingEnable() {
        preferences.reload();
        return preferences.getBoolean(LOGGING, false);
    }

    public static void setLoggingEnabled(boolean enabled){
        preferences.edit().putBoolean(LOGGING, enabled).commit();
    }

    public static String getEasterEgg() {
        preferences.reload();
        return preferences.getString(EGG_NAME, Egg.getSystemEgg().getId());
    }

    public static void setEasterEgg(Egg egg){
        preferences.edit().putString(EGG_NAME, egg.getId()).commit();
    }

}
