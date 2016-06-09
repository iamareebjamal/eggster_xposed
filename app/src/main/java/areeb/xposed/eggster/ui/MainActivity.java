package areeb.xposed.eggster.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import areeb.xposed.eggster.Egg;
import areeb.xposed.eggster.R;
import areeb.xposed.eggster.preferences.PreferenceManager;
import areeb.xposed.eggster.ui.list.EggAdapter;
import de.psdev.licensesdialog.LicensesDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView eggList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    private void handleActivation(){
        if(!PreferenceManager.isModuleActive()){
            Snackbar snackbar = Snackbar.make(eggList, "Xposed Module isn't enabled", Snackbar.LENGTH_INDEFINITE);
            PackageManager packageManager = getPackageManager();

            final Intent intent = new Intent("de.robv.android.xposed.installer.OPEN_SECTION");
            intent.setPackage("de.robv.android.xposed.installer");
            intent.putExtra("section", "modules");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            List activities = packageManager.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            boolean isIntentSafe = activities.size() > 0;

            if(isIntentSafe){
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
            case R.id.licences:
                showLicenses();
                return true;
            case R.id.about:
                //showAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showLicenses() {
        new LicensesDialog.Builder(this)
                .setNotices(R.raw.notices)
                .setIncludeOwnLicense(true)
                .setThemeResourceId(R.style.AppTheme_Dialog)
                .build()
                .show();
    }

}
