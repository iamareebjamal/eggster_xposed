package areeb.xposed.eggster.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import areeb.xposed.eggster.Egg;
import areeb.xposed.eggster.R;
import areeb.xposed.eggster.ui.list.EggAdapter;

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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Opening Platlogo", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(Intent.ACTION_MAIN).setFlags(
                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                        .setClassName("areeb.xposed.eggster",
                                "areeb.xposed.eggster.eggs.platlogo_n.PlatLogoActivity"));
            }
        });
    }

}
