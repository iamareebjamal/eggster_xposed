package areeb.xposed.eggster;

import de.robv.android.xposed.XSharedPreferences;

public class XPreferenceManager {

    private static XSharedPreferences preferences = new XSharedPreferences(EggsPoached.PACKAGE_NAME, "easter_preference");
    private static final String EGG_NAME = "egg_name";

    public static boolean isEnabled() {
        preferences.reload();
        return preferences.getBoolean("enabled", true);
    }

    public static boolean isLoggingEnable() {
        preferences.reload();
        return preferences.getBoolean("log", false);
    }

    public static String getEasterEgg() {
        preferences.reload();
        return preferences.getString(EGG_NAME, Egg.getSystemEgg().getId());
    }

    public static void setEasterEgg(Egg egg){
        preferences.edit().putString(EGG_NAME, egg.getId()).commit();
    }

}
