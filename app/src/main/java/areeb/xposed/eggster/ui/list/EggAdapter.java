package areeb.xposed.eggster.ui.list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import areeb.xposed.eggster.Egg;
import areeb.xposed.eggster.R;

import java.util.ArrayList;

public class EggAdapter extends ArrayAdapter<Egg> {

    private Context context;
    private ArrayList<Egg> eggs;

    public EggAdapter(Context context, ArrayList<Egg> eggs) {
        super(context, 0, eggs);
        this.context = context;
        this.eggs = eggs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Egg egg = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_easter_egg, parent, false);
        }

        final SwitchCompat switchCompat = (SwitchCompat) convertView.findViewById(R.id.egg_select);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCompat.toggle();
            }
        });

        TextView textView = (TextView) convertView.findViewById(R.id.egg_title);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.egg_image);

        textView.setText(egg.getName());

        String identifier = egg.getDrawable();
        int res_id = context.getResources().getIdentifier(identifier, "drawable", "areeb.xposed.eggster");
        imageView.setImageResource(res_id);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_MAIN).setFlags(
                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                        .setClassName("areeb.xposed.eggster",
                                egg.getPackage()));
            }
        });

        return convertView;
    }
}
