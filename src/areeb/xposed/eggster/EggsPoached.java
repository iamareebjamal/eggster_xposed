/*
 * Copyright (C) 2014 Areeb Jamal (iamareebjamal)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */




package areeb.xposed.eggster;



import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;


public class EggsPoached implements IXposedHookLoadPackage, IXposedHookZygoteInit
{

	Activity mActivity;

	XSharedPreferences xpref = new XSharedPreferences("areeb.xposed.eggster", "easter_preference");
	Boolean enabled;
	String chosenEgg;



	private static final String HOOKED_PACKAGE = "com.cosmic.mods", HOOKED_CLASS = "com.cosmic.mods.PlatLogoActivity", HOOK_CLASS = "com.android.internal.app.PlatLogoActivity", HOOK_METHOD = "onCreate";


	int GB = 0, HC = 1, ICS = 2, JB = 3, KK = 4, L = 5, LP = 6;

	String[] versionName = {"Gingerbread", "Honeycomb", "Ice Cream Sandwich", "Jelly Bean", "Kitkat", "Android L Preview", "Lollipop"}, versionSName = {"GB", "HC", "ICS", "JB", "KK", "L", "LP"};
	int[] versionCode = {10, 11, 14, 16, 19, 20, 21};


	private static final String PACKAGE = "areeb.xposed.eggster";
	private static final String[] platActs = {"areeb.xposed.eggster.gb.PlatLogoActivity", "areeb.xposed.eggster.hc.PlatLogoActivity", "areeb.xposed.eggster.ics.PlatLogoActivity", "areeb.xposed.eggster.jb.PlatLogoActivity", "areeb.xposed.eggster.kk.PlatLogoActivity", "areeb.xposed.eggster.l.PlatLogoActivity", "areeb.xposed.eggster.lp.PlatLogoActivity"};

	private static final int version = Build.VERSION.SDK_INT;
	String CURRENT_VERSION;






	final Class <?> hookClass = findClass(HOOK_CLASS , null);


	public void initZygote(StartupParam startupParam) throws Throwable
	{
		XC_MethodHook hook = new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable
			{

				if (version >= versionCode[GB])
				{

					CURRENT_VERSION = versionSName[GB];

				}
				else if (version >= versionCode[HC])
				{

					CURRENT_VERSION = versionSName[HC];

				}
				else if (version >= versionCode[ICS])
				{

					CURRENT_VERSION = versionSName[ICS];

				}
				else if (version >= versionCode[JB])
				{

					CURRENT_VERSION = versionSName[JB];

				}
				else if (version >= versionCode[KK])
				{

					CURRENT_VERSION = versionSName[KK];

				}
				else if (version >= versionCode[L])
				{

					CURRENT_VERSION = versionSName[L];

				}
				else if (version >= versionCode[LP])
				{

					CURRENT_VERSION = versionSName[LP];

				}

				xpref.reload();
				enabled = xpref.getBoolean("enabled", true);
				chosenEgg = xpref.getString("egg_name", CURRENT_VERSION);

				String chosenPackage = PACKAGE;
				String chosenAct = "";

				if (chosenEgg.equals(versionSName[GB]))
				{

					chosenAct = platActs[GB];

				}
				else if (chosenEgg.equals(versionSName[HC]))
				{

					chosenAct = platActs[HC];

				}
				else if (chosenEgg.equals(versionSName[ICS]))
				{

					chosenAct = platActs[ICS];

				}
				else if (chosenEgg.equals(versionSName[JB]))
				{

					chosenAct = platActs[JB];

				}
				else if (chosenEgg.equals(versionSName[KK]))
				{

					chosenAct = platActs[KK];

				}
				else if (chosenEgg.equals(versionSName[L]))
				{

					chosenAct = platActs[L];

				}
				else if (chosenEgg.equals(versionSName[LP]))
				{

					chosenAct = platActs[LP];

				}


				if (enabled == true)
				{

					mActivity = (Activity) param.thisObject;

					Context mContext = (Context) callMethod(param.thisObject, "getApplicationContext");

					Intent i = new Intent(Intent.ACTION_MAIN);
					i.setComponent(new ComponentName(chosenPackage, chosenAct));

					//Toast.makeText(mContext, "Done", Toast.LENGTH_SHORT).show();

					try
					{

						callMethod(mActivity, "finish");

						mActivity.finish(); //getting sure


						callMethod(mActivity, "startActivity", i);


					}
					catch (ActivityNotFoundException e)
					{


						Toast.makeText(mContext, "No substitute found! Reverting to original.", Toast.LENGTH_SHORT).show();

					}

					logIt("All methods called");
				}
			}
		};

		findAndHookMethod(hookClass, HOOK_METHOD, Bundle.class, hook);
	}





	@Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable
	{


        if (lpparam.packageName.equals(HOOKED_PACKAGE))
		{



			//XposedBridge.log(lpparam.packageName + "loaded");


			final Class<?> platlogoActivityClass = findClass(HOOKED_CLASS, lpparam.classLoader);
			findAndHookMethod(platlogoActivityClass,  HOOK_METHOD, Bundle.class, new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable
					{
						//XposedBridge.log(HOOK_CLASS + "Hook Successful");

						xpref.reload();
						enabled = xpref.getBoolean("enabled", true);
						chosenEgg = xpref.getString("egg_name", "KK");





						if (enabled == true)
						{

							mActivity = (Activity) param.thisObject;

							callMethod(mActivity, "finish");

							logIt("Jelly Bean Closed");

						}

					}
				});

		}


		

	}
	
	
	public void logIt(String string){
		
		xpref.reload();
		Boolean log = xpref.getBoolean("log", false);
		
		if (log == true){
			
			XposedBridge.log(string);
			
		}
		
	}







}
