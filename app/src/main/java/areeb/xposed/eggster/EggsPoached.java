package areeb.xposed.eggster;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

import static de.robv.android.xposed.XposedHelpers.*;

public class EggsPoached implements IXposedHookZygoteInit {


    public static final String PACKAGE_NAME = "areeb.xposed.eggster";

    private static final String HOOK_CLASS = "com.android.internal.app.PlatLogoActivity", HOOK_METHOD = "onCreate";

    private final Class<?> hookedClass = findClass(HOOK_CLASS, null);

    public static void log(String string) {
        if (XPreferenceManager.isLoggingEnable()) {
            XposedBridge.log(string);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {

        XC_MethodHook methodHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Egg egg = Egg.getEggFromId(XPreferenceManager.getEasterEgg());

                if (XPreferenceManager.isEnabled() && egg != null) {
                    Activity platLogoActivity = (Activity) param.thisObject;

                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setComponent(new ComponentName(PACKAGE_NAME, egg.getPackage()));

                    Toast.makeText(platLogoActivity, "Done", Toast.LENGTH_SHORT).show();

                    try {

                        callMethod(platLogoActivity, "finish");
                        platLogoActivity.finish(); //getting sure
                        callMethod(platLogoActivity, "startActivity", intent);

                        log("Success");

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
}
