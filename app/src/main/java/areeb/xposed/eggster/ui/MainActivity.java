package areeb.xposed.eggster.ui;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import areeb.xposed.eggster.BuildConfig;
import areeb.xposed.eggster.Egg;
import areeb.xposed.eggster.R;
import areeb.xposed.eggster.preferences.PreferenceManager;
import areeb.xposed.eggster.ui.list.Contact;
import areeb.xposed.eggster.ui.list.EggAdapter;
import de.psdev.licensesdialog.LicensesDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView eggList;
    private static int ayy; // lmao

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleOldPreferences();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eggList = (ListView) findViewById(R.id.egg_list);

        ArrayList<Egg> eggs = new ArrayList<>();
        for (Egg e : Egg.values()) {
            eggs.add(e);
        }

        EggAdapter eggAdapter = new EggAdapter(this, eggs);
        eggList.setAdapter(eggAdapter);

        handleActivation();

    }

    private void handleActivation() {
        if (!PreferenceManager.isModuleActive()) {
            Snackbar snackbar = Snackbar.make(eggList, "Xposed Module isn't enabled", Snackbar.LENGTH_INDEFINITE);
            PackageManager packageManager = getPackageManager();

            final Intent intent = new Intent("de.robv.android.xposed.installer.OPEN_SECTION");
            intent.setPackage("de.robv.android.xposed.installer");
            intent.putExtra("section", "modules");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            List activities = packageManager.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            boolean isIntentSafe = activities.size() > 0;

            if (isIntentSafe) {
                snackbar.setAction("Enable", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(intent);
                    }
                });
            }
            snackbar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.egg:
                startActivity(new Intent(android.provider.Settings.ACTION_DEVICE_INFO_SETTINGS));
                return true;
            case R.id.licences:
                showLicenses();
                return true;
            case R.id.about:
                showAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAbout() {

        View view = getLayoutInflater().inflate(R.layout.about, null);
        // App Name
        ((TextView) view.findViewById(R.id.appName)).setText(getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME);
        view.findViewById(R.id.aboutTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv = (TextView) view;

                if(ayy < 5)
                    tv.setText(tv.getText() + "\n\nThere sure are a lot of Varshneys in my life");
                else if (ayy == 5)
                    tv.setText(tv.getText() + "\n\nEnough of that now dude");
                ayy++;
            }
        });

        populateContacts(view);

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(view);
        dialog.getWindow().setLayout(getResources().getDisplayMetrics().widthPixels, FrameLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void populateContacts(View view) {
        LinearLayout contactPanel = (LinearLayout) view.findViewById(R.id.contactPanel);
        Contact[] contacts = Contact.getContacts();
        for (Contact c : contacts) {
            ImageView im = new ImageView(getApplicationContext());
            im.setImageDrawable(VectorDrawableCompat.create(getResources(), c.resId, null));
            im.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
            im.setPadding(padding, padding, padding, padding);
            setBorderlessBackground(im);
            setClickHandlers(im, c);

            contactPanel.addView(im);
        }
    }

    private void setClickHandlers(View view, final Contact c) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getApplicationContext(), c.description, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = c.url;
                Intent i;

                if (url.startsWith("mailto:")) {
                    String mail = url.replaceFirst("mailto:", "");
                    i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_EMAIL, mail);
                } else {
                    i = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                }

                try {
                    startActivity(i);
                } catch (ActivityNotFoundException ane) {
                    Toast.makeText(getApplicationContext(), "No app can handle it! Oooh!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setBorderlessBackground(View view) {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        view.setBackgroundResource(backgroundResource);
        view.setClickable(true);
        typedArray.recycle();
    }

    private void showLicenses() {
        new LicensesDialog.Builder(this)
                .setNotices(R.raw.notices)
                .setIncludeOwnLicense(true)
                .setThemeResourceId(R.style.AppTheme_Dialog)
                .build()
                .show();
    }

    private void handleOldPreferences() {
        SharedPreferences pref = getSharedPreferences("ver_info", Context.MODE_PRIVATE);
        int ver = pref.getInt("version", 0);

        if (ver < 11) {
            new PreferenceManager(getApplicationContext()).clear(); // Remove old preferences
        }

        pref.edit().putInt("version", BuildConfig.VERSION_CODE).commit();
    }

}
