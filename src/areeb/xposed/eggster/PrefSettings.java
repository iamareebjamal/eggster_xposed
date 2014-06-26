package areeb.xposed.eggster;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
@SuppressLint("DefaultLocale")
public class PrefSettings extends PreferenceActivity {
	
	
	/**
	 * Determines whether to always show the simplified settings UI, where
	 * settings are presented in a single list. When false, settings are shown
	 * as a master/detail two-pane view on tablets. When true, a single pane is
	 * shown on tablets.
	 */
	
	private static final boolean ALWAYS_SIMPLE_PREFS = false;
	
	@Override
	protected boolean isValidFragment(String fragmentName){
		
		//Fix FC by vulnerability fixing code
		
		if (GBPreferenceFragment.class.getName().equals(fragmentName) || HCPreferenceFragment.class.getName().equals(fragmentName) || ICSPreferenceFragment.class.getName().equals(fragmentName) || JBPreferenceFragment.class.getName().equals(fragmentName) || KKPreferenceFragment.class.getName().equals(fragmentName) || ABPreferenceFragment.class.getName().equals(fragmentName))
		{
			return true;
			
		}
		
		
		return false;
	}
	


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
		
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		setupSimplePreferencesScreen();
				
	}
		
	/**
	 * Shows the simplified settings UI if the device configuration if the
	 * device configuration dictates that a simplified, single-pane UI should be
	 * shown.
	 */
	
	
		
	@SuppressWarnings("deprecation")
	private void setupSimplePreferencesScreen() {
		if (!isSimplePreferences(this)) {
			return;
		}
		
		//Set up a custom name for preferences
		PreferenceManager prefMgr = getPreferenceManager();
		setPreferenceName(prefMgr);
		int p = getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getInt("kk_freq", 50);

		// In the simplified UI, fragments are not used at all and we instead
		// use the older PreferenceActivity APIs.

		// Add Base preference
		
		PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(this);
        setPreferenceScreen(screen);
		
		
		// Add GB preferences, and a corresponding header.
		PreferenceCategory fakeHeader = new PreferenceCategory(this);
		fakeHeader.setTitle(getString(R.string.pref_header_gb).toUpperCase());
		getPreferenceScreen().addPreference(fakeHeader);
		addPreferencesFromResource(R.xml.pref_gb);
		// Add HC preferences, and a corresponding header.
		fakeHeader = new PreferenceCategory(this);
		fakeHeader.setTitle(getString(R.string.pref_header_hc).toUpperCase());
		getPreferenceScreen().addPreference(fakeHeader);
		addPreferencesFromResource(R.xml.pref_hc);

		// Add ICS preferences, and a corresponding header.
		fakeHeader = new PreferenceCategory(this);
		fakeHeader.setTitle(getString(R.string.pref_header_ics).toUpperCase());
		getPreferenceScreen().addPreference(fakeHeader);
		addPreferencesFromResource(R.xml.pref_ics);
		
		// Add JB preferences, and a corresponding header.
		fakeHeader = new PreferenceCategory(this);
		fakeHeader.setTitle(getString(R.string.pref_header_jb).toUpperCase());
		getPreferenceScreen().addPreference(fakeHeader);
		addPreferencesFromResource(R.xml.pref_jb);
		
		
		// Add KK preferences, and a corresponding header.
		fakeHeader = new PreferenceCategory(this);
		fakeHeader.setTitle(getString(R.string.pref_header_kk).toUpperCase());
		getPreferenceScreen().addPreference(fakeHeader);
		addPreferencesFromResource(R.xml.pref_kk);
		
		fakeHeader = new PreferenceCategory(this);
		fakeHeader.setTitle(getString(R.string.pref_header_about).toUpperCase());
		getPreferenceScreen().addPreference(fakeHeader);
		addPreferencesFromResource(R.xml.pref_about);

		// Bind the summaries of preferences to
		// their values. When their values change, their summaries are updated
		// to reflect the new value, per the Android Design guidelines.
		bindPreferenceSummaryToValue(findPreference("gb_toast_text"));
		bindPreferenceSummaryToValue(findPreference("gb_sysui"));
		bindPreferenceSummaryToValue(findPreference("hc_toast_text"));
		bindPreferenceSummaryToValue(findPreference("hc_sysui"));
		bindPreferenceSummaryToValue(findPreference("ics_toast_text"));
		bindPreferenceSummaryToValue(findPreference("ics_sysui_plat"));
		bindPreferenceSummaryToValue(findPreference("ics_sysui"));		
		bindPreferenceSummaryToValue(findPreference("number_of_cats"));
		bindPreferenceSummaryToValue(findPreference("jb_text_1"));
		bindPreferenceSummaryToValue(findPreference("jb_text_2"));
		bindPreferenceSummaryToValue(findPreference("number_of_jb"));
		bindPreferenceSummaryToValue(findPreference("jb_size"));
		bindPreferenceSummaryToValue(findPreference("jb_sysui_plat"));
		bindPreferenceSummaryToValue(findPreference("jb_sysui"));
		bindPreferenceSummaryToValue(findPreference("kk_text"));
		bindPreferenceSummaryToValue(findPreference("kk_letter"));
		bindPreferenceSummaryToValue(findPreference("kk_clicks"));
		bindPreferenceSummaryToValue(findPreference("kk_duration"));
		bindPreferenceSummaryToValue(findPreference("kk_interpolator"));
		bindPreferenceSummaryToValue(findPreference("kk_letter_size"));
		bindPreferenceSummaryToValue(findPreference("kk_text_size"));
		bindPreferenceSummaryToValue(findPreference("kk_sysui"));
		final Preference pref = findPreference("kk_freq");
		
		if (p == 50)
			pref.setSummary("Balanced");
		else if (p == 0)
			pref.setSummary("Special pastries will never be shown");
		else if (p <= 40 && p > 20)
			pref.setSummary("Special pastries will be rare");
		else if (p <= 20 && p != 0)
			pref.setSummary("Special pastries will be very rare");
		else if (p >= 60 && p < 80)
			pref.setSummary("Special pastries will be common");
		else if (p >= 80 && p != 100)
			pref.setSummary("Special pastries will be very common");
		else if (p == 100)
			pref.setSummary("Special pastries will be as common as normal ones");
		
		pref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				int t = Integer.parseInt(newValue.toString());
				if (t == 50)
					preference.setSummary("Balanced");
				else if (t == 0)
					preference.setSummary("Special pastries will never be shown");
				else if (t <= 40 && t > 20)
					preference.setSummary("Special pastries will be rare");
				else if (t <= 20 && t != 0)
					preference.setSummary("Special pastries will be very rare");
				else if (t >= 60 && t < 80)
					preference.setSummary("Special pastries will be common");
				else if (t >= 80 && t != 100)
					preference.setSummary("Special pastries will be very common");
				else if (t == 100)
					preference.setSummary("Special pastries will be as common as normal ones");
				return false;
			}
		});
		
	}
	

	/** {@inheritDoc} */
	@Override
	public boolean onIsMultiPane() {
		return true;
	}
	
	

	/**
	 * Helper method to determine if the device has an extra-large screen. For
	 * example, 10" tablets are extra-large.
	 */
	private static boolean isLargeTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}
	
	
	/**
	 * Determine if the device is extra Large, if it is not, then shows
	 * Multi Pane Window only in LandScape mode.
	 */
	private static boolean isOk(Context context){
		
		if (!isLargeTablet(context)){
			
			return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
				
			
		}
		return false;
		
	}

	/**
	 * Determines whether the simplified settings UI should be shown. This is
	 * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
	 * doesn't have newer APIs like {@link PreferenceFragment}, or the device
	 * doesn't have an extra-large screen, or it is in portrait mode. In these cases, a single-pane
	 * "simplified" settings UI should be shown.
	 */
	
	private static boolean isSimplePreferences(Context context) {
		return ALWAYS_SIMPLE_PREFS
				|| Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
				|| !isOk(context);
	}

	/** {@inheritDoc} */
	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target) {
		if (!isSimplePreferences(this)) {
			
			
			loadHeadersFromResource(R.xml.pref_egg_headers, target);
		}
	}

	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	
	/*
	 * We are not having any other preference than EditText Preference so removed unnecessary binding code
	 */
	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			
		
			String stringValue = String.valueOf(value);

			// For all other preferences, set the summary to the value's
			// simple string representation.
			
			preference.setSummary(stringValue);
			
			return true;
		}
	};

	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 * 
	 * @see #sBindPreferenceSummaryToValueListener
	 */
	private static void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// Trigger the listener immediately with the preference's
		// current value according to the instance type.
		
		if (preference instanceof SeekBarPreference){
			
			//Display default value if no preference chosen
			
			sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, preference.getContext().getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getInt(preference.getKey(), ((SeekBarPreference) preference).getDefaultValue()));
			
		} else if (preference instanceof CenterSeekBarPreference) {
			sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, preference.getContext().getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getInt(preference.getKey(), ((CenterSeekBarPreference) preference).getDefaultValue()));
		} else {
			
			sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, preference.getContext().getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getString(preference.getKey(),""));
			
		}
		
		
			
	}

	/**
	 * This fragment shows GingerBread preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class GBPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			PreferenceManager prefMgr = getPreferenceManager();
			setPreferenceName(prefMgr);
			
			addPreferencesFromResource(R.xml.pref_gb);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceSummaryToValue(findPreference("gb_toast_text"));
			bindPreferenceSummaryToValue(findPreference("gb_sysui"));			
		}
	}

	/**
	 * This fragment shows HoneyComb preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class HCPreferenceFragment extends
			PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			PreferenceManager prefMgr = getPreferenceManager();
			setPreferenceName(prefMgr);
			
			addPreferencesFromResource(R.xml.pref_hc);
			

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceSummaryToValue(findPreference("hc_toast_text"));
			bindPreferenceSummaryToValue(findPreference("hc_sysui"));
		}
	}

	/**
	 * This fragment shows ICS preference only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class ICSPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			PreferenceManager prefMgr = getPreferenceManager();
			setPreferenceName(prefMgr);
			
			addPreferencesFromResource(R.xml.pref_ics);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceSummaryToValue(findPreference("ics_toast_text"));
			bindPreferenceSummaryToValue(findPreference("number_of_cats"));
			bindPreferenceSummaryToValue(findPreference("ics_sysui_plat"));
			bindPreferenceSummaryToValue(findPreference("ics_sysui"));
		}
	}
	
	/**
	 * This fragment shows JB preference only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class JBPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			PreferenceManager prefMgr = getPreferenceManager();
			setPreferenceName(prefMgr);
			
			addPreferencesFromResource(R.xml.pref_jb);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceSummaryToValue(findPreference("jb_text_1"));
			bindPreferenceSummaryToValue(findPreference("jb_text_2"));
			bindPreferenceSummaryToValue(findPreference("number_of_jb"));
			bindPreferenceSummaryToValue(findPreference("jb_size"));
			bindPreferenceSummaryToValue(findPreference("jb_sysui_plat"));
			bindPreferenceSummaryToValue(findPreference("jb_sysui"));
			
		}
	}
	
	/**
	 * This fragment shows KK preference only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class KKPreferenceFragment extends PreferenceFragment {

		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			
			
			PreferenceManager prefMgr = getPreferenceManager();
			setPreferenceName(prefMgr);

			addPreferencesFromResource(R.xml.pref_kk);

			// Bind the summaries of EditText/List/Dialog/Ringtone preferences
			// to their values. When their values change, their summaries are
			// updated to reflect the new value, per the Android Design
			// guidelines.
			bindPreferenceSummaryToValue(findPreference("kk_letter"));
			bindPreferenceSummaryToValue(findPreference("kk_text"));
			bindPreferenceSummaryToValue(findPreference("kk_clicks"));
			bindPreferenceSummaryToValue(findPreference("kk_duration"));
			bindPreferenceSummaryToValue(findPreference("kk_letter_size"));
			bindPreferenceSummaryToValue(findPreference("kk_interpolator"));
			bindPreferenceSummaryToValue(findPreference("kk_text_size"));
			bindPreferenceSummaryToValue(findPreference("kk_sysui"));
			final Preference pref = findPreference("kk_freq");
			
			int p = getActivity().getSharedPreferences("preferenceggs", Context.MODE_PRIVATE).getInt("kk_freq", 50);
			if (p == 50)
				pref.setSummary("Balanced");
			else if (p == 0)
				pref.setSummary("Special pastries will never be shown");
			else if (p <= 40 && p > 20)
				pref.setSummary("Special pastries will be rare");
			else if (p <= 20 && p != 0)
				pref.setSummary("Special pastries will be very rare");
			else if (p >= 60 && p < 80)
				pref.setSummary("Special pastries will be common");
			else if (p >= 80 && p != 100)
				pref.setSummary("Special pastries will be very common");
			else if (p == 100)
				pref.setSummary("Special pastries will be as common as normal ones");
			
			pref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					int t = Integer.parseInt(newValue.toString());
					if (t == 50)
						preference.setSummary("Balanced");
					else if (t == 0)
						preference.setSummary("Special pastries will never be shown");
					else if (t <= 40 && t > 20)
						preference.setSummary("Special pastries will be rare");
					else if (t <= 20 && t != 0)
						preference.setSummary("Special pastries will be very rare");
					else if (t >= 60 && t < 80)
						preference.setSummary("Special pastries will be common");
					else if (t >= 80 && t != 100)
						preference.setSummary("Special pastries will be very common");
					else if (t == 100)
						preference.setSummary("Special pastries will be as common as normal ones");
					return false;
				}
			});
		}
	}
	
	/**
	 * This fragment shows About preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class ABPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			PreferenceManager prefMgr = getPreferenceManager();
			setPreferenceName(prefMgr);
			
			addPreferencesFromResource(R.xml.pref_about);
			
			
		}
	}
	
	
	
	public static void setPreferenceName(PreferenceManager prefMgr){
		
		prefMgr.setSharedPreferencesName("preferenceggs");
		prefMgr.setSharedPreferencesMode(Context.MODE_PRIVATE);
		
	}
	
}
