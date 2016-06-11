package areeb.xposed.eggster.preferences;

import areeb.xposed.eggster.EggsPoached;
import com.crossbowffs.remotepreferences.RemotePreferenceProvider;

public class XPreferenceProvider extends RemotePreferenceProvider {

    public XPreferenceProvider() {
        super(EggsPoached.PACKAGE_NAME, new String[]{PreferenceManager.PREF_NAME});
    }

}
