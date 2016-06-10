package areeb.xposed.eggster.preferences;

import areeb.xposed.eggster.Egg;
import areeb.xposed.eggster.EggsPoached;
import de.robv.android.xposed.XSharedPreferences;

public class XPreferenceManager {

    private static XSharedPreferences preferences = new XSharedPreferences(EggsPoached.PACKAGE_NAME, "easter_preference");
    static {
        preferences.makeWorldReadable();
    }

    public static boolean isEnabled() {
        preferences.reload();
        return preferences.getBoolean(PreferenceManager.ENABLED, true);
    }

    public static boolean isLoggingEnable() {
        preferences.reload();
        return preferences.getBoolean(PreferenceManager.LOGGING, false);
    }

    public static String getEasterEgg() {
        preferences.reload();
        return preferences.getString(PreferenceManager.EGG_NAME, Egg.getSystemEgg().getId());
    }
}
