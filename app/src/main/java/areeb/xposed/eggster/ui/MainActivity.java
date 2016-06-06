package areeb.xposed.eggster.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import areeb.xposed.eggster.ui.list.EggAdapter;
import de.psdev.licensesdialog.LicensesDialog;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView eggList = (ListView) findViewById(R.id.egg_list);

        ArrayList<Egg> eggs = new ArrayList<>();
        for (Egg e : Egg.values()) {
            eggs.add(e);
        }

        EggAdapter eggAdapter = new EggAdapter(this, eggs);
        eggList.setAdapter(eggAdapter);

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

    private void showLicenses(){
        new LicensesDialog.Builder(this)
                .setNotices(R.raw.notices)
                .setIncludeOwnLicense(true)
                .build()
                .show();
    }

}
