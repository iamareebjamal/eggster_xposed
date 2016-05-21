package areeb.xposed.eggster;

import de.robv.android.xposed.XSharedPreferences;

public class XPreferenceManager {

    private static XSharedPreferences preferences = new XSharedPreferences(EggsPoached.PACKAGE_NAME, "easter_preference");

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
        return preferences.getString("egg_name", Egg.getSystemEgg().getId());
    }


}
