package areeb.xposed.eggster;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import areeb.xposed.eggster.preferences.PreferenceManager;
import areeb.xposed.eggster.preferences.XPreferenceManager;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.*;

public class EggsPoached implements IXposedHookZygoteInit, IXposedHookLoadPackage {

    public static final String PACKAGE_NAME = "areeb.xposed.eggster";
    private static final String HOOK_CLASS = "com.android.internal.app.PlatLogoActivity", HOOK_METHOD = "onCreate";

    private final Class<?> hookedClass = findClass(HOOK_CLASS, null);
    private XPreferenceManager xpref;

    public void log(String string) {
        if (xpref.isLoggingEnable()) {
            XposedBridge.log(string);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        XC_MethodHook methodHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                Activity platLogoActivity = (Activity) param.thisObject;
                xpref = new XPreferenceManager(platLogoActivity.getApplicationContext());

                Egg egg = Egg.getEggFromId(xpref.getEasterEgg());
                if (xpref.isEnabled() && egg != null) {


                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    intent.setComponent(new ComponentName(PACKAGE_NAME, egg.getPackage()));

                    try {
                        callMethod(platLogoActivity, "finish");
                        callMethod(platLogoActivity, "startActivity", intent);

                    } catch (ActivityNotFoundException ane) {
                        Toast.makeText((Context) callMethod(param.thisObject, "getApplicationContext"),
                                "No substitute found! Reverting to original.", Toast.LENGTH_SHORT).show();

                        log("Reverting to original");
                    }

                }
            }
        };

        findAndHookMethod(hookedClass, HOOK_METHOD, Bundle.class, methodHook);
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals(PACKAGE_NAME))
            findAndHookMethod(PreferenceManager.class.getName(), loadPackageParam.classLoader,
                    "isModuleActive", XC_MethodReplacement.returnConstant(true));
    }

}
