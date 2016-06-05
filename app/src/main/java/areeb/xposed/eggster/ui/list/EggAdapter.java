package areeb.xposed.eggster.ui.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import areeb.xposed.eggster.Egg;
import areeb.xposed.eggster.R;

import java.util.ArrayList;

public class EggAdapter extends ArrayAdapter<Egg> {

    private Context context;
    private ArrayList<Egg> eggs;

    private boolean color = false;

    public EggAdapter(Context context, ArrayList<Egg> eggs) {
        super(context, 0, eggs);
        this.context = context;
        this.eggs = eggs;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Egg egg = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_easter_egg, parent, false);

        final SwitchCompat switchCompat = (SwitchCompat) convertView.findViewById(R.id.egg_select);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCompat.toggle();
            }
        });

        final TextView textView = (TextView) convertView.findViewById(R.id.egg_title);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.egg_image);

        String name = egg.getName();
        textView.setText(name);

        imageView.setImageResource(egg.getDrawableRes(context));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && name.equals(Egg.N_PREVIEW.getName())){
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    textView.setText(new String(Character.toChars(0x1F31A)));
                    imageView.setImageResource(R.drawable.logo_nm);
                    return false;
                }
            });
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_MAIN).setFlags(
                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                        .setClassName("areeb.xposed.eggster",
                                egg.getPackage()));
            }
        });

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                color = checked;

                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
